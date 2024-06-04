package com.kuricki.keyquest.db

import kotlinx.coroutines.flow.Flow


interface GameLevelRepository {
    /**
     * Get all levels in the repository.
     */
    suspend fun getAllLevels(): Flow<List<GameLevel>>

    /**
     * Get a level by its ID.
     */
    suspend fun getLevelById(id: Int): Flow<GameLevel?>

    /**
     * Save a level to the repository.
     */
    suspend fun saveLevel(level: GameLevel)

    /**
     * Delete a level from the repository.
     */
    suspend fun deleteLevel(level: GameLevel)


    /**
     * Delete all levels from the repository.
     */
    suspend fun deleteAllLevels()

}