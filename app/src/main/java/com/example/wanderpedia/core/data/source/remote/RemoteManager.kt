package com.example.wanderpedia.core.data.source.remote

interface RemoteManager {
    suspend fun getWonders()
}