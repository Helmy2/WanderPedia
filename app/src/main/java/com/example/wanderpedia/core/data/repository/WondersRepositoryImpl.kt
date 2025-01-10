package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.source.local.LocalManager
import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.model.CachedTimePeriod
import com.example.wanderpedia.core.data.source.local.model.toDomain
import com.example.wanderpedia.core.data.source.remote.RemoteManager
import com.example.wanderpedia.core.data.source.remote.model.toCached
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.Wonder
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

    override fun getAllWonders(): Flow<Resource<List<Wonder>>> =
        localManager.getAllWonders().map { cachedWonders ->
            if (cachedWonders.isNotEmpty()) {
                Resource.Success(cachedWonders.map { it.toDomain() })
            } else {
                // Fetch data from API
                val result = apiService.getAllWonders()
                val wonders = result.map { it.toCached() }
                // Cache the data
                localManager.insertWonders(wonders)
                Resource.Success(wonders.map { it.toDomain() })
            }
        }.catch {
            Resource.Error(it)
        }.flowOn(ioDispatcher)


    // The API service don't have a getWonderById method
    // So we have to fetch all the wonders and filter the one we want
    override suspend fun getWonderById(id: String): Resource<Wonder> = withContext(ioDispatcher) {
        safeResource {
            // Check if data is cached
            val cachedWonder = localManager.getWonderById(id)
            if (cachedWonder != null) {
                cachedWonder.toDomain()
            } else {
                // Fetch data from API
                val result = apiService.getAllWonders()
                val wonders = result.map { it.toCached() }

                // Cache the data
                localManager.insertWonders(wonders)

                val wonder = wonders.firstOrNull { it.id == id }
                if (wonder == null) {
                    throw Exception("Wonder with id $id not found")
                } else {
                    wonder.toDomain()
                }
            }
        }
    }

    override fun getWondersByCategory(category: Category): Flow<Resource<List<Wonder>>> =
        localManager.getWonderByCategory(category.toCached().name).map { cachedWonders ->
            if (cachedWonders.isNotEmpty()) {
                Resource.Success(cachedWonders.map { it.toDomain() })
            } else {
                // Fetch data from API
                val result = apiService.getWondersByCategory(category.toCached().name)
                val wonders = result.map { it.toCached() }
                // Cache the data
                localManager.insertWonders(wonders)
                Resource.Success(wonders.map { it.toDomain() })
            }
        }.catch {
            Resource.Error(it)
        }


    override fun getWondersBy(
        nameQuery: String?,
        locationQuery: String?,
        timePeriodQuery: CachedTimePeriod?,
        categoryQuery: CachedCategory?
    ): Flow<Resource<List<Wonder>>> = localManager.getWondersBy(
        nameQuery = nameQuery,
        locationQuery = locationQuery,
        timePeriodQuery = timePeriodQuery?.name,
        categoryQuery = categoryQuery?.name
    ).map { cachedWonders ->
        Resource.Success(cachedWonders.map { it.toDomain() })
    }.catch {
        Resource.Error(it)
    }.flowOn(ioDispatcher)
}