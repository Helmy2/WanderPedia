package com.example.wanderpedia.features.favorite

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.features.favorite.ui.FavoriteScreen
import com.example.wanderpedia.main.AppDestinations

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.favoriteNavigation(
    transitionScope: SharedTransitionScope,
    navigateToDetail: (Wonder) -> Unit,
) {
    composable<AppDestinations.Favorite> {
        FavoriteScreen(
            transitionScope = transitionScope,
            contentScope = this@composable,
            navigateToDetail = navigateToDetail
        )
    }
}