package com.kuricki.keyquest.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuricki.keyquest.db.UserSession
import com.kuricki.keyquest.db.UserSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject

class LoginViewModel(private val repository: UserSessionRepository): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Sets the username
     * @param userName The username
     */
    fun setUsrName(userName: String) {
        if(userName.contains(' ')) {
            setError("Username cannot contain spaces")
            userName.trim(' ')
        } else {
            setError(null)
        }

        _uiState.update{ currentState ->
            currentState.copy(
                usrName = userName,
            )
        }
    }

    /**
     * Sets the password
     * @param password The password
     */
    fun setPsw(password: String) {
        if(password.contains(' ')) {
            setError("Password cannot contain spaces")
            password.trim(' ')
        } else {
            setError(null)
        }

        _uiState.update{ currentState ->
            currentState.copy(
                psw = password,
            )
        }
    }

    fun setPsw2(password: String) {
        if(password.contains(' ')) {
            setError("Password cannot contain spaces")
            password.trim(' ')
        } else {
            setError(null)
        }

        _uiState.update{ currentState ->
            currentState.copy(
                psw2 = password,
            )
        }
    }

    /**
     * Authenticate the user and set the session.
     * @param onLoginSuccess The callback function to be called when the login is successful
     */
    fun loginUser(onLoginSuccess: (a: UserSession) -> Unit) {
        println("Login")
        val u = _uiState.value.usrName
        val p = _uiState.value.psw
        // Check for empty username or password
        if(u.isEmpty() || p.isEmpty()) {
            setError("Username or password cannot be empty")
            return
        }

        // Check for invalid chars in username or password
        if(u.contains(' ') || p.contains(' ')) {
            setError("Username and password cannot contain spaces")
            return
        }

        // Make the API call
        viewModelScope.launch {
            try {
                val json = mapOf(
                        "username" to parseToJsonElement(u),
                        "hash" to parseToJsonElement(p)
                )
                val a = KeyQuestApi.retrofitService.getLoginSession(
                    JsonObject(json)
                )

                //save session in repository
                repository.setSession(a)

                //callback
                onLoginSuccess(a)
            } catch (e: retrofit2.HttpException) {
                if(e.code() == 406) {
                    // Wrong username or password
                    setError("Wrong username or password")
                }
            } catch (e: Exception) {
                println(e)
                setError("Login failed")
            }
        }
    }

    fun registerUser(onLoginSuccess: (a: UserSession) -> Unit) {
        println("Register")
        val u = _uiState.value.usrName
        val p = _uiState.value.psw
        val p2 = _uiState.value.psw2

        if(u.isEmpty() || p.isEmpty() || p2.isEmpty()) {
            setError("Username or password cannot be empty")
            return
        }

        // Check for invalid chars in username or password
        if(u.contains(' ') || p.contains(' ') || p2.contains(' ')) {
            setError("Username and password cannot contain spaces")
            return
        }

        if(p != p2) {
            setError("Passwords do not match")
            return
        }

        // Make the API call
        viewModelScope.launch {
            try {
                val json = mapOf(
                    "username" to parseToJsonElement(u),
                    "hash" to parseToJsonElement(p)
                )
                val a = KeyQuestApi.retrofitService.registerUser(
                    JsonObject(json)
                )

                //save session in repository
                repository.setSession(a)

                //callback
                onLoginSuccess(a)
            } catch (e: retrofit2.HttpException) {
                if(e.code() == 406) {
                    // Wrong username or password
                    setError("Wrong username or password")
                }
            } catch (e: Exception) {
                println(e)
                setError("Login failed")
            }
        }
    }

    fun checkSession(a: (UserSession?) -> Unit) {
        viewModelScope.launch {
            //update checkSession
            _uiState.update { currentState ->
                currentState.copy(
                    checkSession = false
                )
            }
            //Check is there a session saved
            val s = repository.getSession().collect {
                if (it != null) {
                    //if there is a session, check if it is still valid
                    try {
                        val response = KeyQuestApi.retrofitService.validateSession(it.token)
                        a(response)
                        //update checkSession
                        _uiState.update { currentState ->
                            currentState.copy(
                                checkSession = true
                            )
                        }
                    } catch (e: retrofit2.HttpException) {
                        //if the session is invalid, delete it
                        repository.delete(it)
                        a(null)
                    }
                } else {
                    //if there is no session
                    println("No session")
                    a(null)
                }
            }
        }
    }


    /**
     * Displays the error message in login screen.
     * @param error The error message, can be null to clear the error message
     */
    fun setError(error: String?) {
        _uiState.update{ currentState ->
            currentState.copy(
                error = error
            )
        }
    }
}