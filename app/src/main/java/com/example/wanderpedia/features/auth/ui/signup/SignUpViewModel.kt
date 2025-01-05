package com.example.wanderpedia.features.auth.ui.signup

import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
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
) : BaseViewModel<SignUpState, SignUpEvent, SignUpEffect>(
    initialState = SignUpState(),
    reducer = SignUpReducer(),
) {

    fun navigateSuccess() {
        sendEffect(SignUpEffect.SuccessToSignUp)
    }

    fun navigateBack() {
        sendEffect(SignUpEffect.NavigateBack)
    }
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
            val result = signUpWithEmailUseCase(
                state.value.email,
                state.value.password
            )
            sendEventForEffect(SignUpEvent.SignInWithEmail(result))
            sendEvent(SignUpEvent.UpdateLoading(false))
        }
    }

}
