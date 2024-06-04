package com.kuricki.keyquest

import android.app.Application
import com.kuricki.keyquest.db.AppContainer
import com.kuricki.keyquest.db.AppDataContainer

class KeyquestApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}