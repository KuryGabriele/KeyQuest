package com.kuricki.keyquest.data

import android.content.Context
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.kuricki.keyquest.db.UserSession
import com.kuricki.keyquest.db.UserSessionRepository
import com.kuricki.keyquest.utils.isOnline
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.JsonObject
import java.security.MessageDigest

class LoginScreenModel(private val repository: UserSessionRepository): ScreenModel {
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
        var p = _uiState.value.psw
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

        //hash password
        val md = MessageDigest.getInstance("SHA-256")
        val digest: ByteArray = md.digest(("$u@$p").toByteArray())
        p = digest.fold("") { str, it -> str + "%02x".format(it) }

        // Make the API call
        screenModelScope.launch {
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
                KeyQuestApi.setToken(a.token)

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
        var p = _uiState.value.psw
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

        //hash password
        val md = MessageDigest.getInstance("SHA-256")
        val digest: ByteArray = md.digest(("$u@$p").toByteArray())
        p = digest.fold("") { str, it -> str + "%02x".format(it) }

        // Make the API call
        screenModelScope.launch {
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

    fun checkSession(context: Context, a: (UserSession?) -> Unit) {
        screenModelScope.launch {
            //update checkSession
            _uiState.update { currentState ->
                currentState.copy(
                    checkSession = false
                )
            }
            //Check is there a session saved
            repository.getSession().collect {
                if (it != null) {
                    //if there is a session, check if it is still valid
                    //if device is online
                    if(isOnline(context)) {
                        try {
                            val response = KeyQuestApi.retrofitService.validateSession(it.token)
                            KeyQuestApi.setToken(it.token)
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
                        //if device is offline
                        a(it)
                    }
                } else {
                    //if there is no session
                    println("No session")
                    a(null)
                }
            }
        }
    }

    fun logout(a: () -> Unit) {
        screenModelScope.launch {
            repository.getSession().collect {
                if (it != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            checkSession = false
                        )
                    }

                    repository.delete(it)
                    a()
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