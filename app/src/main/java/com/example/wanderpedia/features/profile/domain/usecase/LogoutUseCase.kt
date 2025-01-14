package com.example.wanderpedia.features.profile.domain.usecase

import com.example.wanderpedia.core.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepo: UserRepository
) {
    suspend operator fun invoke() =
        userRepo.signOut()
}