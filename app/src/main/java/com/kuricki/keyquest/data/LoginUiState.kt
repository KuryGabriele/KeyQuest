package com.kuricki.keyquest.data

data class LoginUiState(
    val usrName: String = "",
    val psw: String = "",
    val error: String? = null
)
