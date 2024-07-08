package com.kuricki.keyquest.data

import androidx.compose.runtime.asIntState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.GameLevelRepository
import kotlinx.coroutines.launch

class SummaryScreenModel(val lvl: GameLevel, val score: Int, val repository: GameLevelRepository): ScreenModel {
    private val _bestScore = mutableStateOf(lvl.bestScore)
    val bestScore = _bestScore.asIntState()

    init {
        screenModelScope.launch {
            repository.getLevelById(lvl.id).collect {
                if (it == null || it.bestScore < _bestScore.value) {
                    //if no best score is present or
                    //the new score is higher update
                    repository.saveLevel(lvl)
                } else {
                    if(it.bestScore > _bestScore.value){
                        //if not best score
                        _bestScore.value = it.bestScore
                    }
                }
            }
        }
    }
}