package com.kuricki.keyquest.data

import androidx.lifecycle.ViewModel
import com.kuricki.keyquest.db.GameLevel
import com.kuricki.keyquest.db.GameLevelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LevelSelectViewModel(private val repository: GameLevelRepository): ViewModel() {
    private val _uiState = MutableStateFlow(LevelSelectUiState())
    val uiState: StateFlow<LevelSelectUiState> = _uiState.asStateFlow()

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
}