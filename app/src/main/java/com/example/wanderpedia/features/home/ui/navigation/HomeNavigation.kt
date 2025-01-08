package com.example.wanderpedia.features.home.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.features.home.ui.HomeScreen
import com.example.wanderpedia.navigation.AppDestinations

fun NavGraphBuilder.homeNavigation(
    navigateToDetail: (id: String) -> Unit
) {
    composable<AppDestinations.Home> {
        HomeScreen(navigateToDetail = navigateToDetail)
    }
}



