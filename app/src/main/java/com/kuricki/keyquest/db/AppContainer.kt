package com.kuricki.keyquest.db

import android.content.Context


interface AppContainer {
    val gameLevelRepository: GameLevelRepository
    val userSessionRepository: UserSessionRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val gameLevelRepository: GameLevelRepository by lazy {
        OfflineGameLevelRepository(KeyquestDatabase.getDatabase(context).gameLevelDao())
    }

    override val userSessionRepository: UserSessionRepository by lazy {
        OfflineUserSessionRepository(KeyquestDatabase.getDatabase(context).userSessionDao())
    }

}