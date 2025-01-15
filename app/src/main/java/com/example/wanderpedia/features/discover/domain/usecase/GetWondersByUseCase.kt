package com.example.wanderpedia.features.discover.domain.usecase

import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.model.CachedTimePeriod
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.repository.WondersRepository
import javax.inject.Inject

class GetWondersByUseCase @Inject constructor(
    private val wondersRepository: WondersRepository
) {
    suspend operator fun invoke(
        textQuery: String? = null,
        timePeriodQuery: CachedTimePeriod? = null,
        categoryQuery: CachedCategory? = null
    ): Result<List<Wonder>> =
        wondersRepository.getWondersBy(
            textQuery = textQuery,
            timePeriodQuery = timePeriodQuery,
            categoryQuery = categoryQuery
        )
}