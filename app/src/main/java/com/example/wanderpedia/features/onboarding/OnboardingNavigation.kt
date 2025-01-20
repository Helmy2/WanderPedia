package com.example.wanderpedia.features.onboarding

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.wanderpedia.main.AppDestinations

fun NavGraphBuilder.onboardingNavigation() {
    composable<AppDestinations.Onboarding> {
        OnboardingScreen()
    }
}



