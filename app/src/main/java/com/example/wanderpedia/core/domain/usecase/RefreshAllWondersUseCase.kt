package com.example.wanderpedia.core.domain.usecase

import com.example.wanderpedia.core.domain.repository.WondersRepository
import javax.inject.Inject

class RefreshAllWondersUseCase @Inject constructor(
    private val repository: WondersRepository
) {
    suspend operator fun invoke() =
        repository.refreshAllWonders()
}