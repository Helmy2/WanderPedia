package com.example.wanderpedia.core.domain.repository

import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.model.CachedTimePeriod
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.model.WonderWithDigitalis

interface WondersRepository {
    suspend fun getWonderById(id: String): Result<WonderWithDigitalis>
    suspend fun refreshAllWonders(): Result<Unit>
    suspend fun getAllWonders(): Result<List<Wonder>>
    suspend fun getWondersByCategory(category: Category): Result<List<Wonder>>
    suspend fun getWondersBy(
        textQuery: String?, timePeriodQuery: CachedTimePeriod?, categoryQuery: CachedCategory?
    ): Result<List<Wonder>>
}