package com.kuricki.keyquest.db

import android.content.Context


interface AppContainer {
    val gameLevelRepository: GameLevelRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val gameLevelRepository: GameLevelRepository by lazy {
        OfflineGameLevelRepository(KeyquestDatabase.getDatabase(context).gameLevelDao())
    }

}