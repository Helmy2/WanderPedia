package com.example.wanderpedia.features.auth.ui.signup

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.auth.domain.usecase.SignUpWithEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<SignUpContract.State, SignUpContract.Event, SignUpContract.Effect>(
    initialState = SignUpContract.State(),
) {
    override fun handleEvents(event: SignUpContract.Event) {
        when (event) {
            is SignUpContract.Event.UpdateEmail -> setState { copy(email = event.email) }
            is SignUpContract.Event.UpdateLoading -> setState { copy(loading = event.loading) }
            is SignUpContract.Event.UpdatePassword -> setState { copy(password = event.password) }
            is SignUpContract.Event.UpdatePasswordVisibility -> setState { copy(isPasswordVisible = event.isVisible) }
            SignUpContract.Event.SignInWithEmail -> signUpWithEmail()
            SignUpContract.Event.NavigateBack -> setEffect { SignUpContract.Effect.NavigateBack }
            SignUpContract.Event.NavigateNext -> setEffect { SignUpContract.Effect.NavigateNext }
        }
    }

    fun signUpWithEmail() {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }

            val emailSupportingText = getEmailSupportingText(state.value.email)
            if (emailSupportingText.isNotEmpty()) {
                setState { copy(emailSupportingText = emailSupportingText) }
                return@launch
            }
            val passwordSupportingText = getPasswordSupportingText(state.value.password)
            if (passwordSupportingText.isNotEmpty()) {
                setState { copy(passwordSupportingText = passwordSupportingText) }
                return@launch
            }

            val result = signUpWithEmailUseCase(
                state.value.email,
                state.value.password
            )
            when (result) {
                is Resource.Error -> setEffect {
                    SignUpContract.Effect.ShowErrorToast(result.exception?.localizedMessage.orEmpty())
                }

                is Resource.Success -> setState {
                    copy(showDialog = true)
                }
            }
            setState { copy(loading = false) }
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
