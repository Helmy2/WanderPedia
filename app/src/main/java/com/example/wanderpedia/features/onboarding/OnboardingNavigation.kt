package com.example.wanderpedia.features.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.main.AppDestinations

fun NavGraphBuilder.onboardingNavigation(
    navigateHome: () -> Unit
) {
    composable<AppDestinations.Onboarding> {
        OnboardingScreen(navigateHome = navigateHome)
    }
}



