package com.example.wanderpedia.features.auth.ui.signin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToRestPassword: () -> Unit,
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                SignInEffect.NavigateToForgotPassword -> {
                    onNavigateToRestPassword()
                }

                SignInEffect.NavigateToSignUp -> {
                    onNavigateToSignUp()
                }

                SignInEffect.SuccessSignIn -> {
                    onComplete()
                }

                is SignInEffect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(it.message)
                    }
                }
            }
        }
    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) { padding ->
        SignInContent(
            email = state.email,
            password = state.password,
            loading = state.loading,
            isPasswordHidden = state.isPasswordHidden,
            onEmailChange = { viewModel.sendEvent(SignInEvent.UpdateEmail(it)) },
            onPasswordChange = { viewModel.sendEvent(SignInEvent.UpdatePassword(it)) },
            isPasswordHiddenClick = { viewModel.sendEvent(SignInEvent.UpdatePasswordAdvisably(it)) },
            onForgetPasswordClick = viewModel::onRestPasswordClick,
            onSignWithEmailInClick = viewModel::signInWithEmail,
            onSignWithGoogle = { viewModel.signInWithGoogle(context) },
            onSignUpClick = viewModel::onSignUpClick,
            onNavigateBack = onNavigateBack,
            modifier = Modifier.padding(padding)
        )
    }
}
