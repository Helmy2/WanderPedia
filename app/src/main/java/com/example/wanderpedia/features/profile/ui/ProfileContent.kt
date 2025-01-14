package com.example.wanderpedia.features.profile.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ProfileContent(
    state: ProfileContract.State, handleEvents: (ProfileContract.Event) -> Unit
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Display Name: ${state.user.displayName}")
        Text(text = "Email: ${state.user.email}")
        Text(text = "isAnonymous: ${state.user.isAnonymous}")
        Text(text = "image : ${state.user.imageUrl}")
        Text(text = "id: ${state.user.id}")
        AnimatedContent(state.user.isAnonymous) {
            if (it) {
                Column {
                    Button(
                        onClick = { handleEvents(ProfileContract.Event.NavigateToLogin) },
                        enabled = !state.loading
                    ) {
                        Text("LogIn")
                    }
                    Button(
                        onClick = {
                            handleEvents(ProfileContract.Event.LinkToGoogleAccount(context))
                        },
                        enabled = !state.loading,
                    ) {
                        Text("Link With Google")
                    }
                }
            } else {
                Button(
                    onClick = { handleEvents(ProfileContract.Event.Logout) },
                    enabled = !state.loading
                ) {
                    Text("Logout")
                }
            }
        }

    }

}