package com.example.wanderpedia.features.auth.ui.signin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
        state = state,
        onEvent = viewModel::setEvent,
    )
}
