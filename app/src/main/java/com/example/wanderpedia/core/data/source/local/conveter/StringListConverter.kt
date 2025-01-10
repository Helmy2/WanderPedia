package com.example.wanderpedia.core.data.source.local.conveter

import android.util.Log
import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

const val TAG = "StringListConverter"

class StringListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @TypeConverter
    fun fromStringList(data: List<String>?): String? {
        return data?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>? {
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