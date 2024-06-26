package com.kuricki.keyquest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSessionDao {
    @Insert(UserSession::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userSession: UserSession)

    @Delete(UserSession::class)
    suspend fun delete(userSession: UserSession)

    // Get specific session by id
    @Query("SELECT * FROM UserSession WHERE id = :userId")
    fun getSession(userId: Int): Flow<UserSession?>

    // Get the first session, should be the only one in the table
    @Query("SELECT * FROM UserSession ORDER BY id ASC LIMIT 1")
    fun getFirstSession(): Flow<UserSession?>
}