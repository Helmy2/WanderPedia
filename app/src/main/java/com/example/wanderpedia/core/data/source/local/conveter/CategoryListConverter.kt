package com.example.wanderpedia.core.data.source.local.conveter

import android.util.Log
import androidx.room.TypeConverter
import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import kotlinx.serialization.json.Json

class CategoryListConverter {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun fromCachedCategoryList(data: List<CachedCategory>?): String? {
        return data?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toCachedCategoryList(data: String?): List<CachedCategory>? {
        return data?.let {
            try {
                json.decodeFromString(it)
            } catch (e: Exception) {
                Log.d(TAG, "toStringList: $e")
                null
            }
        } ?: emptyList()
    }
}