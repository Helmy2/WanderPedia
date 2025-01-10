package com.example.wanderpedia.core.domain.model

import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import kotlinx.serialization.Serializable

@Serializable
data class Wonder(
    val id: String,
    val buildYear: Int,
    val location: String,
    val name: String,
    val summary: String,
    val timePeriod: TimePeriod,
    val mapLink: String,
    val tripAdvisorLink: String,
    val wikiLink: String,
    val images: List<String>,
    val categories: List<Category>,
)

@Serializable
sealed class Category(
    val name: String,
) {
    data object AncientWonders : Category("Ancient Wonders")
    data object ModernWonders : Category("Modern Wonders")
    data object NewWonders : Category("New Wonders")
    data object Civ5Wonders : Category("Civilization VI")
    data object Civ6Wonders : Category("Civilization V")
    data object Unknown : Category("All")
}

@Serializable
sealed class TimePeriod(
    val name: String
) {
    data object Prehistoric : TimePeriod("Prehistoric")
    data object Ancient : TimePeriod("Ancient")
    data object Classical : TimePeriod("Classical")
    data object PostClassical : TimePeriod("PostClassical")
    data object EarlyModern : TimePeriod("EarlyModern")
    data object Modern : TimePeriod("Modern")
    data object Unknown : TimePeriod("Unknown")
}

fun Category.toCached(): CachedCategory {
    return when (this) {
        Category.AncientWonders -> CachedCategory.SevenWonders
        Category.ModernWonders -> CachedCategory.SevenModernWonders
        Category.NewWonders -> CachedCategory.SevenWonders
        Category.Civ5Wonders -> CachedCategory.Civ5
        Category.Civ6Wonders -> CachedCategory.Civ6
        Category.Unknown -> CachedCategory.Unknown
    }
}
