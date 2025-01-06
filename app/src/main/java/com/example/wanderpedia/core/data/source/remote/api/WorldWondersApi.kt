package com.example.wanderpedia.core.data.source.remote.api

import com.example.wanderpedia.core.data.source.remote.api.model.CategoryResponse
import com.example.wanderpedia.core.data.source.remote.api.model.WonderResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WorldWondersApi {
    @GET("wonders")
    suspend fun getWonders(
        @Query("name") name: String? = null,
        @Query("location") location: String? = null,
        @Query("category") category: CategoryResponse? = null,
    ): List<WonderResponse>
}

