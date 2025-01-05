package com.example.wanderpedia.features.auth.ui.signin

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
import androidx.compose.material3.Text
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
import com.example.wanderpedia.core.ui.component.DefaultTextField
import com.example.wanderpedia.core.ui.theme.WanderPediaTheme


@Composable
fun SignInContent(
    email: String,
    password: String,
    loading: Boolean,
    isPasswordHidden: Boolean,
    isValuedSignInWithEmail: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onForgetPasswordClick: () -> Unit,
    onSignWithEmailInClick: () -> Unit,
    onSignWithGoogle: () -> Unit,
    onSignUpClick: () -> Unit,
    onNavigateBack: () -> Unit,
    onPasswordHiddenClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center,
    ) {
        BackButton(
            onClick = onNavigateBack, modifier = Modifier
                .padding(32.dp)
                .align(Alignment.TopStart)
        )
        Column(
            Modifier
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
                modifier = Modifier.fillMaxWidth(),
            )
            PasswordField(
                password = password,
                onValueChange = onPasswordChange,
                isHidden = isPasswordHidden,
                onIsHiddenChange = onPasswordHiddenClick,
                modifier = Modifier.fillMaxWidth()
            )
            ForgetPasswordField(
                modifier = Modifier.align(Alignment.End),
                onClick = onForgetPasswordClick,
            )
            Spacer(modifier = Modifier.height(16.dp))
            SignButtonField(
                loading = loading,
                isValuedSignInWithEmail = isValuedSignInWithEmail,
                onSignWithEmailInClick = onSignWithEmailInClick,
                onSignWithGoogle = onSignWithGoogle
            )
            SignUpField(onClick = onSignUpClick)
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun SignInContentPrev() {
    WanderPediaTheme {
        SignInContent(
            email = "",
            password = "",
            loading = false,
            onEmailChange = {},
            onPasswordChange = {},
            onForgetPasswordClick = {},
            onSignWithEmailInClick = {},
            onSignWithGoogle = {},
            onSignUpClick = {},
            onNavigateBack = {},
            isPasswordHidden = false,
            isValuedSignInWithEmail = false,
            onPasswordHiddenClick = {},
        )
    }
}


@Composable
private fun PasswordField(
    password: String,
    isHidden: Boolean,
    isValuedPassword: Boolean = true,
    onValueChange: (String) -> Unit,
    onIsHiddenChange: (Boolean) -> Unit,
    supportingText: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier.Companion
) {
    DefaultTextField(
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
    modifier: Modifier = Modifier,
) {
    DefaultTextField(
        value = email,
        onValueChange = onValueChange,
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email,
        ),
        modifier = modifier,
    )
}

@Composable
private fun SignButtonField(
    modifier: Modifier = Modifier,
    loading: Boolean,
    isValuedSignInWithEmail: Boolean,
    onSignWithEmailInClick: () -> Unit,
    onSignWithGoogle: () -> Unit
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(loading) {
            CircularProgressIndicator()
        }
        Column {
            DefaultButton(
                enabled = !loading && isValuedSignInWithEmail,
                onClick = onSignWithEmailInClick,
            ) {
                Text(text = "Sign In")
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
                    text = "Sign in with Google",
                )
            }
        }
    }
}

@Composable
private fun TitleField(
    modifier: Modifier = Modifier
) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Welcome Back!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Please sign in to your account", style = MaterialTheme.typography.bodySmall
        )
    }
}


@Composable
private fun ForgetPasswordField(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onClick() },
    ) {
        Text(
            text = "Forget Password?",
            modifier = Modifier.padding(4.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun SignUpField(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    Row(modifier.padding(8.dp)) {
        Text(
            text = "Don't have an Account?", style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onClick() },
        ) {
            Text(
                text = "Sign Up",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

