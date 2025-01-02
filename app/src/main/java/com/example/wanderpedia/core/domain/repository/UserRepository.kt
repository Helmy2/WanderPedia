package com.example.wanderpedia.core.domain.repository

import com.example.wanderpedia.core.domain.model.User
import com.example.wanderpedia.core.domain.model.usecase.DataResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUser: Flow<DataResult<User>>
    suspend fun createAnonymousAccount(): DataResult<Unit>
    suspend fun updateDisplayName(newDisplayName: String): DataResult<Unit>
    suspend fun linkAccountWithGoogle(idToken: String): DataResult<Unit>
    suspend fun linkAccountWithEmail(email: String, password: String): DataResult<Unit>
    suspend fun signInWithGoogle(idToken: String): DataResult<Unit>
    suspend fun signInWithEmail(email: String, password: String): DataResult<Unit>
    suspend fun signOut(): DataResult<Unit>
    suspend fun deleteAccount(): DataResult<Unit>
}