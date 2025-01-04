package com.example.wanderpedia.features.auth.ui.signup

import android.util.Patterns
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.ui.Reducer
import com.example.wanderpedia.core.ui.Reducer.ViewEffect
import com.example.wanderpedia.core.ui.Reducer.ViewEvent
import com.example.wanderpedia.core.ui.Reducer.ViewState

data class SignUpState(
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

sealed class SignUpEvent : ViewEvent {
    data class UpdateShowSuccessDialog(val showDialog: Boolean) : SignUpEvent()
    data class UpdateLoading(val loading: Boolean) : SignUpEvent()
    data class UpdateEmail(val email: String) : SignUpEvent()
    data class UpdatePassword(val password: String) : SignUpEvent()
    data class UpdatePasswordAdvisably(val isVisible: Boolean) : SignUpEvent()
    data class SignInWithEmail(val resource: Resource<Unit>) : SignUpEvent()
    data class SignInWithGoogle(val resource: Resource<Unit>) : SignUpEvent()
}

sealed class SignUpEffect : ViewEffect {
    object SuccessToSignUp : SignUpEffect()
    object NavigateBack : SignUpEffect()
    class ShowErrorToast(val message: String) : SignUpEffect()
}

class SignUpReducer : Reducer<SignUpState, SignUpEvent, SignUpEffect> {
    override fun reduce(
        previousState: SignUpState,
        event: SignUpEvent
    ): Pair<SignUpState, SignUpEffect?> {
        return when (event) {
            is SignUpEvent.UpdateShowSuccessDialog -> previousState.copy(showDialog = event.showDialog) to null
            is SignUpEvent.UpdateEmail -> previousState.copy(email = event.email) to null
            is SignUpEvent.UpdateLoading -> previousState.copy(loading = event.loading) to null
            is SignUpEvent.UpdatePassword -> previousState.copy(password = event.password) to null
            is SignUpEvent.UpdatePasswordAdvisably -> previousState.copy(isPasswordVisible = event.isVisible) to null
            is SignUpEvent.SignInWithEmail -> {
                val emailSupportingText = getEmailSupportingText(previousState.email)
                if (emailSupportingText.isNotEmpty())
                    return previousState.copy(emailSupportingText = emailSupportingText) to null

                val passwordSupportingText = getPasswordSupportingText(previousState.password)
                if (passwordSupportingText.isNotEmpty())
                    return previousState.copy(
                        emailSupportingText = emailSupportingText,
                        passwordSupportingText = passwordSupportingText
                    ) to null


                val effect = when (event.resource) {
                    is Resource.Success -> {
                        previousState.copy(showDialog = true)
                        null
                    }

                    is Resource.Error -> SignUpEffect.ShowErrorToast(event.resource.exception?.localizedMessage.orEmpty())
                    Resource.Loading -> null
                }
                return previousState to effect
            }

            is SignUpEvent.SignInWithGoogle -> {
                val effect = when (event.resource) {
                    is Resource.Success -> {
                        previousState.copy(showDialog = true)
                        null
                    }

                    is Resource.Error -> SignUpEffect.ShowErrorToast(event.resource.exception?.localizedMessage.orEmpty())
                    Resource.Loading -> null
                }
                return previousState to effect
            }
        }
    }
}

private fun getPasswordSupportingText(password: String): String {
    return password.let {
        if (it.length < 7) {
            "Password must be at least 8 characters"
        } else if (it.none { it.isUpperCase() }) {
            "Password must contain at least one uppercase letter"
        } else if (it.none { it.isDigit() }) {
            "Password must contain at least one digit"
        } else
            ""
    }
}

private fun getEmailSupportingText(email: String): String {
    return if (email.isEmpty()) {
        "Email is required"
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        "Email is invalid"
    } else {
        ""
    }
}