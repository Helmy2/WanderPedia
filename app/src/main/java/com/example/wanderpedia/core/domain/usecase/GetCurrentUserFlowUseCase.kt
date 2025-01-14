package com.example.wanderpedia.core.domain.usecase

import com.example.wanderpedia.core.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserFlowUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() =
        userRepository.currentUser
}