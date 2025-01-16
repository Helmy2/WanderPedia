package com.example.wanderpedia.features.detail.domain.usecase

import com.example.wanderpedia.core.domain.repository.WondersRepository
import javax.inject.Inject

class UpdateWonderFavoriteUseCase @Inject constructor(
    val wondersRepository: WondersRepository
) {
    suspend operator fun invoke(id: String, isFavorite: Boolean) =
        wondersRepository.updateWonderFavorite(id, isFavorite)
}