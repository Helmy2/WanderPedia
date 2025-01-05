package com.example.wanderpedia.features.home.ui.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.navigation.AppDestinations

fun NavGraphBuilder.homeNavigation(
) {
    composable<AppDestinations.Home> {
        Text("Home")
    }
}



