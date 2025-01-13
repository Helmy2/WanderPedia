package com.example.wanderpedia.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wanderpedia.core.ui.component.DefaultNavBar
import com.example.wanderpedia.core.ui.component.TOP_LEVEL_ROUTES
import kotlinx.coroutines.delay


@Composable
fun WonderNav(
    onSplashHidden: () -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(Unit) {
        // TODO Simulate a delay before hiding the splash screen
        delay(100)
        onSplashHidden()
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                TOP_LEVEL_ROUTES.any { it.route.hashCode() == currentDestination?.id },
                enter = slideInVertically(initialOffsetY = { +it }),
                exit = slideOutVertically(targetOffsetY = { +it })
            ) {
                DefaultNavBar(
                    label = { Text(text = it.name) },
                    selected = { currentDestination?.id == it.route.hashCode() },
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.name
                        )
                    },
                    onClick = {
                        navController.navigate(route = it.route) {
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
    ) { padding ->
        AppNavHost(
            stateDestinations = AppDestinations.Home,
            navController = navController,
            modifier = Modifier.padding(padding),
        )
    }
}