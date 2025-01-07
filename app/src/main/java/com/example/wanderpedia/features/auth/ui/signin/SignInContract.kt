package com.example.wanderpedia.features.auth.ui.signin

import android.content.Context
import com.example.wanderpedia.core.ui.ViewEffect
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

object SignInContract {
    data class State(
        val email: String = "",
        val password: String = "",
        val loading: Boolean = false,
        val isPasswordVisible: Boolean = false,
    ) : ViewState {
        val isValuedSignInWithEmail: Boolean = email.isNotBlank() && password.isNotBlank()
    }

    sealed class Event : ViewEvent {
        data class UpdateLoading(val loading: Boolean) : Event()
        data class UpdateEmail(val email: String) : Event()
        data class UpdatePassword(val password: String) : Event()
        data class UpdatePasswordVisibility(val isVisible: Boolean) : Event()
        data class SignInWithGoogle(val context: Context) : Event()
        data object SignInWithEmail : Event()
        data object NavigateToSignUp : Event()
        data object NavigateToForgotPassword : Event()
        data object NavigateNext : Event()
        data object NavigateBack : Event()
    }


    sealed class Effect : ViewEffect {
        object NavigateToSignUp : Effect()
        object NavigateToForgotPassword : Effect()
        object NavigateNext : Effect()
        object NavigateBack : Effect()
        class ShowErrorToast(val message: String) : Effect()
    }
}
