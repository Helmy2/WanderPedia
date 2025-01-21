package com.example.wanderpedia.features.auth.domain.usecase

import com.example.wanderpedia.core.domain.repository.UserRepository
import com.example.wanderpedia.core.domain.usecase.RefreshAllWondersUseCase
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val refreshAllWondersUseCase: RefreshAllWondersUseCase
) {
    suspend operator fun invoke(googleIdTokenCredential: GoogleIdTokenCredential): Result<Unit> {
        val result = userRepository.signInWithGoogle(googleIdTokenCredential)
        return if (result.isSuccess) {
            refreshAllWondersUseCase()
        } else {
            result
        }
    }

}

