package com.kuricki.keyquest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameLevelDao {
    @Insert(GameLevel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameLevel: GameLevel)

    @Delete(GameLevel::class)
    suspend fun delete(gameLevel: GameLevel)

    //get all game levels
    @Query("SELECT * FROM GameLevel")
    suspend fun getAll(): List<GameLevel>

    //get specific game level
    @Query("SELECT * FROM GameLevel WHERE id = :id")
    suspend fun getById(id: Int): GameLevel?
}