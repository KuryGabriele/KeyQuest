package com.kuricki.keyquest.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserSession")
data class UserSession(
    @PrimaryKey val id: Int,
    val username: String,
    val token: String,
    val tokenExpire: Long
)
