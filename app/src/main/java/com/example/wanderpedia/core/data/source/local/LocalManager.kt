package com.example.wanderpedia.core.data.source.local

import com.example.wanderpedia.core.data.source.local.model.CachedWonder

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
}