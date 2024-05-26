package com.kuricki.keyquest.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
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

    fun loginUser() {
        println("Login", )
        viewModelScope.launch {
            try {
                val json = mapOf(
                        "username" to parseToJsonElement(_uiState.value.usrName),
                        "hash" to parseToJsonElement(_uiState.value.psw)
                )
                val r = KeyQuestApi.retrofitService.getLoginSession(
                    JsonObject(json)
                )
                println("Done " + r.id)
            } catch (e: HttpException) {
                println(e)
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}