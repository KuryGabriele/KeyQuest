package com.kuricki.keyquest.db

import kotlinx.coroutines.flow.Flow

class OfflineGameLevelRepository(private val glDao: GameLevelDao): GameLevelRepository {
    override suspend fun getAllLevels(): Flow<List<GameLevel>> {
        return glDao.getAll()
    }

    override suspend fun getLevelById(id: Int): Flow<GameLevel?> {
        return glDao.getById(id)
    }

    override suspend fun saveLevel(level: GameLevel) = glDao.insert(level)

    override suspend fun deleteLevel(level: GameLevel) = glDao.delete(level)

    override suspend fun deleteAllLevels() = glDao.deleteAll()
}