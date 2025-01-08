package com.example.wanderpedia.core.data.source.remote

import com.example.wanderpedia.core.data.error.AuthException.UserNotFoundException
import com.example.wanderpedia.core.domain.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AccountService {
    override val currentUser: Flow<User?>
        get() = callbackFlow {
            firebaseAuth.addAuthStateListener { auth ->
                trySend(auth.currentUser?.toUser())
            }
            awaitClose {
                firebaseAuth.removeAuthStateListener { auth ->
                    trySend(auth.currentUser?.toUser())
                }
            }
        }.onStart {
            emit(firebaseAuth.currentUser?.toUser())
        }

    override suspend fun createAnonymousAccount() {
        firebaseAuth.signInAnonymously().await()
    }

    override suspend fun updateDisplayName(
        newDisplayName: String
    ) {
        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName
        }

        getCurrentUser().updateProfile(profileUpdates).await()
    }


    override suspend fun linkAccountWithGoogle(
        idToken: String
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        getCurrentUser().linkWithCredential(credential).await()
    }

    override suspend fun linkAccountWithEmail(
        email: String, password: String
    ) {
        val credential = EmailAuthProvider.getCredential(email, password)
        getCurrentUser().linkWithCredential(credential).await()
    }

    override suspend fun signUpWithEmail(
        email: String, password: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signInWithGoogle(
        idToken: String
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).await()
    }

    override suspend fun signInWithEmail(
        email: String, password: String
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()

        // Sign the user back in anonymously.
        firebaseAuth.signInAnonymously().await()
    }

    override suspend fun deleteAccount() {
        getCurrentUser().delete().await()
    }

    override suspend fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }


    private fun getCurrentUser(): FirebaseUser {
        return firebaseAuth.currentUser ?: throw UserNotFoundException()
    }

    private fun FirebaseUser.toUser(): User {
        return User(
            id = this.uid,
            email = this.email ?: "",
            displayName = this.displayName ?: "",
            imageUrl = this.photoUrl?.toString() ?: "",
            isAnonymous = this.isAnonymous
        )
    }
}