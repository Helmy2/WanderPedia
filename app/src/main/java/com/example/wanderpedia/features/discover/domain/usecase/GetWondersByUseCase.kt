package com.example.wanderpedia.features.discover.domain.usecase

import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.model.CachedTimePeriod
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.repository.WondersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWondersByUseCase @Inject constructor(
    private val wondersRepository: WondersRepository
) {
    operator fun invoke(
        textQuery: String? = null,
        timePeriodQuery: CachedTimePeriod? = null,
        categoryQuery: CachedCategory? = null
    ): Flow<Resource<List<Wonder>>> =
        wondersRepository.getWondersBy(
            textQuery = textQuery,
            timePeriodQuery = timePeriodQuery,
            categoryQuery = categoryQuery
        )
}