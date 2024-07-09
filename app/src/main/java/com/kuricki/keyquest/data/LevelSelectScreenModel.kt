package com.kuricki.keyquest.data

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.GameLevelRepository
import com.kuricki.keyquest.db.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LevelSelectScreenModel(val loginSession: UserSession, private val repository: GameLevelRepository): ScreenModel {
    private val _uiState = MutableStateFlow(LevelSelectUiState())
    val uiState: StateFlow<LevelSelectUiState> = _uiState.asStateFlow()

    init {
        println("LevelSelectViewModel init")
        setUserName(loginSession.username)
    }

    override fun onDispose() {
        println("LevelSelectViewModel onDispose")
    }

    fun getLevels(){
        screenModelScope.launch {
            //update ui
            _uiState.update { currentState ->
                currentState.copy(
                    getLevels = false,
                )
            }
            repository.getAllLevels().collect {
                //if there are levels in db, use them
                if(it.isNotEmpty() && !uiState.value.getLevelsFromApi){
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

                    _uiState.update { currentState ->
                        currentState.copy(
                            getLevelsFromApi = false
                        )
                    }
                }
            }
        }
    }

    /**
     * Set the username
     * @param _userName the username
     */
    private fun setUserName(_userName: String){
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
    private fun setLevels(newLevels: MutableList<GameLevel>){
        _uiState.update{ currentState ->
            currentState.copy(
                levels = newLevels
            )
        }
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