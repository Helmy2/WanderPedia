package com.example.wanderpedia.features.profile.domain.usecase

import com.example.wanderpedia.core.domain.repository.UserRepository
import com.example.wanderpedia.core.domain.usecase.RefreshAllWondersUseCase
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepo: UserRepository,
    private val refreshAllWondersUseCase: RefreshAllWondersUseCase
) {
    suspend operator fun invoke(): Result<Unit> {
        val result = userRepo.signOut()
        return if (result.isSuccess) {
            refreshAllWondersUseCase()
        } else {
            result
        }
    }

}