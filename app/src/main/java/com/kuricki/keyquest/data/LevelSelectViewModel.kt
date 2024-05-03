package com.kuricki.keyquest.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LevelSelectViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LevelSelectUiState())
    val uiState: StateFlow<LevelSelectUiState> = _uiState.asStateFlow()

    fun setUserName(_userName:String){
        _uiState.update{ currentState ->
            currentState.copy(
                userName = _userName
            )
        }
    }

    fun setLevels(newLevels: MutableList<Level>){
        _uiState.update{ currentState ->
            currentState.copy(
                levels = newLevels
            )
        }
    }

    fun updateLevel(_id: Int, _currentScore: Int, _bestScore: Int){
        //find the level and update it
        val l = _uiState.value.levels
        val i = l.indexOfFirst { it.id == _id }
        //update the list
        l[i].currentScore = _currentScore
        l[i].bestScore = _bestScore

        setLevels(l)
    }
}