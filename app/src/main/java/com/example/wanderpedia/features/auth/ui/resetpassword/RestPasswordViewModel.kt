package com.example.wanderpedia.features.auth.ui.resetpassword

import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.auth.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<RestPasswordState, RestPasswordEvent, RestPasswordEffect>(
    initialState = RestPasswordState(),
    reducer = RestPasswordReducer()
) {

    fun updateDialog(isVisible: Boolean) {
        sendEvent(RestPasswordEvent.UpdateShowDialog(isVisible))
    }

    fun updateEmail(email: String) {
        sendEvent(RestPasswordEvent.UpdateEmail(email))
    }

    fun resetPassword() {
        viewModelScope.launch(ioDispatcher) {
            sendEvent(RestPasswordEvent.UpdateLoading(true))
            val result = resetPasswordUseCase(state.value.email)
            sendEventForEffect(RestPasswordEvent.ResetPassword(result))
            sendEvent(RestPasswordEvent.UpdateLoading(false))
        }
    }
}