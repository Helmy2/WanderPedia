package com.example.wanderpedia.features.auth.ui.signup

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

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                SignUpEffect.NavigateBack -> onNavigateBack()
                SignUpEffect.SuccessToSignUp -> onComplete()
                is SignUpEffect.ShowErrorToast -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = it.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            DefaultSnackbarHost(snackbarHostState)
        }
    ) { padding ->
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
            onBackClick = viewModel::navigateBack,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onPasswordHiddenClick = viewModel::updatePasswordVisibility,
            onSignWithEmailInClick = viewModel::signUpWithEmail,
            onConfirmClick = viewModel::navigateSuccess,
            onDismissDialog = { viewModel.updateDialogValue(false) },
            onSignWithGoogle = { viewModel.signUpWithGoogle(context) },
            modifier = Modifier.padding(padding),
        )
    }
}
