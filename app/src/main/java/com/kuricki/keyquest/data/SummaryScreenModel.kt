package com.kuricki.keyquest.data

import android.content.Context
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.mutableIntStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.GameLevelRepository
import com.kuricki.keyquest.db.UserSession
import com.kuricki.keyquest.utils.isOnline
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject

class SummaryScreenModel(private val loginSession: UserSession, private val context: Context, private val lvl: GameLevel, private val score: Int, private val repository: GameLevelRepository): ScreenModel {
    private val _bestScore = mutableIntStateOf(lvl.bestScore)
    val bestScore = _bestScore.asIntState()

    init {
        screenModelScope.launch {
            repository.getLevelById(lvl.id).collect {
                if (it == null || it.bestScore < _bestScore.intValue) {
                    //if no best score is present or
                    //the new score is higher update
                    repository.saveLevel(lvl)

                    if(isOnline(context)) {
                        //update best score on api
                        try {
                            val json = mapOf(
                                "lId"  to parseToJsonElement(lvl.id.toString()),
                                "score" to parseToJsonElement(score.toString())
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
                } else {
                    if(it.bestScore > _bestScore.intValue){
                        //if not best score
                        _bestScore.intValue = it.bestScore
                    }
                }
            }
        }
    }
}