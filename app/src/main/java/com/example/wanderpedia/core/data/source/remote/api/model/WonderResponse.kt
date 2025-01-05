package com.example.wanderpedia.core.data.source.remote.api.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WonderResponse(
    @SerialName("build_year") val buildYear: Int?,
    @SerialName("categories") val categories: List<String?>?,
    @SerialName("links") val links: Links?,
    @SerialName("location") val location: String?,
    @SerialName("name") val name: String?,
    @SerialName("summary") val summary: String?,
    @SerialName("time_period") val timePeriod: String?
)

@Serializable
data class Links(
    @SerialName("britannica") val britannica: String?,
    @SerialName("google_maps") val googleMaps: String?,
    @SerialName("images") val images: List<String?>?,
    @SerialName("trip_advisor") val tripAdvisor: String?,
    @SerialName("wiki") val wiki: String?
)