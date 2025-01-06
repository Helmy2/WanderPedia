package com.example.wanderpedia.core.data.source.remote

import com.example.wanderpedia.core.data.source.remote.api.model.CategoryResponse
import com.example.wanderpedia.core.data.source.remote.api.model.WonderResponse

interface RemoteManager {
    suspend fun getWonders(
        name: String?,
        location: String?,
        category: CategoryResponse?
    ): List<WonderResponse>
}