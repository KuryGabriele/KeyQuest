package com.kuricki.keyquest.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject

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

    fun loginUser(onLoginSuccess: (a: LoginSession) -> Unit) {
        println("Login", )
        val u = _uiState.value.usrName
        val p = _uiState.value.psw
        if(u.isEmpty() || p.isEmpty()) {
            //If username or password is empty, show error message
            setError("Username or password cannot be empty")
            return
        }

        viewModelScope.launch {
            try {
                val json = mapOf(
                        "username" to parseToJsonElement(u),
                        "hash" to parseToJsonElement(p)
                )
                val a = KeyQuestApi.retrofitService.getLoginSession(
                    JsonObject(json)
                )

                onLoginSuccess(a)
            } catch (e: retrofit2.HttpException) {
                if(e.code() == 406) {
                    setError("Wrong username or password")
                }
            } catch (e: Exception) {
                println(e)
                setError("Login failed")
            }
        }
    }

    fun setError(error: String) {
        _uiState.update{ currentState ->
            currentState.copy(
                error = error
            )
        }
    }
}