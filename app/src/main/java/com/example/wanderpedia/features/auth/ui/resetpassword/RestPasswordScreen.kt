package com.example.wanderpedia.features.auth.ui.resetpassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.ObserveEffect
import com.example.wanderpedia.core.ui.SnackbarController
import com.example.wanderpedia.core.ui.SnackbarEvent


@Composable
fun RestPasswordScreen(
    viewModel: RestPasswordViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.effect, viewModel) {
        when (it) {
            RestPasswordContract.Effect.NavigateBack -> onNavigateBack()
            RestPasswordContract.Effect.NavigateNext -> onNavigateBack()
            is RestPasswordContract.Effect.ShowErrorToast -> {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = it.message
                    )
                )
            }
        }
    }

    RestPasswordContent(
        email = state.email,
        showDislodge = state.showDialog,
        loading = state.loading,
        isEmailValid = state.isEmailValid,
        onNavigateBack = { viewModel.handleEvents(RestPasswordContract.Event.NavigateBack) },
        onEmailChange = { viewModel.handleEvents(RestPasswordContract.Event.UpdateEmail(it)) },
        resetPasswordClick = { viewModel.handleEvents(RestPasswordContract.Event.ResetPassword) },
        onConfirmClick = { viewModel.handleEvents(RestPasswordContract.Event.NavigateNext) },
        onDismissRequest = { viewModel.handleEvents(RestPasswordContract.Event.DismissDialog) },
    )
}



