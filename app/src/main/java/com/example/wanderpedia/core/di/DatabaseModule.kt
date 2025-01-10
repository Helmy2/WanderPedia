package com.example.wanderpedia.core.di

import android.content.Context
import androidx.room.Room
import com.example.wanderpedia.core.data.source.local.LocalManager
import com.example.wanderpedia.core.data.source.local.LocalManagerImpl
import com.example.wanderpedia.core.data.source.local.WonderDatabase
import com.example.wanderpedia.core.data.source.local.dao.WonderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Volatile
    private var instance: WonderDatabase? = null

    @Provides
    @Singleton
    fun provideWonderDatabase(@ApplicationContext context: Context): WonderDatabase {
        return instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }

    private fun buildDatabase(context: Context): WonderDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WonderDatabase::class.java,
            "wonder_database"
        ).fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    fun provideWonderDao(database: WonderDatabase): WonderDao {
        return database.wonderDao()
    }

    @Provides
    fun provideLocalManager(localManagerImpl: LocalManagerImpl): LocalManager {
        return localManagerImpl
    }
}