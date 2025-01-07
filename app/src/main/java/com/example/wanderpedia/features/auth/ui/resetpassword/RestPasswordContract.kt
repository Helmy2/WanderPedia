package com.example.wanderpedia.features.auth.ui.resetpassword

import com.example.wanderpedia.core.ui.ViewEffect
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState


class RestPasswordContract {
    data class State(
        val email: String = "",
        val loading: Boolean = false,
        val showDialog: Boolean = false
    ) : ViewState {
        val isEmailValid: Boolean
            get() = email.isNotBlank()
    }


    sealed class Event : ViewEvent {
        data class UpdateLoading(val loading: Boolean) : Event()
        data class UpdateEmail(val email: String) : Event()
        data object ResetPassword : Event()
        data object DismissDialog : Event()
        data object NavigateNext : Event()
        data object NavigateBack : Event()
    }

    sealed class Effect : ViewEffect {
        object NavigateNext : Effect()
        object NavigateBack : Effect()
        class ShowErrorToast(val message: String) : Effect()
    }
}

