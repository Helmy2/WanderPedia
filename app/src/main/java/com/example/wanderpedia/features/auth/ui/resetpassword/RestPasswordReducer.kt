package com.example.wanderpedia.features.auth.ui.resetpassword

import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.ui.Reducer
import com.example.wanderpedia.core.ui.Reducer.ViewEffect
import com.example.wanderpedia.core.ui.Reducer.ViewEvent
import com.example.wanderpedia.core.ui.Reducer.ViewState


data class RestPasswordState(
    val email: String = "",
    val loading: Boolean = false,
    val showDislodge: Boolean = false
) : ViewState {
    val isEmailValid: Boolean
        get() = email.isNotBlank()
}


sealed class RestPasswordEvent : ViewEvent {
    data class UpdateShowDialog(val showDialog: Boolean) : RestPasswordEvent()
    data class UpdateLoading(val loading: Boolean) : RestPasswordEvent()
    data class UpdateEmail(val email: String) : RestPasswordEvent()
    data class ResetPassword(val resource: Resource<Unit>) : RestPasswordEvent()
}

sealed class RestPasswordEffect : ViewEffect {
    object SuccessToRestPassword : RestPasswordEffect()
    object NavigateBack : RestPasswordEffect()
    class ShowErrorToast(val message: String) : RestPasswordEffect()
}

class RestPasswordReducer : Reducer<RestPasswordState, RestPasswordEvent, RestPasswordEffect> {
    override fun reduce(
        previousState: RestPasswordState,
        event: RestPasswordEvent
    ): Pair<RestPasswordState, RestPasswordEffect?> {
        return when (event) {
            is RestPasswordEvent.UpdateShowDialog -> previousState.copy(showDislodge = event.showDialog) to null
            is RestPasswordEvent.UpdateEmail -> previousState.copy(email = event.email) to null
            is RestPasswordEvent.UpdateLoading -> previousState.copy(loading = event.loading) to null
            is RestPasswordEvent.ResetPassword -> {
                when (event.resource) {
                    is Resource.Success -> previousState.copy(showDislodge = true) to RestPasswordEffect.SuccessToRestPassword
                    is Resource.Error -> previousState to RestPasswordEffect.ShowErrorToast(event.resource.exception?.localizedMessage.orEmpty())
                }
            }
        }
    }
}

