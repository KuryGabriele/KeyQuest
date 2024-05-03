package com.kuricki.keyquest.data

data class LevelSelectUiState(
    val userName: String = "Kury",
    val levels: MutableList<Level> = (mutableListOf(Level(0, "undefined", 0,0, 0)))
)
