package com.example.wanderpedia.features.profile.domain.usecase

import com.example.wanderpedia.core.domain.repository.UserRepository
import javax.inject.Inject

class UpdateDisplayNameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(displayName: String) =
        userRepository.updateDisplayName(displayName)
}