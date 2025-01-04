package com.example.wanderpedia.features.auth.ui.signin

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.auth.domain.usecase.GetGoogleCredentialUseCase
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
    @IoDispatcher private val mainDispatcher: CoroutineDispatcher
) : BaseViewModel<SignInState, SignInEvent, SignInEffect>(
    initialState = SignInState(),
    reducer = SignInReducer(),
) {

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch(mainDispatcher) {
            sendEvent(SignInEvent.UpdateLoading(true))

            val credential = credentialUseCase(context)
            val result = when (credential) {
                is Resource.Error -> Resource.Error(credential.error)
                is Resource.Success -> signInWithGoogleUseCase(credential.data)
            }
            sendEventForEffect(SignInEvent.SignInWithGoogle(result))

            sendEvent(SignInEvent.UpdateLoading(false))
        }
    }

    fun signInWithEmail() {
        viewModelScope.launch(ioDispatcher) {
            sendEvent(SignInEvent.UpdateLoading(true))
            val result = signInWithEmailUseCase(state.value.email, state.value.password)
            sendEventForEffect(SignInEvent.SignInWithEmail(result))
            sendEvent(SignInEvent.UpdateLoading(false))
        }
    }

    fun onRestPasswordClick() {
        sendEffect(SignInEffect.NavigateToForgotPassword)
    }

    fun onSignUpClick() {
        sendEffect(SignInEffect.NavigateToSignUp)
    }
}