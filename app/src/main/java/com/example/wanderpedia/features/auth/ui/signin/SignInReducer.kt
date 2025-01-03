package com.example.wanderpedia.features.auth.ui.signin

import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.ui.Reducer
import com.example.wanderpedia.core.ui.Reducer.ViewEffect
import com.example.wanderpedia.core.ui.Reducer.ViewEvent
import com.example.wanderpedia.core.ui.Reducer.ViewState


data class SignInState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val isPasswordHidden: Boolean = false,
) : ViewState

sealed class SignInEvent : ViewEvent {
    data class UpdateLoading(val loading: Boolean) : SignInEvent()
    data class UpdateEmail(val email: String) : SignInEvent()
    data class UpdatePassword(val password: String) : SignInEvent()
    data class UpdatePasswordAdvisably(val isPasswordHidden: Boolean) : SignInEvent()
    data class SignInWithEmail(val resource: Resource<Unit>) : SignInEvent()
    data class SignInWithGoogle(val resource: Resource<Unit>) : SignInEvent()
}


sealed class SignInEffect : ViewEffect {
    object NavigateToSignUp : SignInEffect()
    object NavigateToForgotPassword : SignInEffect()
    object SuccessSignIn : SignInEffect()
    class ShowErrorToast(val message: String) : SignInEffect()
}

class SignInReducer : Reducer<SignInState, SignInEvent, SignInEffect> {
    override fun reduce(
        previousState: SignInState,
        event: SignInEvent
    ): Pair<SignInState, SignInEffect?> {
        return when (event) {
            is SignInEvent.UpdateLoading -> previousState.copy(loading = event.loading) to null
            is SignInEvent.UpdateEmail -> previousState.copy(email = event.email) to null
            is SignInEvent.UpdatePassword -> previousState.copy(password = event.password) to null
            is SignInEvent.UpdatePasswordAdvisably -> previousState.copy(isPasswordHidden = event.isPasswordHidden) to null
            is SignInEvent.SignInWithEmail -> {
                val effect = when (event.resource) {
                    is Resource.Success -> SignInEffect.SuccessSignIn
                    is Resource.Error -> SignInEffect.ShowErrorToast(event.resource.exception?.localizedMessage.orEmpty())
                    Resource.Loading -> null
                }
                previousState to effect
            }

            is SignInEvent.SignInWithGoogle -> {
                val effect = when (event.resource) {
                    is Resource.Success -> SignInEffect.SuccessSignIn
                    is Resource.Error -> SignInEffect.ShowErrorToast(event.resource.exception?.localizedMessage.orEmpty())
                    Resource.Loading -> null
                }
                previousState to effect
            }
        }
    }
}