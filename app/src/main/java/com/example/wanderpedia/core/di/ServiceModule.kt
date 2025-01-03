package com.example.wanderpedia.core.di

import com.example.wanderpedia.core.data.source.remote.AccountService
import com.example.wanderpedia.core.data.source.remote.AccountServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    @Singleton
    abstract fun bindAccountService(
        accountServiceImpl: AccountServiceImpl,
    ): AccountService
}