package com.example.wanderpedia.core.data.source.remote

import com.example.wanderpedia.core.data.source.remote.api.WorldWondersApi
import com.example.wanderpedia.core.data.source.remote.model.WonderResponse
import javax.inject.Inject

class RemoteManagerImpl @Inject constructor(
    private val wondersApi: WorldWondersApi
) : RemoteManager {
    override suspend fun getAllWonders(): List<WonderResponse> {
        return wondersApi.getAllWonders()
    }

    override suspend fun getWondersByCategory(category: String): List<WonderResponse> {
        return wondersApi.getWondersByCategory(category)
    }

    override suspend fun getWonderByName(name: String): WonderResponse {
        return wondersApi.getWonderByName(name)
    }

}