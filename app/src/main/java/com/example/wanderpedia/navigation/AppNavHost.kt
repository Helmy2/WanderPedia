package com.example.wanderpedia.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.wanderpedia.features.auth.ui.navigation.authNavigation
import com.example.wanderpedia.features.detail.ui.navigatoin.detailNavigation
import com.example.wanderpedia.features.discover.ui.navigation.discoverNavigation
import com.example.wanderpedia.features.home.ui.navigation.homeNavigation


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    stateDestinations: AppDestinations,
    modifier: Modifier = Modifier,
) {
//    sharedTransitionScope
//    animatedVisibilityScope: AnimatedVisibilityScope
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = stateDestinations,
            modifier = modifier
        ) {
            authNavigation(
                navController = navController,
                onCompleteAuth = {
                    navController.navigate(AppDestinations.Home)
                },
            )
            homeNavigation(
                transitionScope = this@SharedTransitionLayout,
            ) {
                navController.navigate(AppDestinations.Detail(it))
            }
            discoverNavigation(
                transitionScope = this@SharedTransitionLayout,
            ) {
                navController.navigate(AppDestinations.Detail(it))
            }
            composable<AppDestinations.Favorite> {
                Text("Favorite")
            }
            composable<AppDestinations.Profile> {
                Text("Profile")
            }
            detailNavigation(
                transitionScope = this@SharedTransitionLayout,
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}