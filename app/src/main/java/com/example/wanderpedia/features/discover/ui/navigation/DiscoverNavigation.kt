package com.example.wanderpedia.features.discover.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.features.discover.ui.DiscoverScreen
import com.example.wanderpedia.navigation.AppDestinations

fun NavGraphBuilder.discoverNavigation(
    navigateToDetail: (id: String) -> Unit
) {
    composable<AppDestinations.Discover> {
        DiscoverScreen(navigateToDetail = navigateToDetail)
    }
}