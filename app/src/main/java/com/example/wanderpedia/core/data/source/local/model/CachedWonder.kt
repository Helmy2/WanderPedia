package com.example.wanderpedia.core.data.source.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.wanderpedia.core.data.source.local.conveter.StringListConverter
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.TimePeriod
import com.example.wanderpedia.core.domain.model.Wonder

@Entity(tableName = "cached_wonders")
data class CachedWonder(
    @PrimaryKey val id: String,
    val buildYear: Int?,
    val location: String?,
    val name: String?,
    val summary: String?,
    val timePeriod: CachedTimePeriod,
    val mapLink: String?,
    val tripAdvisorLink: String?,
    val wikiLink: String?,
    @TypeConverters(StringListConverter::class)
    val images: List<String>,
    @TypeConverters(StringListConverter::class)
    val categories: List<CachedCategory>,
)

enum class CachedTimePeriod {
    Prehistoric,
    Ancient,
    Classical,
    PostClassical,
    EarlyModern,
    Modern,
    Unknown
}

enum class CachedCategory {
    SevenWonders,
    SevenModernWonders,
    SevenNewWonders,
    Civ5,
    Civ6,
    Unknown
}

fun CachedWonder.toDomain(): Wonder {
    return Wonder(
        id = id,
        buildYear = buildYear ?: 0,
        location = location ?: "Unknown",
        name = name ?: "Unknown",
        summary = summary ?: "Unknown",
        timePeriod = timePeriod.toDomain(),
        mapLink = mapLink ?: "Unknown",
        tripAdvisorLink = tripAdvisorLink ?: "Unknown",
        wikiLink = wikiLink ?: "Unknown",
        images = images,
        categories = categories.map { it.toDomain() }
    )
}


fun CachedTimePeriod?.toDomain(): TimePeriod {
    return when (this) {
        CachedTimePeriod.Prehistoric -> TimePeriod.Prehistoric
        CachedTimePeriod.Ancient -> TimePeriod.Ancient
        CachedTimePeriod.Classical -> TimePeriod.Classical
        CachedTimePeriod.PostClassical -> TimePeriod.PostClassical
        CachedTimePeriod.EarlyModern -> TimePeriod.EarlyModern
        CachedTimePeriod.Modern -> TimePeriod.Modern
        else -> TimePeriod.Unknown
    }
}

fun CachedCategory?.toDomain(): Category {
    return when (this) {
        CachedCategory.SevenWonders -> Category.AncientWonders
        CachedCategory.SevenModernWonders -> Category.ModernWonders
        CachedCategory.SevenNewWonders -> Category.NewWonders
        CachedCategory.Civ5 -> Category.Civ5Wonders
        CachedCategory.Civ6 -> Category.Civ6Wonders
        else -> Category.Unknown
    }
}