package com.example.wanderpedia.features.auth.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.wanderpedia.navigation.AppDestinations
import kotlinx.serialization.Serializable

fun NavGraphBuilder.authNavigation(
    navController: NavController,
    onCompleteAuth: () -> Unit
) {
    navigation<AppDestinations.Auth>(
        startDestination = AuthDestinations.SignIn
    ) {
        signInRute(
            onNavigateToSignUp = {
                navController.navigate(AuthDestinations.SignUp)
            },
            onNavigateToRestPassword = {
                navController.navigate(AuthDestinations.RestPassword)
            },
            onNavigateBack = {
                navController.popBackStack()
            },
            onComplete = {
                onCompleteAuth()
            }
        )

        signUpRute(
            onNavigateBack = {
                navController.popBackStack()
            },
            onComplete = {
                onCompleteAuth()
            }
        )

        restPasswordRute(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}

@Immutable
@Serializable
private sealed class AuthDestinations {
    @Serializable
    object SignIn : AuthDestinations()

    @Serializable
    object SignUp : AuthDestinations()

    @Serializable
    object RestPassword : AuthDestinations()
}

fun NavGraphBuilder.signInRute(
    onNavigateToSignUp: () -> Unit,
    onNavigateToRestPassword: () -> Unit,
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit
) {
    composable<AuthDestinations.SignIn> {

    }
}

fun NavGraphBuilder.signUpRute(
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit
) {
    composable<AuthDestinations.SignUp> {

    }
}

fun NavGraphBuilder.restPasswordRute(
    onNavigateBack: () -> Unit,
) {
    composable<AuthDestinations.RestPassword> {

    }
}

