package com.example.wanderpedia.core.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wanderpedia.core.data.source.local.database.conveter.CategoryListConverter
import com.example.wanderpedia.core.data.source.local.database.conveter.StringListConverter
import com.example.wanderpedia.core.data.source.local.database.dao.WonderDao
import com.example.wanderpedia.core.data.source.local.database.model.CachedWonder

@Database(entities = [CachedWonder::class], version = 1, exportSchema = false)
@TypeConverters(
    value = [StringListConverter::class, CategoryListConverter::class]
)
abstract class WonderDatabase : RoomDatabase() {
    abstract fun wonderDao(): WonderDao
}