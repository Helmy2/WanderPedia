package com.example.wanderpedia.core.data.source.remote

import com.example.wanderpedia.core.data.source.remote.api.WorldWondersApi
import javax.inject.Inject

class RemoteManagerImpl @Inject constructor(
    private val wondersApi: WorldWondersApi
) : RemoteManager {
    override suspend fun getWonders() {
        wondersApi.getWonders()
    }
}