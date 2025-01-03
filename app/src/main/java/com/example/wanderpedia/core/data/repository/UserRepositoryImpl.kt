package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.error.AuthException.UserNotFoundException
import com.example.wanderpedia.core.data.source.remote.AccountService
import com.example.wanderpedia.core.domain.model.User
import com.example.wanderpedia.core.domain.model.usecase.DataResult
import com.example.wanderpedia.core.domain.model.usecase.safeDataResult
import com.example.wanderpedia.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val accountService: AccountService
) : UserRepository {
    override val currentUser: Flow<DataResult<User>>
        get() = accountService.currentUser.map { user ->
            user?.let {
                DataResult.Success(user)
            } ?: DataResult.Error(UserNotFoundException())
        }


    override suspend fun createAnonymousAccount(
    ) = safeDataResult {
        accountService.createAnonymousAccount()
    }

    override suspend fun updateDisplayName(
        newDisplayName: String
    ) = safeDataResult {
        accountService.updateDisplayName(newDisplayName)
    }


    override suspend fun linkAccountWithGoogle(
        idToken: String
    ) = safeDataResult {
        accountService.linkAccountWithGoogle(idToken)
    }

    override suspend fun linkAccountWithEmail(
        email: String, password: String
    ) = safeDataResult {
        accountService.linkAccountWithEmail(email, password)
    }

    override suspend fun signInWithGoogle(
        idToken: String
    ) = safeDataResult {
        accountService.signInWithGoogle(idToken)
    }

    override suspend fun signInWithEmail(
        email: String, password: String
    ) = safeDataResult {
        accountService.signInWithEmail(email, password)
    }

    override suspend fun signOut() = safeDataResult {
        accountService.signOut()
    }

    override suspend fun deleteAccount() = safeDataResult {
        accountService.deleteAccount()
    }
}

