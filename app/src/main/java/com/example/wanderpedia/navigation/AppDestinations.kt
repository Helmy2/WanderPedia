package com.example.wanderpedia.navigation

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
    data class Detail(val id: String) : AppDestinations()
}