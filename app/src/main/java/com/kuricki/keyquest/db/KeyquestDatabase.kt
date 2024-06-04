package com.kuricki.keyquest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameLevel::class, UserSession::class], version = 1, exportSchema = false)
abstract class KeyquestDatabase: RoomDatabase() {
    abstract fun UserSessionDao(): UserSessionDao

    companion object {
        @Volatile
        private var Instance: KeyquestDatabase? = null
        
        fun getDatabase(context: Context): KeyquestDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    KeyquestDatabase::class.java,
                    "keyquest_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
            }
        }
    }
}