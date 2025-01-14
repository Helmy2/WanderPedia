package com.example.wanderpedia.features.auth.ui.signup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.ObserveEffect
import com.example.wanderpedia.core.ui.SnackbarController
import com.example.wanderpedia.core.ui.SnackbarEvent

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.effect, viewModel) {
        when (it) {
            is SignUpContract.Effect.NavigateBack -> onNavigateBack()
            is SignUpContract.Effect.NavigateNext -> onComplete()
            is SignUpContract.Effect.ShowErrorToast -> {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = it.message
                    )
                )
            }
        }
    }

    SignUpContent(
        email = state.email,
        password = state.password,
        loading = state.loading,
        showDialog = state.showDialog,
        isPasswordVisible = state.isPasswordVisible,
        isValuedEmail = state.emailSupportingText.isEmpty(),
        isValuedPassword = state.passwordSupportingText.isEmpty(),
        passwordSupportingText = state.passwordSupportingText,
        emailSupportingText = state.emailSupportingText,
        onBackClick = { viewModel.handleEvents(SignUpContract.Event.NavigateBack) },
        onEmailChange = { viewModel.handleEvents(SignUpContract.Event.UpdateEmail(it)) },
        onPasswordChange = { viewModel.handleEvents(SignUpContract.Event.UpdatePassword(it)) },
        onSignWithEmailInClick = { viewModel.handleEvents(SignUpContract.Event.SignInWithEmail) },
        onConfirmClick = { viewModel.handleEvents(SignUpContract.Event.NavigateNext) },
        onDismissDialog = { viewModel.handleEvents(SignUpContract.Event.NavigateBack) },
        onPasswordHiddenClick = {
            viewModel.handleEvents(
                SignUpContract.Event.UpdatePasswordVisibility(
                    it
                )
            )
        },
    )
}
