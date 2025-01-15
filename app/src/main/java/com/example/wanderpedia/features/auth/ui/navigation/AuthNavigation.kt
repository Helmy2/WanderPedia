package com.example.wanderpedia.features.auth.ui.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.wanderpedia.features.auth.ui.resetpassword.RestPasswordScreen
import com.example.wanderpedia.features.auth.ui.signin.SignInScreen
import com.example.wanderpedia.features.auth.ui.signup.SignUpScreen
import com.example.wanderpedia.main.AppDestinations
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
            onComplete = onCompleteAuth
        )

        signUpRute(
            onNavigateBack = {
                navController.popBackStack()
            },
            onComplete = {
                navController.navigate(AppDestinations.Home)
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
        SignInScreen(
            onNavigateToSignUp = onNavigateToSignUp,
            onNavigateToRestPassword = onNavigateToRestPassword,
            onNavigateBack = onNavigateBack,
            onComplete = onComplete
        )
    }
}

fun NavGraphBuilder.signUpRute(
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit
) {
    composable<AuthDestinations.SignUp> {
        SignUpScreen(
            onNavigateBack = onNavigateBack,
            onComplete = onComplete
        )
    }
}

fun NavGraphBuilder.restPasswordRute(
    onNavigateBack: () -> Unit,
) {
    composable<AuthDestinations.RestPassword> {
        RestPasswordScreen(
            onNavigateBack = onNavigateBack,
        )
    }
}

