package com.example.wanderpedia.features.auth.ui.signin

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.usecase.GetGoogleCredentialUseCase
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.auth.domain.usecase.SignInWithEmailUseCase
import com.example.wanderpedia.features.auth.domain.usecase.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val credentialUseCase: GetGoogleCredentialUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<SignInContract.State, SignInContract.Event, SignInContract.Effect>(
    initialState = SignInContract.State(),
) {
    override fun handleEvents(event: SignInContract.Event) {
        when (event) {
            is SignInContract.Event.UpdateEmail -> setState { copy(email = event.email) }
            is SignInContract.Event.UpdateLoading -> setState { copy(loading = event.loading) }
            is SignInContract.Event.UpdatePassword -> setState { copy(password = event.password) }
            is SignInContract.Event.UpdatePasswordVisibility -> setState { copy(isPasswordVisible = event.isVisible) }
            is SignInContract.Event.SignInWithGoogle -> signInWithGoogle(event.context)
            SignInContract.Event.NavigateBack -> setEffect { SignInContract.Effect.NavigateBack }
            SignInContract.Event.NavigateNext -> setEffect { SignInContract.Effect.NavigateNext }
            SignInContract.Event.NavigateToForgotPassword -> setEffect { SignInContract.Effect.NavigateToForgotPassword }
            SignInContract.Event.NavigateToSignUp -> setEffect { SignInContract.Effect.NavigateToSignUp }
            SignInContract.Event.SignInWithEmail -> signInWithEmail()
        }
    }

    private fun signInWithGoogle(context: Context) {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }

            val credential = credentialUseCase(context)
            val result = when (credential) {
                is Resource.Error -> Resource.Error(credential.error)
                is Resource.Success -> signInWithGoogleUseCase(credential.data)
            }
            when (result) {
                is Resource.Error -> setEffect { SignInContract.Effect.ShowErrorToast(result.exception?.localizedMessage.orEmpty()) }
                is Resource.Success -> setEffect { SignInContract.Effect.NavigateNext }
            }

            setState { copy(loading = false) }
        }
    }

    private fun signInWithEmail() {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            val result = signInWithEmailUseCase(state.value.email, state.value.password)
            when (result) {
                is Resource.Error -> setEffect { SignInContract.Effect.ShowErrorToast(result.exception?.localizedMessage.orEmpty()) }
                is Resource.Success -> setEffect { SignInContract.Effect.NavigateNext }
            }
            setState { copy(loading = false) }
        }
    }
}