package com.example.wanderpedia.navigation

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
        navController = navController, startDestination = stateDestinations
    ) {
        authNavigation(navController = navController, onCompleteAuth = {
            navController.navigate(AppDestinations.Home)
        })
        composable<AppDestinations.Home> {

        }
    }
}

