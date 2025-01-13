package com.example.wanderpedia.features.detail.domain.usecase

import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.WonderWithDigitalis
import com.example.wanderpedia.core.domain.repository.WondersRepository
import javax.inject.Inject

class GetWonderByIdUseCase @Inject constructor(
    private val wondersRepository: WondersRepository
) {
    suspend operator fun invoke(id: String): Resource<WonderWithDigitalis> =
        wondersRepository.getWonderById(id)
}