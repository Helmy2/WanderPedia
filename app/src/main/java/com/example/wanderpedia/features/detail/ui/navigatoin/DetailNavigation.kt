package com.example.wanderpedia.features.detail.ui.navigatoin

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.features.detail.ui.DetailScreen
import com.example.wanderpedia.navigation.AppDestinations

fun NavGraphBuilder.detailNavigation(
    navigateBack: () -> Unit
) {
    composable<AppDestinations.Detail> {
        DetailScreen(navigateBack = navigateBack)
    }
}