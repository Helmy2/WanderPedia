package com.example.wanderpedia.core.data.source.remote

import com.example.wanderpedia.core.data.source.remote.model.WonderResponse

interface RemoteManager {
    suspend fun getAllWonders(): List<WonderResponse>
    suspend fun getWondersByCategory(category: String): List<WonderResponse>
    suspend fun getWonderByName(name: String): WonderResponse
    suspend fun addFavoriteWonder(id: String)
    suspend fun removeFavoriteWonder(id: String)
    suspend fun getFavoriteWondersId(): List<String>
}