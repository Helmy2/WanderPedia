package com.example.wanderpedia.core.data.source.remote.api.mapper

import com.example.wanderpedia.core.data.source.remote.api.model.CategoryResponse
import com.example.wanderpedia.core.data.source.remote.api.model.TimePeriodResponse
import com.example.wanderpedia.core.data.source.remote.api.model.WonderResponse
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.TimePeriod
import com.example.wanderpedia.core.domain.model.Wonder

fun CategoryResponse?.toDomain(): Category {
    return when (this) {
        CategoryResponse.SevenWonders -> Category.SevenWonders
        CategoryResponse.SevenModernWonders -> Category.SevenModernWonders
        CategoryResponse.SevenNewWonders -> Category.SevenNewWonders
        CategoryResponse.Civ5 -> Category.Civ5
        CategoryResponse.Civ6 -> Category.Civ6
        else -> Category.All
    }
}

fun Category.toData(): CategoryResponse? {
    return when (this) {
        Category.Civ5 -> CategoryResponse.Civ5
        Category.Civ6 -> CategoryResponse.Civ6
        Category.SevenModernWonders -> CategoryResponse.SevenModernWonders
        Category.SevenNewWonders -> CategoryResponse.SevenNewWonders
        Category.SevenWonders -> CategoryResponse.SevenWonders
        Category.All -> null
    }
}

fun TimePeriodResponse?.toDomain(): TimePeriod {
    return when (this) {
        TimePeriodResponse.Prehistoric -> TimePeriod.Prehistoric
        TimePeriodResponse.Ancient -> TimePeriod.Ancient
        TimePeriodResponse.Classical -> TimePeriod.Classical
        TimePeriodResponse.PostClassical -> TimePeriod.PostClassical
        TimePeriodResponse.EarlyModern -> TimePeriod.EarlyModern
        TimePeriodResponse.Modern -> TimePeriod.Modern
        null -> TimePeriod.Unknown
    }
}

fun WonderResponse.toDomain(): Wonder {
    return Wonder(
        buildYear = buildYear ?: 0,
        location = location.orEmpty(),
        name = name.orEmpty(),
        summary = summary.orEmpty(),
        timePeriod = timePeriod.toDomain(),
        mapLink = links?.googleMaps.orEmpty(),
        tripAdvisorLink = links?.tripAdvisor.orEmpty(),
        wikiLink = links?.wiki.orEmpty(),
        images = links?.images?.filterNotNull().orEmpty(),
        categories = categories?.map { it.toDomain() }.orEmpty()
    )
}
