package com.example.wanderpedia.core.data.source.local

import com.example.wanderpedia.core.data.source.local.database.dao.WonderDao
import com.example.wanderpedia.core.data.source.local.database.model.CachedWonder
import javax.inject.Inject

class LocalManagerImpl @Inject constructor(
    private val wonderDao: WonderDao
) : LocalManager {
    override suspend fun insertWonder(wonder: CachedWonder) {
        wonderDao.insertWonder(wonder)
    }

    override suspend fun insertWonders(wonders: List<CachedWonder>) {
        wonderDao.insertWonders(wonders)
    }

    override fun getAllWonders(): List<CachedWonder> {
        return wonderDao.getAllWonders()
    }

    override suspend fun getWonderById(id: String): CachedWonder? {
        return wonderDao.getWonderById(id)
    }

    override fun getWonderByCategory(category: String): List<CachedWonder> {
        return wonderDao.getWonderByCategory(category)
    }

    override fun getWondersBy(
        textQuery: String?,
        timePeriodQuery: String?,
        categoryQuery: String?
    ): List<CachedWonder> {
        return wonderDao.getWondersBy(
            textQuery = textQuery,
            timePeriodQuery = timePeriodQuery,
            categoryQuery = categoryQuery
        )
    }

    override suspend fun updateWonderFavorite(id: String, isFavorite: Boolean) {
        wonderDao.updateWonderFavorite(id, isFavorite)
    }

}