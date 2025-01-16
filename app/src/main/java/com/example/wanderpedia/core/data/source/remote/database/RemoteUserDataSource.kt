package com.example.wanderpedia.core.data.source.remote.database

interface RemoteUserDataSource {
    suspend fun addFavoriteWonder(id: String)
    suspend fun removeFavoriteWonder(id: String)
    suspend fun getFavoriteWondersId(): List<String>
}