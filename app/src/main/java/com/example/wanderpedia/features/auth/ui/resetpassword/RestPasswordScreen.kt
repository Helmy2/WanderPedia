package com.example.wanderpedia.features.auth.ui.resetpassword

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.component.DefaultSnackbarHost
import kotlinx.coroutines.launch


@Composable
fun RestPasswordScreen(
    viewModel: RestPasswordViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                RestPasswordEffect.NavigateBack -> onNavigateBack()
                is RestPasswordEffect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = it.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                RestPasswordEffect.SuccessToRestPassword -> onNavigateBack()
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
            showDislodge = state.showDislodge,
            modifier = Modifier.padding(padding),
            loading = state.loading,
            onNavigateBack = onNavigateBack,
            onEmailChange = viewModel::updateEmail,
            resetPasswordClick = viewModel::resetPassword,
            isEmailValid = state.isEmailValid,
            onComplete = { viewModel.updateDialog(false) },
            onDismissRequest = { viewModel.updateDialog(false) },
        )
    }
}



