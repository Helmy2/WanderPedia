package com.example.wanderpedia.core.domain.usecase

import com.example.wanderpedia.core.domain.repository.UserRepository
import javax.inject.Inject

class CreateAnonymousAccountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() =
        userRepository.createAnonymousAccount()
}