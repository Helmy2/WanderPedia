package com.example.wanderpedia.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
sealed class AppDestinations {
    @Serializable
    object Auth : AppDestinations()

    @Serializable
    object Home : AppDestinations()
}