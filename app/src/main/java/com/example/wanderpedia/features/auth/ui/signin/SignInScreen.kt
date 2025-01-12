package com.example.wanderpedia.features.auth.ui.signin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.component.DefaultSnackbarHost
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToRestPassword: () -> Unit,
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                SignInContract.Effect.NavigateToForgotPassword -> onNavigateToRestPassword()
                SignInContract.Effect.NavigateToSignUp -> onNavigateToSignUp()
                SignInContract.Effect.NavigateNext -> onComplete()
                SignInContract.Effect.NavigateBack -> onNavigateBack()
                is SignInContract.Effect.ShowErrorToast -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }


    Scaffold(snackbarHost = {
        DefaultSnackbarHost(snackbarHostState)
    }) { padding ->
        SignInContent(
            email = state.email,
            password = state.password,
            loading = state.loading,
            isPasswordHidden = !state.isPasswordVisible,
            isValuedSignInWithEmail = state.isValuedSignInWithEmail,
            onEmailChange = { viewModel.handleEvents(SignInContract.Event.UpdateEmail(it)) },
            onPasswordChange = { viewModel.handleEvents(SignInContract.Event.UpdatePassword(it)) },
            onForgetPasswordClick = { viewModel.handleEvents(SignInContract.Event.NavigateToForgotPassword) },
            onSignWithEmailInClick = { viewModel.handleEvents(SignInContract.Event.SignInWithEmail) },
            onSignUpClick = { viewModel.handleEvents(SignInContract.Event.NavigateToSignUp) },
            onNavigateBack = { viewModel.handleEvents(SignInContract.Event.NavigateBack) },
            onSignWithGoogle = {
                viewModel.handleEvents(
                    SignInContract.Event.SignInWithGoogle(
                        context
                    )
                )
            },
            onPasswordHiddenClick = {
                viewModel.handleEvents(
                    SignInContract.Event.UpdatePasswordVisibility(
                        it
                    )
                )
            },
            modifier = Modifier.padding(padding)
        )
    }
}
