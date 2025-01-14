package com.example.wanderpedia.features.profile.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.features.profile.ui.ProfileScreen
import com.example.wanderpedia.navigation.AppDestinations

fun NavGraphBuilder.profileNavigation(
    navigateToLogin: () -> Unit,
) {
    composable<AppDestinations.Profile> {
        ProfileScreen(
            navigateToLogin = navigateToLogin
        )
    }
}