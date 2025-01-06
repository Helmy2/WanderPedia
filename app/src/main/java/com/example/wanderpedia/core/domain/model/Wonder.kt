package com.example.wanderpedia.core.domain.model


data class Wonder(
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

sealed class Category(
    val name: String,
) {
    data object SevenWonders : Category("Seven Ancient Wonders")
    data object SevenModernWonders : Category("Seven Modern Wonders")
    data object SevenNewWonders : Category("Seven New Wonders")
    data object Civ5 : Category("Wonder can be found in the video game Civilization VI")
    data object Civ6 : Category("Wonder can be found in the video game Civilization V")
    data object All : Category("All")
}

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
