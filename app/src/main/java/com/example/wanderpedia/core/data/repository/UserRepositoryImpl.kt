package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.error.AuthException.UserNotFoundException
import com.example.wanderpedia.core.domain.model.User
import com.example.wanderpedia.core.domain.model.usecase.DataResult
import com.example.wanderpedia.core.domain.model.usecase.safeDataResult
import com.example.wanderpedia.core.domain.repository.UserRepository
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
) : UserRepository {
    override val currentUser: Flow<DataResult<User>>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                trySend(
                    safeDataResult {
                        auth.currentUser?.toUser() ?: throw UserNotFoundException()
                    }
                )
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun createAnonymousAccount(): DataResult<Unit> = safeDataResult {
        auth.signInAnonymously().await()
    }

    override suspend fun updateDisplayName(
        newDisplayName: String
    ): DataResult<Unit> = safeDataResult {
        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName
        }

        getCurrentUser().updateProfile(profileUpdates).await()
    }


    override suspend fun linkAccountWithGoogle(
        idToken: String
    ): DataResult<Unit> = safeDataResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        getCurrentUser().linkWithCredential(credential).await()
    }

    override suspend fun linkAccountWithEmail(
        email: String, password: String
    ): DataResult<Unit> = safeDataResult {
        val credential = EmailAuthProvider.getCredential(email, password)
        getCurrentUser().linkWithCredential(credential).await()
    }

    override suspend fun signInWithGoogle(
        idToken: String
    ): DataResult<Unit> = safeDataResult {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).await()
    }

    override suspend fun signInWithEmail(
        email: String, password: String
    ): DataResult<Unit> = safeDataResult {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signOut(): DataResult<Unit> = safeDataResult {
        auth.signOut()

        // Sign the user back in anonymously.
        auth.signInAnonymously().await()
    }

    override suspend fun deleteAccount(): DataResult<Unit> = safeDataResult {
        getCurrentUser().delete().await()
    }

    private fun getCurrentUser(): FirebaseUser {
        return auth.currentUser ?: throw UserNotFoundException()
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            id = this.uid,
            email = this.email ?: "",
            displayName = this.displayName ?: "",
            isAnonymous = this.isAnonymous
        )
    }
}

