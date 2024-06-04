package com.kuricki.keyquest.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userSession: UserSession)

    @Delete
    suspend fun delete(userSession: UserSession)

    @Query("SELECT * FROM UserSession WHERE id = :userId")
    suspend fun getSession(userId: Int)

}