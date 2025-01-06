package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.source.remote.RemoteManager
import com.example.wanderpedia.core.data.source.remote.api.mapper.toData
import com.example.wanderpedia.core.data.source.remote.api.mapper.toDomain
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.model.safeResource
import com.example.wanderpedia.core.domain.repository.WondersRepository
import javax.inject.Inject

class WondersRepositoryImpl @Inject constructor(
    private val wondersApi: RemoteManager
) : WondersRepository {
    override suspend fun getWonders(
        name: String,
        location: String,
        category: Category
    ): Resource<List<Wonder>> = safeResource {
        wondersApi.getWonders(
            name = name.ifBlank { null },
            location = location.ifBlank { null },
            category = category.toData()
        ).map { it.toDomain() }
    }
}