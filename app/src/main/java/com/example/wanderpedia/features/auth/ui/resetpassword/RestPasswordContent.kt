package com.example.wanderpedia.features.auth.ui.resetpassword

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.core.ui.component.BackButton
import com.example.wanderpedia.core.ui.component.DefaultButton
import com.example.wanderpedia.core.ui.component.DefaultDialog
import com.example.wanderpedia.core.ui.component.DefaultTextField
import com.example.wanderpedia.core.ui.theme.WanderPediaTheme

@Composable
fun RestPasswordContent(
    email: String,
    showDislodge: Boolean,
    isEmailValid: Boolean,
    loading: Boolean,
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onDismissRequest: () -> Unit,
    onEmailChange: (String) -> Unit,
    resetPasswordClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center,
    ) {
        if (showDislodge) {
            DefaultDialog(
                onDismissRequest = onDismissRequest,
                content = {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Check your email",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = "We have send password recovery instruction to your email",
                            style = MaterialTheme.typography.bodySmall,
                            minLines = 2
                        )
                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        TextButton(
                            onClick = {
                                onDismissRequest()
                                onConfirmClick()
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) { Text("Confirm") }
                    }
                },
            )
        }

        BackButton(
            onClick = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(32.dp),
        )
        Column(
            modifier = Modifier.fillMaxWidth(.8f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column {
                Text(
                    text = "Forgot password",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Enter your email account to reset your password",
                    style = MaterialTheme.typography.bodySmall,
                    minLines = 2
                )
            }
            Spacer(Modifier.height(8.dp))
            EmailField(
                email = email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            SignButtonField(
                loading = loading,
                enabled = isEmailValid,
                resetPasswordClick = resetPasswordClick,
            )
        }
    }
}

@Composable
private fun SignButtonField(
    modifier: Modifier = Modifier,
    loading: Boolean,
    enabled: Boolean,
    resetPasswordClick: () -> Unit,
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(loading) {
            CircularProgressIndicator()
        }

        DefaultButton(
            onClick = resetPasswordClick,
            enabled = !loading && enabled,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Reset Password")
        }
    }
}

@Preview
@Composable
private fun ForgetPasswordContentPrev() {
    WanderPediaTheme {
        RestPasswordContent(
            email = "",
            showDislodge = false,
            onConfirmClick = {},
            onNavigateBack = {},
            onDismissRequest = {},
            onEmailChange = {},
            isEmailValid = false,
            loading = false,
            resetPasswordClick = {},
        )
    }
}

@Composable
private fun EmailField(
    email: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    DefaultTextField(
        value = email,
        onValueChange = onValueChange,
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Email,
        ),
        modifier = modifier,
    )
}