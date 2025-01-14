package com.example.wanderpedia.core.domain.repository

import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.User
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUser: Flow<Resource<User>>
    suspend fun createAnonymousAccount(): Resource<Unit>
    suspend fun updateDisplayName(newDisplayName: String): Resource<Unit>
    suspend fun linkAccountWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential): Resource<Unit>
    suspend fun linkAccountWithEmail(email: String, password: String): Resource<Unit>
    suspend fun signInWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential): Resource<Unit>
    suspend fun signInWithEmail(email: String, password: String): Resource<Unit>
    suspend fun resetPassword(email: String): Resource<Unit>
    suspend fun signOut(): Resource<Unit>
    suspend fun deleteAccount(): Resource<Unit>
}