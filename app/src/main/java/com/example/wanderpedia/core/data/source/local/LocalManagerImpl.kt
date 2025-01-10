package com.example.wanderpedia.core.data.source.local

import com.example.wanderpedia.core.data.source.local.dao.WonderDao
import com.example.wanderpedia.core.data.source.local.model.CachedWonder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalManagerImpl @Inject constructor(
    private val wonderDao: WonderDao
) : LocalManager {
    override suspend fun insertWonders(wonders: List<CachedWonder>) {
        wonderDao.insertWonders(wonders)
    }

    override fun getAllWonders(): Flow<List<CachedWonder>> {
        return wonderDao.getAllWonders()
    }

    override suspend fun getWonderById(id: String): CachedWonder? {
        return wonderDao.getWonderById(id)
    }

    override fun getWonderByCategory(category: String): Flow<List<CachedWonder>> {
        return wonderDao.getWonderByCategory(category)
    }

    override fun getWondersBy(
        nameQuery: String?,
        locationQuery: String?,
        timePeriodQuery: String?,
        categoryQuery: String?
    ): Flow<List<CachedWonder>> {
        return wonderDao.getWondersBy(
            nameQuery = nameQuery,
            locationQuery = locationQuery,
            timePeriodQuery = timePeriodQuery,
            categoryQuery = categoryQuery
        )
    }
}