package com.example.wanderpedia.core.data.source.remote

import com.example.wanderpedia.core.data.error.AuthException.UserNotFoundException
import com.example.wanderpedia.core.domain.model.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AccountService {
    private val _currentUserFlow = MutableSharedFlow<User?>(replay = 1)

    override val currentUser: Flow<User?>
        get() = _currentUserFlow.onStart {
            notifyProfileUpdated()
        }

    override suspend fun createAnonymousAccount() {
        firebaseAuth.signInAnonymously().await()
    }

    init {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            notifyProfileUpdated()
        }
        firebaseAuth.addAuthStateListener(authStateListener)

        // Optional: Clean up the listener when this service is no longer needed
        Runtime.getRuntime().addShutdownHook(Thread {
            firebaseAuth.removeAuthStateListener(authStateListener)
        })
    }

    private fun notifyProfileUpdated() {
        _currentUserFlow.tryEmit(firebaseAuth.currentUser?.toUser())
    }

    override suspend fun updateDisplayName(
        newDisplayName: String
    ) {
        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName
        }

        getCurrentUser().updateProfile(profileUpdates).await()
        notifyProfileUpdated()
    }


    override suspend fun linkAccountWithGoogle(
        idToken: String
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        getCurrentUser().linkWithCredential(credential).await()
        notifyProfileUpdated()
    }

    override suspend fun linkAccountWithEmail(
        email: String, password: String
    ) {
        val credential = EmailAuthProvider.getCredential(email, password)
        getCurrentUser().linkWithCredential(credential).await()
        notifyProfileUpdated()
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