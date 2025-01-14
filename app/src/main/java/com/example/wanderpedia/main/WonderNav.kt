package com.example.wanderpedia.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wanderpedia.core.ui.ObserveEffect
import com.example.wanderpedia.core.ui.SnackbarController
import com.example.wanderpedia.core.ui.component.DefaultNavBar
import com.example.wanderpedia.core.ui.component.DefaultSnackbarHost
import com.example.wanderpedia.core.ui.component.TOP_LEVEL_ROUTES
import kotlinx.coroutines.launch


@Composable
fun WonderNav(
    showOnboarding: Boolean,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ObserveEffect(SnackbarController.events, snackbarHostState) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            if (it.message.isNotEmpty()) {
                val result = snackbarHostState.showSnackbar(
                    it.message,
                    it.action?.name,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    it.action?.action?.invoke()
                }
            }
        }
    }

    Scaffold(bottomBar = {
        AnimatedVisibility(
            TOP_LEVEL_ROUTES.any { it.route.hashCode() == currentDestination?.id },
            enter = slideInVertically(initialOffsetY = { +it }),
            exit = slideOutVertically(targetOffsetY = { +it })
        ) {
            DefaultNavBar(label = { Text(text = it.name) },
                selected = { currentDestination?.id == it.route.hashCode() },
                icon = {
                    Icon(
                        it.icon, contentDescription = it.name
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
                })
        }
    }, snackbarHost = {
        DefaultSnackbarHost(snackbarHostState)
    }) { padding ->
        AppNavHost(
            stateDestinations = if (showOnboarding) AppDestinations.Onboarding
            else AppDestinations.Home,
            navController = navController,
            modifier = Modifier.padding(padding),
        )
    }
}