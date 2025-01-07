package com.example.wanderpedia.features.auth.ui.signup

import com.example.wanderpedia.core.ui.ViewEffect
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

object SignUpContract {
    data class State(
        val email: String = "",
        val name: String = "",
        val password: String = "",
        val loading: Boolean = false,
        val showDialog: Boolean = false,
        val isPasswordVisible: Boolean = false,
        val nameSupportingText: String = "",
        val emailSupportingText: String = "",
        val passwordSupportingText: String = "",
    ) : ViewState

    sealed class Event : ViewEvent {
        data class UpdateLoading(val loading: Boolean) : Event()
        data class UpdateEmail(val email: String) : Event()
        data class UpdatePassword(val password: String) : Event()
        data class UpdatePasswordVisibility(val isVisible: Boolean) : Event()
        data object SignInWithEmail : Event()
        data object NavigateNext : Event()
        data object NavigateBack : Event()
    }

    sealed class Effect : ViewEffect {
        data object NavigateNext : Effect()
        data object NavigateBack : Effect()
        class ShowErrorToast(val message: String) : Effect()
    }
}