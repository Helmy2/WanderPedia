package com.example.wanderpedia.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wanderpedia.features.auth.ui.navigation.authNavigation

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val stateDestinations = AppDestinations.Auth
    NavHost(
        navController = navController, startDestination = stateDestinations,
        enterTransition = { scaleIn() + fadeIn() },
        exitTransition = { scaleOut() + fadeOut() }
    ) {
        authNavigation(navController = navController, onCompleteAuth = {
            navController.navigate(AppDestinations.Home)
        })
        composable<AppDestinations.Home> {

        }
    }
}

