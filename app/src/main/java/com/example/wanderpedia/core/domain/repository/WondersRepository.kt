package com.example.wanderpedia.core.domain.repository

import com.example.wanderpedia.core.data.source.local.database.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.database.model.CachedTimePeriod
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.model.WonderWithDetails

interface WondersRepository {
    suspend fun getWonderById(id: String): Result<WonderWithDetails>
    suspend fun refreshAllWonders(): Result<Unit>
    suspend fun getAllWonders(): Result<List<Wonder>>
    suspend fun getWondersByCategory(category: Category): Result<List<Wonder>>
    suspend fun getWondersBy(
        textQuery: String?, timePeriodQuery: CachedTimePeriod?, categoryQuery: CachedCategory?
    ): Result<List<Wonder>>
    suspend fun updateWonderFavorite(id: String, isFavorite: Boolean): Result<Unit>
}