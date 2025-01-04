package com.example.wanderpedia.features.auth.ui.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.R
import com.example.wanderpedia.core.ui.component.BackButton
import com.example.wanderpedia.core.ui.component.DefaultButton
import com.example.wanderpedia.core.ui.component.DefaultDialog
import com.example.wanderpedia.core.ui.component.DefaultTextField
import com.example.wanderpedia.core.ui.theme.WanderPediaTheme


@Composable
fun SignUpContent(
    email: String,
    password: String,
    loading: Boolean,
    showDialog: Boolean,
    isPasswordVisible: Boolean,
    isValuedEmail: Boolean,
    isValuedPassword: Boolean,
    passwordSupportingText: String,
    emailSupportingText: String,
    modifier: Modifier = Modifier,
    onDismissDialog: () -> Unit,
    onBackClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordHiddenClick: (Boolean) -> Unit,
    onSignWithEmailInClick: () -> Unit,
    onSignWithGoogle: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center,
    ) {
        DialogField(
            showDialog = showDialog,
            onDismissRequest = onDismissDialog,
        )

        BackButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(32.dp)
        )

        Column(
            modifier
                .fillMaxHeight()
                .fillMaxWidth(.8f),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TitleField(modifier = Modifier.padding(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            EmailField(
                email = email,
                onValueChange = onEmailChange,
                isValuedEmail = isValuedEmail,
                supportingText = { Text(text = emailSupportingText) }
            )
            PasswordField(
                password = password,
                onValueChange = onPasswordChange,
                isHidden = !isPasswordVisible,
                onIsHiddenChange = onPasswordHiddenClick,
                isValuedPassword = isValuedPassword,
                supportingText = { Text(text = passwordSupportingText) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            SignButtonField(
                loading = loading,
                onSignWithEmailInClick = onSignWithEmailInClick,
                onSignWithGoogle = onSignWithGoogle
            )

            SignInField(onClick = onBackClick)
        }
    }
}

@Composable
private fun SignButtonField(
    modifier: Modifier = Modifier,
    loading: Boolean,
    onSignWithEmailInClick: () -> Unit,
    onSignWithGoogle: () -> Unit
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(loading) {
            CircularProgressIndicator(
                modifier = Modifier,
            )
        }
        Column {
            DefaultButton(
                enabled = !loading,
                onClick = onSignWithEmailInClick,
            ) {
                Text(text = "Sign Up")
            }
            Spacer(modifier = Modifier.height(8.dp))
            DefaultButton(
                enabled = !loading,
                onClick = onSignWithGoogle
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "Google logo"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign up with Google",
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpContentPrev() {
    WanderPediaTheme {
        SignUpContent(
            email = "",
            password = "",
            loading = false,
            showDialog = false,
            isPasswordVisible = false,
            isValuedPassword = false,
            isValuedEmail = false,
            passwordSupportingText = "",
            emailSupportingText = "",
            onDismissDialog = {},
            onBackClick = {},
            onEmailChange = {},
            onPasswordChange = {},
            onPasswordHiddenClick = {},
            onSignWithEmailInClick = {},
            onSignWithGoogle = {}
        )
    }
}


@Composable
private fun PasswordField(
    password: String,
    isHidden: Boolean,
    isValuedPassword: Boolean,
    onValueChange: (String) -> Unit,
    onIsHiddenChange: (Boolean) -> Unit,
    supportingText: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier.Companion
) {
    OutlinedTextField(
        shape = RoundedCornerShape(8.dp),
        value = password,
        onValueChange = onValueChange,
        isError = !isValuedPassword,
        supportingText = if (isValuedPassword) null else supportingText,
        label = { Text("Password") },
        visualTransformation = if (isHidden) PasswordVisualTransformation() else VisualTransformation.Companion.None,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Companion.Done, keyboardType = KeyboardType.Companion.Password
        ),
        trailingIcon = {
            IconButton(onClick = { onIsHiddenChange(!isHidden) }) {
                Icon(
                    imageVector = if (isHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (isHidden) "Show password" else "Hide password"
                )
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun EmailField(
    email: String,
    onValueChange: (String) -> Unit,
    isValuedEmail: Boolean = true,
    supportingText: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    DefaultTextField(
        value = email,
        onValueChange = onValueChange,
        label = { Text("Email") },
        isError = !isValuedEmail,
        supportingText = if (isValuedEmail) null else supportingText,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
        ),
        modifier = modifier,
    )
}

@Composable
private fun DialogField(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showDialog) {
        DefaultDialog(
            onDismissRequest = onDismissRequest,
            content = {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Sign Up Successfully",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = "You have successfully signed up for the app",
                        style = MaterialTheme.typography.bodySmall,
                        minLines = 2
                    )
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                    TextButton(
                        onClick = onDismissRequest, modifier = Modifier.align(Alignment.End)
                    ) { Text("Confirm") }
                }
            },
            modifier = modifier
        )
    }
}

@Composable
private fun TitleField(
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Create Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please fill the form to continue", style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
private fun SignInField(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Row(modifier.padding(8.dp)) {
        Text(
            text = "have an Account?", style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onClick() },
        ) {
            Text(
                text = "Sign In",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

