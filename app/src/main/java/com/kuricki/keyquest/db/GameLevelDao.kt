package com.kuricki.keyquest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameLevelDao {
    @Insert(GameLevel::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameLevel: GameLevel)

    @Delete(GameLevel::class)
    suspend fun delete(gameLevel: GameLevel)

    @Query("DELETE FROM GameLevel")
    suspend fun deleteAll()

    //get all game levels
    @Query("SELECT * FROM GameLevel")
    fun getAll(): Flow<List<GameLevel>>

    //get specific game level
    @Query("SELECT * FROM GameLevel WHERE id = :id")
    fun getById(id: Int): Flow<GameLevel?>
}