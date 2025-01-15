package com.example.wanderpedia.main

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
sealed class AppDestinations {
    @Serializable
    data object Auth : AppDestinations()

    @Serializable
    data object Home : AppDestinations()

    @Serializable
    data object Discover : AppDestinations()

    @Serializable
    data object Favorite : AppDestinations()

    @Serializable
    data object Profile : AppDestinations()

    @Serializable
    data object Onboarding : AppDestinations()

    @Serializable
    data class Detail(val id: String) : AppDestinations()
}