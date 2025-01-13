package com.example.wanderpedia.features.home.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.features.home.ui.HomeScreen
import com.example.wanderpedia.navigation.AppDestinations

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.homeNavigation(
    transitionScope: SharedTransitionScope,
    navigateToDetail: (id: String) -> Unit,
) {
    composable<AppDestinations.Home> {
        HomeScreen(
            transitionScope = transitionScope,
            contentScope = this@composable,
            navigateToDetail = navigateToDetail
        )
    }
}



