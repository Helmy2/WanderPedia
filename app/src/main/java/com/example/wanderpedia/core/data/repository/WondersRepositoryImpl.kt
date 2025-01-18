package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.source.local.LocalManager
import com.example.wanderpedia.core.data.source.local.database.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.database.model.CachedTimePeriod
import com.example.wanderpedia.core.data.source.local.database.model.toDomainWonder
import com.example.wanderpedia.core.data.source.local.database.model.toDomainWonderWithDigitalis
import com.example.wanderpedia.core.data.source.remote.RemoteManager
import com.example.wanderpedia.core.data.source.remote.model.toCached
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.model.WonderWithDetails
import com.example.wanderpedia.core.domain.model.toCached
import com.example.wanderpedia.core.domain.repository.WondersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WondersRepositoryImpl @Inject constructor(
    private val remoteManager: RemoteManager,
    private val localManager: LocalManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WondersRepository {

    override suspend fun refreshAllWonders(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            // Fetch data from API
            val favoriteWonderIds = remoteManager.getFavoriteWondersId()
            val result = remoteManager.getAllWonders()
            val wonders =
                result.map { it.toCached(isFavorite = favoriteWonderIds.contains(it.name)) }
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
                val favoriteWonderIds = remoteManager.getFavoriteWondersId()
                val result = remoteManager.getAllWonders()
                val wonders =
                    result.map { it.toCached(isFavorite = favoriteWonderIds.contains(it.name)) }
                // Cache the data
                localManager.insertWonders(wonders)
                wonders.map { it.toDomainWonder() }
            }
        }
    }

    override suspend fun getWonderById(id: String): Result<WonderWithDetails> =
        withContext(ioDispatcher) {
            runCatching {
                // Check if data is cached
                val cachedWonder = localManager.getWonderById(id)
                if (cachedWonder != null) {
                    cachedWonder.toDomainWonderWithDigitalis()
                } else {
                    // Fetch data from API
                    val favoriteWonderIds = remoteManager.getFavoriteWondersId()

                    val result = remoteManager.getWonderByName(id)
                    val wonder = result.toCached(isFavorite = favoriteWonderIds.contains(id))

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
                    val favoriteWonderIds = remoteManager.getFavoriteWondersId()

                    val result = remoteManager.getWondersByCategory(
                        category.toCached()?.name ?: Category.Unknown.name
                    )
                    val wonders =
                        result.map { it.toCached(isFavorite = favoriteWonderIds.contains(it.name)) }
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

    override suspend fun updateWonderFavorite(id: String, isFavorite: Boolean): Result<Unit> =
        withContext(ioDispatcher) {
            runCatching {
                if (isFavorite) {
                    remoteManager.addFavoriteWonder(id)
                } else {
                    remoteManager.removeFavoriteWonder(id)
                }

                localManager.updateWonderFavorite(id, isFavorite)
            }
        }


    override fun getFavoriteWonders(): Flow<Result<List<Wonder>>> =
        localManager.getFavoriteWonders().map {
            it.runCatching { it.map { it.toDomainWonder() } }
        }
}