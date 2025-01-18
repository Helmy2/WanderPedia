package com.example.wanderpedia.features.favorite.domain.usecase

import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.repository.WondersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteWondersUseCase @Inject constructor(
    private val wondersRepository: WondersRepository
) {
    operator fun invoke(): Flow<Result<List<Wonder>>> =
        wondersRepository.getFavoriteWonders()
}