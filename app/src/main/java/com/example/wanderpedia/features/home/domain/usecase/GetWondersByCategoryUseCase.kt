package com.example.wanderpedia.features.home.domain.usecase

import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.repository.WondersRepository
import javax.inject.Inject

class GetWondersByCategoryUseCase @Inject constructor(
    private val wonderRepository: WondersRepository
) {
    suspend operator fun invoke(category: Category) =
        wonderRepository.getWonders(name = "", location = "", category = category)
}