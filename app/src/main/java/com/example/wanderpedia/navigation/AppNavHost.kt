package com.example.wanderpedia.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wanderpedia.features.auth.ui.navigation.authNavigation
import com.example.wanderpedia.features.home.ui.navigation.homeNavigation


data class TopLevelRoute(
    val name: String, val route: AppDestinations, val icon: ImageVector
)

val TOP_LEVEL_ROUTES = listOf(
    TopLevelRoute(
        "Home", AppDestinations.Home, Icons.Filled.Home
    ),
    TopLevelRoute(
        "Discover", AppDestinations.Discover, Icons.Filled.Explore
    ),
    TopLevelRoute(
        "Favorite", AppDestinations.Favorite, Icons.Filled.Favorite
    ),
    TopLevelRoute(
        "Profile", AppDestinations.Profile, Icons.Filled.Person
    ),
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val stateDestinations = AppDestinations.Home
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            AnimatedVisibility(TOP_LEVEL_ROUTES.any { it.route.hashCode() == currentDestination?.id }) {
                NavigationBar(
                    content = {
                        TOP_LEVEL_ROUTES.forEachIndexed { i, topLevelRoute ->
                            NavigationBarItem(
                                alwaysShowLabel = false,
                                label = { Text(text = topLevelRoute.name) },
                                selected = currentDestination?.id == topLevelRoute.route.hashCode(),
                                icon = {
                                    Icon(
                                        topLevelRoute.icon,
                                        contentDescription = topLevelRoute.name
                                    )
                                },
                                onClick = {
                                    navController.navigate(route = topLevelRoute.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController, startDestination = stateDestinations,
            enterTransition = { scaleIn() + fadeIn() },
            exitTransition = { scaleOut() + fadeOut() },
            modifier = Modifier.padding(padding)
        ) {
            authNavigation(
                navController = navController,
                onCompleteAuth = {
                    navController.navigate(AppDestinations.Home)
                },
            )
            homeNavigation()
            composable<AppDestinations.Discover> {
                Text("Discover")
            }
            composable<AppDestinations.Favorite> {
                Text("Favorite")
            }
            composable<AppDestinations.Profile> {
                Text("Profile")
            }
        }
    }
}