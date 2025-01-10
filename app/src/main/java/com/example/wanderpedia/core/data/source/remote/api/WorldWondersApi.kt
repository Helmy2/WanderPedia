package com.example.wanderpedia.core.data.source.remote.api

import com.example.wanderpedia.core.data.source.remote.model.WonderResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WorldWondersApi {
    @GET("wonders")
    suspend fun getAllWonders(): List<WonderResponse>


    @GET("wonders")
    suspend fun getWondersByCategory(
        @Query("category") category: String,
    ): List<WonderResponse>
}

