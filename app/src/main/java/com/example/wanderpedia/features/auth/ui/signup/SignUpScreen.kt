package com.example.wanderpedia.features.auth.ui.signup

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
fun SignUpScreen(
    viewModel: SignUpViewModel,
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
                SignUpEffect.NavigateBack -> onNavigateBack()
                SignUpEffect.SuccessToSignUp -> onComplete()
                is SignUpEffect.ShowErrorToast -> {
                    scope.launch {
                        snackbarHostState.currentSnackbarData?.dismiss()
                        snackbarHostState.showSnackbar(
                            message = it.message,
                            duration = SnackbarDuration.Short
                        )
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
        SignUpContent(
            email = state.email,
            password = state.password,
            loading = state.loading,
            showDialog = state.showDialog,
            isPasswordVisible = state.isPasswordVisible,
            isValuedPassword = state.passwordSupportingText.isEmpty(),
            isValuedEmail = state.emailSupportingText.isEmpty(),
            passwordSupportingText = state.passwordSupportingText,
            emailSupportingText = state.emailSupportingText,
            modifier = Modifier.padding(padding),
            onDismissDialog = { viewModel.updateDialogValue(false) },
            onBackClick = onNavigateBack,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onPasswordHiddenClick = viewModel::updatePasswordVisibility,
            onSignWithEmailInClick = viewModel::signUpWithEmail,
            onSignWithGoogle = { viewModel.signUpWithGoogle(context) }
        )
    }
}
