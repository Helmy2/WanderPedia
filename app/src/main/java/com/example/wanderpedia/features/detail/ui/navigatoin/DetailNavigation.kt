package com.example.wanderpedia.features.detail.ui.navigatoin

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.features.detail.ui.DetailScreen
import com.example.wanderpedia.main.AppDestinations

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.detailNavigation(
    transitionScope: SharedTransitionScope,
    navigateBack: () -> Unit,
) {
    composable<AppDestinations.Detail> {
        DetailScreen(
            transitionScope = transitionScope,
            contentScope = this@composable,
            navigateBack = navigateBack
        )
    }
}