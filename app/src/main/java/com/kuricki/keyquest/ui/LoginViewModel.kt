package com.kuricki.keyquest.ui

import androidx.lifecycle.ViewModel
import com.kuricki.keyquest.data.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun setUsrName(userName: String) {
        _uiState.update{ currentState ->
            currentState.copy(
                usrName = userName
            )
        }
    }

    fun setPsw(password: String) {
        _uiState.update{ currentState ->
            currentState.copy(
                psw = password
            )
        }
    }
}