package com.example.wanderpedia.features.auth.ui.resetpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.component.DefaultSnackbarHost


@Composable
fun RestPasswordScreen(
    viewModel: RestPasswordViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                RestPasswordContract.Effect.NavigateBack -> onNavigateBack()
                RestPasswordContract.Effect.NavigateNext -> onNavigateBack()
                is RestPasswordContract.Effect.ShowErrorToast -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            DefaultSnackbarHost(snackbarHostState)
        },
    ) { padding ->
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
            modifier = Modifier.padding(padding),
        )
    }
}



