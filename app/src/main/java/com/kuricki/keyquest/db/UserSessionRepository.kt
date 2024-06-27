package com.kuricki.keyquest.db

import kotlinx.coroutines.flow.Flow

interface UserSessionRepository {
    /**
     * Get a user session
     */
    suspend fun getSession(): Flow<UserSession?>

    /**
     * Set a user session
     */
    suspend fun setSession(session: UserSession)

    /**
     * Delete a user session
     */
    suspend fun delete(session: UserSession)
}