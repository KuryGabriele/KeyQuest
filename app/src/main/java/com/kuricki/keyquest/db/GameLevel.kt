package com.kuricki.keyquest.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "GameLevel")
data class GameLevel(
    @PrimaryKey val id: Int,
    val displayName: String,
    val difficulty: Int,
    val bestScore: Int,
    val notes: String
)
