package com.example.wanderpedia.core.domain.repository

import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.model.CachedTimePeriod
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.model.WonderWithDigitalis
import kotlinx.coroutines.flow.Flow

interface WondersRepository {
    suspend fun getWonderById(id: String): Resource<WonderWithDigitalis>
    fun getAllWonders(): Flow<Resource<List<Wonder>>>
    fun getWondersByCategory(category: Category): Flow<Resource<List<Wonder>>>
    fun getWondersBy(
        textQuery: String?,
        timePeriodQuery: CachedTimePeriod?,
        categoryQuery: CachedCategory?
    ): Flow<Resource<List<Wonder>>>
}