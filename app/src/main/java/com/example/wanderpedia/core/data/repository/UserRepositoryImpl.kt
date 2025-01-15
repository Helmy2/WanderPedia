package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.error.AuthException.UserNotFoundException
import com.example.wanderpedia.core.data.source.remote.AccountService
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.User
import com.example.wanderpedia.core.domain.repository.UserRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override val currentUser: Flow<Result<User>>
        get() = accountService.currentUser.map { user ->
            user?.let {
                Result.success(user)
            } ?: Result.failure(UserNotFoundException())
        }.flowOn(ioDispatcher)


    override suspend fun createAnonymousAccount(
    ) = withContext(ioDispatcher) {
        runCatching {
            accountService.createAnonymousAccount()
        }
    }

    override suspend fun updateDisplayName(
        newDisplayName: String
    ) = withContext(ioDispatcher) {
        runCatching {
            accountService.updateDisplayName(newDisplayName)
        }
    }


    override suspend fun linkAccountWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) =
        withContext(ioDispatcher) {
            runCatching {
                accountService.linkAccountWithGoogle(googleIdTokenCredential.idToken)
            }
        }


    override suspend fun linkAccountWithEmail(
        email: String, password: String
    ) = withContext(ioDispatcher) {
        runCatching {
            accountService.linkAccountWithEmail(email, password)
        }
    }


    override suspend fun signInWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) =
        withContext(ioDispatcher) {
            runCatching {
                accountService.signInWithGoogle(googleIdTokenCredential.idToken)
            }
        }


    override suspend fun signInWithEmail(
        email: String, password: String
    ) = withContext(ioDispatcher) {
        runCatching {
            accountService.signInWithEmail(email, password)
        }
    }


    override suspend fun resetPassword(email: String) = withContext(ioDispatcher) {
        runCatching {
            accountService.resetPassword(email)
        }
    }

    override suspend fun signOut() = withContext(ioDispatcher) {
        runCatching {
            accountService.signOut()
        }
    }

    override suspend fun deleteAccount() = withContext(ioDispatcher) {
        runCatching {
            accountService.deleteAccount()
        }
    }
}

