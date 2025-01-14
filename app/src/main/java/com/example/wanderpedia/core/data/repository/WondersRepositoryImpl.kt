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
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.model.WonderWithDigitalis
import com.example.wanderpedia.core.domain.model.safeResource
import com.example.wanderpedia.core.domain.model.toCached
import com.example.wanderpedia.core.domain.repository.WondersRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WondersRepositoryImpl @Inject constructor(
    private val apiService: RemoteManager,
    private val localManager: LocalManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : WondersRepository {

    override suspend fun refreshAllWonders(): Resource<Unit> = safeResource {
        // Fetch data from API
        val result = apiService.getAllWonders()
        val wonders = result.map { it.toCached() }
        // Cache the data
        localManager.insertWonders(wonders)
    }

    override fun getAllWonders(): Flow<Resource<List<Wonder>>> =
        localManager.getAllWonders().map { cachedWonders ->
            if (cachedWonders.isNotEmpty()) {
                Resource.Success(cachedWonders.map { it.toDomainWonder() })
            } else {
                // Fetch data from API
                val result = apiService.getAllWonders()
                val wonders = result.map { it.toCached() }
                // Cache the data
                localManager.insertWonders(wonders)
                Resource.Success(wonders.map { it.toDomainWonder() })
            }
        }.catch {
            Resource.Error(it)
        }.flowOn(ioDispatcher)


    override suspend fun getWonderById(id: String): Resource<WonderWithDigitalis> =
        withContext(ioDispatcher) {
            safeResource {
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

    override fun getWondersByCategory(category: Category): Flow<Resource<List<Wonder>>> =
        localManager.getWonderByCategory(category.toCached()?.name ?: Category.Unknown.name)
            .map { cachedWonders ->
                if (cachedWonders.isNotEmpty()) {
                    Resource.Success(cachedWonders.map { it.toDomainWonder() })
                } else {
                    // Fetch data from API
                    val result = apiService.getWondersByCategory(
                        category.toCached()?.name ?: Category.Unknown.name
                    )
                    val wonders = result.map { it.toCached() }
                    // Cache the data
                    localManager.insertWonders(wonders)
                    Resource.Success(wonders.map { it.toDomainWonder() })
                }
            }.catch {
                Resource.Error(it)
            }.flowOn(ioDispatcher)


    override fun getWondersBy(
        textQuery: String?, timePeriodQuery: CachedTimePeriod?, categoryQuery: CachedCategory?
    ): Flow<Resource<List<Wonder>>> = localManager.getWondersBy(
        textQuery = textQuery,
        timePeriodQuery = timePeriodQuery?.name,
        categoryQuery = categoryQuery?.name
    ).map { cachedWonders ->
        Resource.Success(cachedWonders.map { it.toDomainWonder() })
    }.catch {
        Resource.Error(it)
    }.flowOn(ioDispatcher)
}