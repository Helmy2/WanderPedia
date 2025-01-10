package com.example.wanderpedia.core.data.source.remote.model


import com.example.wanderpedia.core.data.source.local.model.CachedCategory
import com.example.wanderpedia.core.data.source.local.model.CachedTimePeriod
import com.example.wanderpedia.core.data.source.local.model.CachedWonder
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

@Serializable
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

fun WonderResponse.toCached(): CachedWonder {
    return CachedWonder(
        id = name ?: "",
        buildYear = buildYear,
        location = location,
        name = name,
        summary = summary,
        timePeriod = timePeriod.toCached(),
        mapLink = links?.googleMaps,
        tripAdvisorLink = links?.tripAdvisor,
        wikiLink = links?.wiki,
        images = links?.images?.filterNotNull().orEmpty(),
        categories = categories?.map { it.toCached() }.orEmpty()
    )
}

fun TimePeriodResponse?.toCached(): CachedTimePeriod {
    return when (this) {
        TimePeriodResponse.Prehistoric -> CachedTimePeriod.Prehistoric
        TimePeriodResponse.Ancient -> CachedTimePeriod.Ancient
        TimePeriodResponse.Classical -> CachedTimePeriod.Classical
        TimePeriodResponse.PostClassical -> CachedTimePeriod.PostClassical
        TimePeriodResponse.EarlyModern -> CachedTimePeriod.EarlyModern
        TimePeriodResponse.Modern -> CachedTimePeriod.Modern
        else -> CachedTimePeriod.Unknown
    }
}

fun CategoryResponse?.toCached(): CachedCategory {
    return when (this) {
        CategoryResponse.SevenWonders -> CachedCategory.SevenWonders
        CategoryResponse.SevenModernWonders -> CachedCategory.SevenModernWonders
        CategoryResponse.SevenNewWonders -> CachedCategory.SevenNewWonders
        CategoryResponse.Civ5 -> CachedCategory.Civ5
        CategoryResponse.Civ6 -> CachedCategory.Civ6
        else -> CachedCategory.Unknown
    }
}