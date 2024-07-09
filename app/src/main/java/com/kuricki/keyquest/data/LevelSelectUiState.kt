package com.kuricki.keyquest.data

import com.kuricki.keyquest.db.GameLevel

data class LevelSelectUiState(
    val userName: String = "Kury",
    val levels: MutableList<GameLevel> = (mutableListOf(
        GameLevel(0, "Lvl1: Ode to joy!", 0,0, "C"),
        GameLevel(1, "Lvl2: Ode to joy!", 0,0, "c"),
        GameLevel(0, "Lvl1: Ode to joy!", 0,0, ""),
        GameLevel(1, "Lvl2: Ode to joy!", 0,0, ""),
        GameLevel(0, "Lvl1: Ode to joy!", 0,0, ""),
        GameLevel(1, "Lvl2: Ode to joy!", 0,0, "")
    )),
    val getLevels: Boolean = true,
    val getLevelsFromApi: Boolean = true
)
