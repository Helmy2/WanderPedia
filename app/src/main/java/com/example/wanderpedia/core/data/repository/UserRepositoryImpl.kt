package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.error.AuthException.UserNotFoundException
import com.example.wanderpedia.core.data.source.remote.AccountService
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.User
import com.example.wanderpedia.core.domain.model.safeResource
import com.example.wanderpedia.core.domain.repository.UserRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val accountService: AccountService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override val currentUser: Flow<Resource<User>>
        get() = accountService.currentUser.map { user ->
            user?.let {
                Resource.Success(user)
            } ?: Resource.Error(UserNotFoundException())
        }.flowOn(ioDispatcher)


    override suspend fun createAnonymousAccount(
    ) = safeResource {
        accountService.createAnonymousAccount()
    }

    override suspend fun updateDisplayName(
        newDisplayName: String
    ) = safeResource {
        accountService.updateDisplayName(newDisplayName)
    }


    override suspend fun linkAccountWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) =
        safeResource {
            accountService.linkAccountWithGoogle(googleIdTokenCredential.idToken)
    }

    override suspend fun linkAccountWithEmail(
        email: String, password: String
    ) = safeResource {
        accountService.linkAccountWithEmail(email, password)
    }

    override suspend fun signInWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) =
        safeResource {
            accountService.signInWithGoogle(googleIdTokenCredential.idToken)
    }

    override suspend fun signInWithEmail(
        email: String, password: String
    ) = safeResource {
        accountService.signInWithEmail(email, password)
    }

    override suspend fun signOut() = safeResource {
        accountService.signOut()
    }

    override suspend fun deleteAccount() = safeResource {
        accountService.deleteAccount()
    }
}

