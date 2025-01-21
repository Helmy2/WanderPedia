package com.example.wanderpedia.features.auth.domain.usecase

import com.example.wanderpedia.core.domain.repository.UserRepository
import com.example.wanderpedia.core.domain.usecase.RefreshAllWondersUseCase
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val refreshAllWondersUseCase: RefreshAllWondersUseCase
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        val result = userRepository.signInWithEmail(email, password)
        return if (result.isSuccess) {
            refreshAllWondersUseCase()
        } else {
            result
        }
    }


}