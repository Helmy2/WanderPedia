package com.example.wanderpedia.core.data.source.remote

import com.example.wanderpedia.core.data.source.remote.api.WorldWondersApi
import com.example.wanderpedia.core.data.source.remote.api.model.CategoryResponse
import com.example.wanderpedia.core.data.source.remote.api.model.WonderResponse
import javax.inject.Inject

class RemoteManagerImpl @Inject constructor(
    private val wondersApi: WorldWondersApi
) : RemoteManager {

    override suspend fun getWonders(
        name: String?,
        location: String?,
        category: CategoryResponse?
    ): List<WonderResponse> {
        return wondersApi.getWonders(
            name = name,
            location = location,
            category = category
        )
    }
}