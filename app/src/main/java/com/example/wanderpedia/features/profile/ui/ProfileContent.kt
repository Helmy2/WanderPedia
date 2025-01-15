package com.example.wanderpedia.features.profile.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.R
import com.example.wanderpedia.core.ui.component.DefaultAsyncImage
import com.example.wanderpedia.core.ui.component.DefaultButton
import com.example.wanderpedia.core.ui.component.DefaultDialog
import com.example.wanderpedia.core.ui.component.DefaultTextField

@Composable
fun ProfileContent(
    state: ProfileContract.State, handleEvents: (ProfileContract.Event) -> Unit
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize(),
        ) {
            AnimatedContent(state.user.isAnonymous) {
                if (it) {
                    GuestContent(
                        onLoginClick = { handleEvents(ProfileContract.Event.NavigateToLogin) },
                        onLinkAccountClick = {
                            handleEvents(
                                ProfileContract.Event.LinkToGoogleAccount(context)
                            )
                        },
                        loading = state.loading
                    )
                } else {
                    UserContent(
                        name = state.user.displayName,
                        email = state.user.email,
                        imageUrl = state.user.imageUrl,
                        loading = state.loading,
                        onEditClick = { handleEvents(ProfileContract.Event.UpdateDialogState(true)) },
                        onLogoutClick = { handleEvents(ProfileContract.Event.Logout) }
                    )
                }
            }
        }
        EditNameDialog(
            name = state.user.displayName,
            show = state.showEditeDialog,
            onDismiss = { handleEvents(ProfileContract.Event.UpdateDialogState(false)) },
            onConfirm = { name ->
                handleEvents(ProfileContract.Event.UpdateDialogState(false))
                handleEvents(ProfileContract.Event.UpdateUserName(name))
            },
            modifier = Modifier.align(Alignment.Center).padding(16.dp)
        )
    }
}

@Composable
fun EditNameDialog(
    name: String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val (name, setName) = remember { mutableStateOf(name) }
    AnimatedVisibility(show) {
        DefaultDialog(
            onDismissRequest = onDismiss,
            content = {
                Column(
                    modifier = modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DefaultTextField(
                        value = name,
                        onValueChange = setName,
                        label = { Text("Name") }
                    )
                    DefaultButton(
                        onClick = { onConfirm(name) }, enabled = name.isNotBlank()
                    ) {
                        Text("Confirm")
                    }
                }
            },
            modifier = modifier
        )
    }
}

@Composable
fun GuestContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onLinkAccountClick: () -> Unit,
    loading: Boolean,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "You aren't logged in",
            style = MaterialTheme.typography.headlineSmall
        )
        DefaultButton(
            onClick = onLoginClick, enabled = !loading
        ) {
            Text("LogIn")
        }
        DefaultButton(
            onClick = onLinkAccountClick, enabled = !loading
        ) {
            Text("Link With Google")
        }
        AnimatedVisibility(
            loading, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun UserContent(
    name: String,
    email: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp,
    onEditClick: () -> Unit,
    onLogoutClick: () -> Unit,
    loading: Boolean,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
            ) {
                DefaultAsyncImage(
                    contentDescription = "profile", imageUrl = imageUrl, error = {
                        Image(
                            painterResource(R.drawable.profile), "profile", Modifier.fillMaxSize()
                        )
                    }, modifier = Modifier.fillMaxSize()
                )
            }
            Column {
                Text(
                    if (name.isEmpty()) "Anonymous" else name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    email,
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        DefaultButton(onClick = onEditClick, enabled = !loading) {
            Text(text = "Edit Profile")
        }

        DefaultButton(onClick = onLogoutClick, enabled = !loading) {
            Text(text = "Logout")
        }
        AnimatedVisibility(
            loading, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            CircularProgressIndicator()
        }
    }
}