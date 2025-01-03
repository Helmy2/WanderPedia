package com.example.wanderpedia.features.auth.domain.usecase

import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.repository.UserRepository
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<Unit> {
        return userRepository.signInWithEmail(email, password)
    }
}