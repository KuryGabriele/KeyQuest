package com.kuricki.keyquest.data

data class LoginUiState(
    val usrName: String = "", // username
    val psw: String = "", // password
    val psw2: String = "", // repeat password
    val error: String? = null, //error message
    val checkSession: Boolean = true //should check be checked
)
