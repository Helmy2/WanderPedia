package com.example.wanderpedia.core.data.source.local

import com.example.wanderpedia.core.data.source.local.database.model.CachedWonder
import kotlinx.coroutines.flow.Flow

interface LocalManager {
    suspend fun insertWonder(wonders: CachedWonder)
    suspend fun insertWonders(wonders: List<CachedWonder>)
    fun getAllWonders(): List<CachedWonder>
    suspend fun getWonderById(id: String): CachedWonder?
    fun getWonderByCategory(category: String): List<CachedWonder>
    fun getWondersBy(
        textQuery: String?,
        timePeriodQuery: String?,
        categoryQuery: String?
    ): List<CachedWonder>

    suspend fun updateWonderFavorite(id: String, isFavorite: Boolean)
    fun getFavoriteWonders(): Flow<List<CachedWonder>>
}