package com.kuricki.keyquest.data

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.GameLevelRepository
import com.kuricki.keyquest.db.UserSession
import com.kuricki.keyquest.utils.isOnline
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject

class LevelSelectScreenModel(val loginSession: UserSession, private val repository: GameLevelRepository, private val context: Context): ScreenModel {
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
                if(uiState.value.getLevelsFromApi && isOnline(context)) {
                    //on first launch update the levels
                    try{
                        _uiState.update { currentState ->
                            currentState.copy(
                                getLevelsFromApi = false
                            )
                        }

                        //get token
                        val t = KeyQuestApi.getToken()
                        //get levels from api
                        val l = KeyQuestApi.retrofitService.getLevels(t)
                        //update ui
                        setLevels(l)
                        //Add to db
                        l.forEach{ level ->
                            it.forEach { localLevel ->
                                if(localLevel.id == level.id) {
                                    if(localLevel.bestScore <= level.bestScore) {
                                        //if remote score is higher update local
                                        repository.saveLevel(level)
                                    } else {
                                        //if remote score is lower, update remote
                                        try {
                                            val json = mapOf(
                                                "lId"  to parseToJsonElement(localLevel.id.toString()),
                                                "score" to parseToJsonElement(localLevel.bestScore.toString())
                                            )
                                            KeyQuestApi.retrofitService.submitLevelScore(
                                                loginSession.token,
                                                JsonObject(json)
                                            )
                                        } catch (e: retrofit2.HttpException) {
                                            println("Error saving score to api")
                                            println(e)
                                        } catch (e: Exception) {
                                            println(e)
                                        }
                                    }
                                }
                            }
                        }
                    } catch (e: Exception){
                        println(e)
                    }
                }

                //if there are levels in db, use them
                if(it.isNotEmpty()){
                    println("Got levels from db")
                    setLevels(it.toMutableList())
                } else {
                    if(isOnline(context)) {
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
                    } else {
                        //TODO add message to user
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