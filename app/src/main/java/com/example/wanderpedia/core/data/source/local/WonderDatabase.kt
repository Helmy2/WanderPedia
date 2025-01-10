package com.example.wanderpedia.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wanderpedia.core.data.source.local.dao.WonderDao
import com.example.wanderpedia.core.data.source.local.model.CachedWonder

@Database(entities = [CachedWonder::class], version = 1, exportSchema = false)
abstract class WonderDatabase : RoomDatabase() {
    abstract fun wonderDao(): WonderDao
}