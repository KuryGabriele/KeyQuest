package com.kuricki.keyquest.data

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.GameLevelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LevelSelectScreenModel(private val repository: GameLevelRepository): ScreenModel {
    private val _uiState = MutableStateFlow(LevelSelectUiState())
    val uiState: StateFlow<LevelSelectUiState> = _uiState.asStateFlow()

    init {
        println("LevelSelectViewModel init")
    }

    override fun onDispose() {
        println("LevelSelectViewModel onDispose")
    }

    fun getLevels(){
        //TODO get levels from db, if not there get them from api
        screenModelScope.launch {
            repository.getAllLevels().collect {
                //if there are levels in db, use them
                if(it.isNotEmpty()){
                    println("Got levels from db")
                    setLevels(it.toMutableList())
                } else {
                    //if there are no levels in db, get them from api
                    println("Got levels from api")
                    try{
                        //get token
                        val t = KeyQuestApi.getToken()
                        //get levels from api
                        val l = KeyQuestApi.retrofitService.getLevels(t)
                        //update ui
                        setLevels(l)
                        //Add to db
                        l.forEach{ level ->
                            repository.saveLevel(level)
                        }

                    } catch (e: Exception){
                        println(e)
                    }
                }
            }

            //update ui
            _uiState.update { currentState ->
                currentState.copy(
                    getLevels = false
                )
            }
        }
    }

    /**
     * Set the username
     * @param _userName the username
     */
    fun setUserName(_userName: String){
        _uiState.update{ currentState ->
            currentState.copy(
                userName = _userName
            )
        }
    }

    /**
     * Set the game levels
     * @param newLevels the game levels
     */
    fun setLevels(newLevels: MutableList<GameLevel>){
        _uiState.update{ currentState ->
            currentState.copy(
                levels = newLevels
            )
        }
    }

    /**
     * Update single level
     * @param _id the level id
     * @param _currentScore the new score
     * @param _bestScore the new best score
     */
    fun updateLevel(_id: Int, _currentScore: Int, _bestScore: Int){
        //find the level and update it
        val l = _uiState.value.levels
        val i = l.indexOfFirst { it.id == _id }
        //update the list
        //TODO update the db
        //l[i].currentScore = _currentScore
        //l[i].bestScore = _bestScore

        setLevels(l)
    }

    fun deleteLevels() {
        _uiState.update { currentState ->
            currentState.copy(
                getLevels = false
            )
        }

        screenModelScope.launch {
            repository.deleteAllLevels()
        }
    }
}