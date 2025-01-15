package com.example.wanderpedia.core.domain.repository

import com.example.wanderpedia.core.domain.model.User
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUser: Flow<Result<User>>
    suspend fun createAnonymousAccount(): Result<Unit>
    suspend fun updateDisplayName(newDisplayName: String): Result<Unit>
    suspend fun linkAccountWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential): Result<Unit>
    suspend fun linkAccountWithEmail(email: String, password: String): Result<Unit>
    suspend fun signInWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential): Result<Unit>
    suspend fun signInWithEmail(email: String, password: String): Result<Unit>
    suspend fun resetPassword(email: String): Result<Unit>
    suspend fun signOut(): Result<Unit>
    suspend fun deleteAccount(): Result<Unit>
}