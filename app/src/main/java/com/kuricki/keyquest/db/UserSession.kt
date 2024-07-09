package com.kuricki.keyquest.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "UserSession")
data class UserSession(
    @PrimaryKey val id: Int,
    val username: String,
    val token: String,
    val tokenExpire: Long
)
