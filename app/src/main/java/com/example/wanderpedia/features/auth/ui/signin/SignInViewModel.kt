package com.example.wanderpedia.features.auth.ui.signin

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.R
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.auth.domain.usecase.SignInWithEmailUseCase
import com.example.wanderpedia.features.auth.domain.usecase.SignInWithGoogleUseCase
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @IoDispatcher private val mainDispatcher: CoroutineDispatcher
) : BaseViewModel<SignInState, SignInEvent, SignInEffect>(
    initialState = SignInState(),
    reducer = SignInReducer(),
) {

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch(mainDispatcher) {
            sendEvent(SignInEvent.UpdateLoading(true))
            val googleIdTokenCredential = createGoogleCredential(context)
            val result = signInWithGoogleUseCase(googleIdTokenCredential)
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

    private suspend fun createGoogleCredential(context: Context): GoogleIdTokenCredential {
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = context.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)

        return googleIdTokenCredential
    }
}