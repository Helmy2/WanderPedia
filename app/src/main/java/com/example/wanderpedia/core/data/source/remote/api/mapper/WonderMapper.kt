package com.example.wanderpedia.core.data.source.remote.api.mapper

import com.example.wanderpedia.core.data.source.remote.api.model.CategoryResponse
import com.example.wanderpedia.core.data.source.remote.api.model.TimePeriodResponse
import com.example.wanderpedia.core.data.source.remote.api.model.WonderResponse
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.TimePeriod
import com.example.wanderpedia.core.domain.model.Wonder
import java.util.UUID

fun CategoryResponse?.toDomain(): Category {
    return when (this) {
        CategoryResponse.SevenWonders -> Category.AncientWonders
        CategoryResponse.SevenModernWonders -> Category.ModernWonders
        CategoryResponse.SevenNewWonders -> Category.NewWonders
        CategoryResponse.Civ5 -> Category.Civ5Wonders
        CategoryResponse.Civ6 -> Category.Civ6Wonders
        else -> Category.All
    }
}

fun Category.toData(): CategoryResponse? {
    return when (this) {
        Category.Civ5Wonders -> CategoryResponse.Civ5
        Category.Civ6Wonders -> CategoryResponse.Civ6
        Category.ModernWonders -> CategoryResponse.SevenModernWonders
        Category.NewWonders -> CategoryResponse.SevenNewWonders
        Category.AncientWonders -> CategoryResponse.SevenWonders
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
        // TODO id is Uuid
        id = UUID.randomUUID().toString(),
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
