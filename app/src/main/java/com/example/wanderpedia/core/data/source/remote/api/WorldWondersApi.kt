package com.example.wanderpedia.core.data.source.remote.api

import com.example.wanderpedia.core.data.source.remote.api.model.WonderResponse
import retrofit2.http.GET

interface WorldWondersApi {
    @GET("wonders")
    suspend fun getWonders(): List<WonderResponse>
}