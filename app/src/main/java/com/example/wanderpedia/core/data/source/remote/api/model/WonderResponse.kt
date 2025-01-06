package com.example.wanderpedia.core.data.source.remote.api.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WonderResponse(
    @SerialName("build_year") val buildYear: Int?,
    @SerialName("categories") val categories: List<CategoryResponse?>?,
    @SerialName("links") val links: LinksResponse?,
    @SerialName("location") val location: String?,
    @SerialName("name") val name: String?,
    @SerialName("summary") val summary: String?,
    @SerialName("time_period") val timePeriod: TimePeriodResponse?
)

enum class TimePeriodResponse {
    Prehistoric,
    Ancient,
    Classical,
    PostClassical,
    EarlyModern,
    Modern
}

@Serializable
enum class CategoryResponse {
    SevenWonders,
    SevenModernWonders,
    SevenNewWonders,
    Civ5,
    Civ6
}

@Serializable
data class LinksResponse(
    @SerialName("britannica") val britannica: String?,
    @SerialName("google_maps") val googleMaps: String?,
    @SerialName("images") val images: List<String?>?,
    @SerialName("trip_advisor") val tripAdvisor: String?,
    @SerialName("wiki") val wiki: String?
)