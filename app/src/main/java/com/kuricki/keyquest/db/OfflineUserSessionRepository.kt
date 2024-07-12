package com.kuricki.keyquest.db

import kotlinx.coroutines.flow.Flow

class OfflineUserSessionRepository(private val usDao: UserSessionDao): UserSessionRepository {
    override suspend fun getSession(): Flow<UserSession?> = usDao.getFirstSession()

    override suspend fun setSession(session: UserSession) = usDao.insert(session)

    override suspend fun delete(session: UserSession) = usDao.delete(session)
}

