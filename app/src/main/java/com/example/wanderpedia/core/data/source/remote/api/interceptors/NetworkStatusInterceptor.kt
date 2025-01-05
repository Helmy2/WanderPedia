package com.example.wanderpedia.core.data.source.remote.api.interceptors

import com.example.wanderpedia.core.data.error.NetworkUnavailableException
import com.example.wanderpedia.core.data.source.remote.ConnectionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkStatusInterceptor @Inject constructor(
    private val connectionManager: ConnectionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionManager.isConnected) {
            chain.proceed(chain.request())
        } else {
            throw NetworkUnavailableException()
        }
    }
}
