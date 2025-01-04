package com.example.wanderpedia.features.auth.ui.signup

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.auth.domain.usecase.GetGoogleCredentialUseCase
import com.example.wanderpedia.features.auth.domain.usecase.LinkAccountWithEmailUseCase
import com.example.wanderpedia.features.auth.domain.usecase.LinkAccountWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val linkAccountWithGoogleUseCase: LinkAccountWithGoogleUseCase,
    private val linkAccountWithEmailUseCase: LinkAccountWithEmailUseCase,
    private val getGoogleCredentialUseCase: GetGoogleCredentialUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @IoDispatcher private val mainDispatcher: CoroutineDispatcher
) : BaseViewModel<SignUpState, SignUpEvent, SignUpEffect>(
    initialState = SignUpState(),
    reducer = SignUpReducer(),
) {

    fun updateDialogValue(show: Boolean) {
        sendEvent(SignUpEvent.UpdateShowDialog(show))
    }


    fun updateEmail(email: String) {
        sendEvent(SignUpEvent.UpdateEmail(email))
    }

    fun updatePassword(password: String) {
        sendEvent(SignUpEvent.UpdatePassword(password))
    }

    fun updatePasswordVisibility(
        isVisible: Boolean
    ) {
        sendEvent(SignUpEvent.UpdatePasswordAdvisably(isVisible))
    }

    fun signUpWithEmail() {
        viewModelScope.launch(ioDispatcher) {
            sendEvent(SignUpEvent.UpdateLoading(true))
            val result = linkAccountWithEmailUseCase(
                state.value.email,
                state.value.password
            )
            sendEventForEffect(SignUpEvent.SignInWithEmail(result))
            sendEvent(SignUpEvent.UpdateLoading(false))
        }
    }


    fun signUpWithGoogle(context: Context) {
        viewModelScope.launch(mainDispatcher) {
            sendEvent(SignUpEvent.UpdateLoading(true))

            val credential = withContext(mainDispatcher) {
                getGoogleCredentialUseCase(context)
            }
            val result = when (credential) {
                is Resource.Error -> Resource.Error(credential.error)
                is Resource.Success -> linkAccountWithGoogleUseCase(credential.data)
            }
            sendEventForEffect(SignUpEvent.SignInWithGoogle(result))

            sendEvent(SignUpEvent.UpdateLoading(false))
        }
    }


}
