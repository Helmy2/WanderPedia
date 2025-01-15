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
) : BaseViewModel<RestPasswordContract.State, RestPasswordContract.Event, RestPasswordContract.Effect>(
    initialState = RestPasswordContract.State(),
) {
    override fun handleEvents(event: RestPasswordContract.Event) {
        when (event) {
            is RestPasswordContract.Event.UpdateEmail -> setState { copy(email = event.email) }
            is RestPasswordContract.Event.DismissDialog -> setState { copy(showDialog = false) }
            is RestPasswordContract.Event.UpdateLoading -> setState { copy(loading = event.loading) }
            is RestPasswordContract.Event.NavigateBack -> setEffect { RestPasswordContract.Effect.NavigateBack }
            is RestPasswordContract.Event.NavigateNext -> setEffect { RestPasswordContract.Effect.NavigateNext }
            is RestPasswordContract.Event.ResetPassword -> resetPassword()
        }
    }

    private fun resetPassword() {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            resetPasswordUseCase(state.value.email).apply {
                fold(onSuccess = { setState { copy(showDialog = true) } },
                    onFailure = { setEffect { RestPasswordContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) } })
            }
            setState { copy(loading = false) }
        }
    }
}