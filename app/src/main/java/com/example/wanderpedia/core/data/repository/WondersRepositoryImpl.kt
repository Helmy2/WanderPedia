package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.source.local.LocalManager
import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.model.CachedTimePeriod
import com.example.wanderpedia.core.data.source.local.model.toDomainWonder
import com.example.wanderpedia.core.data.source.local.model.toDomainWonderWithDigitalis
import com.example.wanderpedia.core.data.source.remote.RemoteManager
import com.example.wanderpedia.core.data.source.remote.model.toCached
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.model.WonderWithDigitalis
import com.example.wanderpedia.core.domain.model.toCached
import com.example.wanderpedia.core.domain.repository.WondersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WondersRepositoryImpl @Inject constructor(
    private val apiService: RemoteManager,
    private val localManager: LocalManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WondersRepository {

    override suspend fun refreshAllWonders(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            // Fetch data from API
            val result = apiService.getAllWonders()
            val wonders = result.map { it.toCached() }
            // Cache the data
            localManager.insertWonders(wonders)
        }
    }


    override suspend fun getAllWonders(): Result<List<Wonder>> = withContext(ioDispatcher) {
        runCatching {
            val result = localManager.getAllWonders()
            if (result.isNotEmpty()) {
                result.map { it.toDomainWonder() }
            } else {
                val result = apiService.getAllWonders()
                val wonders = result.map { it.toCached() }
                // Cache the data
                localManager.insertWonders(wonders)
                wonders.map { it.toDomainWonder() }
            }
        }
    }

    override suspend fun getWonderById(id: String): Result<WonderWithDigitalis> =
        withContext(ioDispatcher) {
            runCatching {
                // Check if data is cached
                val cachedWonder = localManager.getWonderById(id)
                if (cachedWonder != null) {
                    cachedWonder.toDomainWonderWithDigitalis()
                } else {
                    // Fetch data from API
                    val result = apiService.getWonderByName(id)
                    val wonder = result.toCached()

                    // Cache the data
                    localManager.insertWonder(wonder)

                    wonder.toDomainWonderWithDigitalis()
                }
            }
        }

    override suspend fun getWondersByCategory(category: Category): Result<List<Wonder>> =
        withContext(ioDispatcher) {
            runCatching {
                val result = localManager.getWonderByCategory(
                    category.toCached()?.name ?: Category.Unknown.name
                )
                if (result.isNotEmpty()) {
                    result.map { it.toDomainWonder() }
                } else {
                    val result = apiService.getWondersByCategory(
                        category.toCached()?.name ?: Category.Unknown.name
                    )
                    val wonders = result.map { it.toCached() }
                    // Cache the data
                    localManager.insertWonders(wonders)
                    wonders.map { it.toDomainWonder() }
                }
            }
        }

    override suspend fun getWondersBy(
        textQuery: String?, timePeriodQuery: CachedTimePeriod?, categoryQuery: CachedCategory?
    ): Result<List<Wonder>> = withContext(ioDispatcher) {
        runCatching {
            val result = localManager.getWondersBy(
                textQuery, timePeriodQuery?.name, categoryQuery?.name
            )
            result.map { it.toDomainWonder() }
        }
    }
}