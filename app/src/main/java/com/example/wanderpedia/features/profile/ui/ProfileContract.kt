package com.example.wanderpedia.features.profile.ui

import android.content.Context
import com.example.wanderpedia.core.domain.model.User
import com.example.wanderpedia.core.ui.ViewEffect
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

class ProfileContract {
    data class State(
        val loading: Boolean = false,
        val user: User = User(),
    ) : ViewState

    sealed class Event : ViewEvent {
        data class LinkToGoogleAccount(val context: Context) : Event()
        data object Logout : Event()
        data object NavigateToLogin : Event()
    }

    sealed class Effect : ViewEffect {
        data class ShowErrorToast(val message: String) : Effect()
        data object NavigateToLogin : Effect()
    }
}