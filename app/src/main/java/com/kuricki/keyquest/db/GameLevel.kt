package com.kuricki.keyquest.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "GameLevel")
data class GameLevel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val displayName: String,
    val difficulty: Int,
    val currentScore: Int,
    val bestScore: Int
)
