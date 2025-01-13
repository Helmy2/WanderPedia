package com.example.wanderpedia.features.discover.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.features.discover.ui.DiscoverScreen
import com.example.wanderpedia.navigation.AppDestinations

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.discoverNavigation(
    transitionScope: SharedTransitionScope,
    navigateToDetail: (id: String) -> Unit,
) {
    composable<AppDestinations.Discover> {
        DiscoverScreen(
            transitionScope = transitionScope,
            contentScope = this@composable,
            navigateToDetail = navigateToDetail
        )
    }
}