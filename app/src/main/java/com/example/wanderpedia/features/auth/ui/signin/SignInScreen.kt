package com.example.wanderpedia.features.auth.ui.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.ObserveEffect
import com.example.wanderpedia.core.ui.SnackbarController
import com.example.wanderpedia.core.ui.SnackbarEvent


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

    ObserveEffect(viewModel.effect, viewModel) {
        when (it) {
            SignInContract.Effect.NavigateToForgotPassword -> onNavigateToRestPassword()
            SignInContract.Effect.NavigateToSignUp -> onNavigateToSignUp()
            SignInContract.Effect.NavigateNext -> onComplete()
            SignInContract.Effect.NavigateBack -> onNavigateBack()
            is SignInContract.Effect.ShowErrorToast -> {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = it.message
                    )
                )
            }
        }
    }

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
            viewModel.handleEvents(SignInContract.Event.SignInWithGoogle(context))
        },
        onPasswordHiddenClick = {
            viewModel.handleEvents(SignInContract.Event.UpdatePasswordVisibility(it))
        },
    )
}
