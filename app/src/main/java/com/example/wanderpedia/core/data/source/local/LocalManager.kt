package com.example.wanderpedia.core.data.source.local

import com.example.wanderpedia.core.data.source.local.model.CachedWonder
import kotlinx.coroutines.flow.Flow

interface LocalManager {
    suspend fun insertWonders(wonders: List<CachedWonder>)
    fun getAllWonders(): Flow<List<CachedWonder>>
    suspend fun getWonderById(id: String): CachedWonder?
    fun getWonderByCategory(category: String): Flow<List<CachedWonder>>
    fun getWondersBy(
        nameQuery: String?,
        locationQuery: String?,
        timePeriodQuery: String?,
        categoryQuery: String?
    ): Flow<List<CachedWonder>>
}