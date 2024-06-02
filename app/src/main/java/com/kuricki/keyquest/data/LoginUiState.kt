package com.kuricki.keyquest.data

data class LoginUiState(
    val usrName: String = "", // username
    val psw: String = "", // password
    val error: String? = null //error message
)
