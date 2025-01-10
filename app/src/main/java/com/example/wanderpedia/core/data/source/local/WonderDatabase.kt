package com.example.wanderpedia.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.wanderpedia.core.data.source.local.conveter.CategoryListConverter
import com.example.wanderpedia.core.data.source.local.conveter.StringListConverter
import com.example.wanderpedia.core.data.source.local.dao.WonderDao
import com.example.wanderpedia.core.data.source.local.model.CachedWonder

@Database(entities = [CachedWonder::class], version = 1, exportSchema = false)
@TypeConverters(
    value = [StringListConverter::class, CategoryListConverter::class]
)
abstract class WonderDatabase : RoomDatabase() {
    abstract fun wonderDao(): WonderDao
}