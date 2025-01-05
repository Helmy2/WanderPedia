package com.example.wanderpedia.core.di

import com.example.wanderpedia.core.data.source.remote.api.ApiConstants.WORLD_WONDERS_ENDPOINT
import com.example.wanderpedia.core.data.source.remote.api.WorldWondersApi
import com.example.wanderpedia.core.data.source.remote.api.interceptors.LoggingInterceptor
import com.example.wanderpedia.core.data.source.remote.api.interceptors.NetworkStatusInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class WorldWondersRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @WorldWondersRetrofit
    fun provideRetrofit(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkStatusInterceptor: NetworkStatusInterceptor,
    ): Retrofit = Retrofit.Builder().baseUrl(WORLD_WONDERS_ENDPOINT).client(
        OkHttpClient().newBuilder().addInterceptor(networkStatusInterceptor)
            .addInterceptor(httpLoggingInterceptor).build()
    ).addConverterFactory(
        Json.asConverterFactory("application/json".toMediaType())
    ).build()


    @Provides
    fun provideHttpLoggingInterceptor(loggingInterceptor: LoggingInterceptor): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(loggingInterceptor)
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Singleton
    fun provideWonderApi(@WorldWondersRetrofit retrofit: Retrofit): WorldWondersApi =
        retrofit.create(WorldWondersApi::class.java)

}