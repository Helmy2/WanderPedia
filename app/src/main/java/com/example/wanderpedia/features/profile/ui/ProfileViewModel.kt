package com.example.wanderpedia.features.profile.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.usecase.GetCurrentUserFlowUseCase
import com.example.wanderpedia.core.domain.usecase.GetGoogleCredentialUseCase
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.profile.domain.usecase.LinkAccountWithGoogleUseCase
import com.example.wanderpedia.features.profile.domain.usecase.LogoutUseCase
import com.example.wanderpedia.features.profile.domain.usecase.UpdateDisplayNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val linkAccountWithGoogleUseCase: LinkAccountWithGoogleUseCase,
    private val credentialUseCase: GetGoogleCredentialUseCase,
    private val updateDisplayNameUseCase: UpdateDisplayNameUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<ProfileContract.State, ProfileContract.Event, ProfileContract.Effect>(
    ProfileContract.State()
) {

    init {
        loadUserProfile()
    }

    override fun handleEvents(event: ProfileContract.Event) {
        when (event) {
            is ProfileContract.Event.Logout -> logout()
            is ProfileContract.Event.NavigateToLogin -> setEffect { ProfileContract.Effect.NavigateToLogin }
            is ProfileContract.Event.LinkToGoogleAccount -> linkToGoogleAccount(event.context)
            is ProfileContract.Event.UpdateDialogState -> setState { copy(showEditeDialog = event.show) }
            is ProfileContract.Event.UpdateUserName -> updateDisplayName(event.name)
        }
    }

    private fun updateDisplayName(string: String) {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            updateDisplayNameUseCase(string).apply {
                fold(
                    onSuccess = { setEffect { ProfileContract.Effect.ShowSuccessToast("Display name updated successfully") } },
                    onFailure = { setEffect { ProfileContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) } },
                )
            }
            setState { copy(loading = false) }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch(ioDispatcher) {
            getCurrentUserFlowUseCase().collect { it ->
                it.fold(
                    onSuccess = { setState { copy(user = it) } },
                    onFailure = { setEffect { ProfileContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) } },
                )
            }
        }
    }

    private fun logout() {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            logoutUseCase().apply {
                fold(
                    onSuccess = { setEffect { ProfileContract.Effect.ShowSuccessToast("Logout successfully") } },
                    onFailure = { setEffect { ProfileContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) } },
                )
            }
            setState { copy(loading = false) }
        }
    }

    private fun linkToGoogleAccount(context: Context) {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            credentialUseCase(context).apply {
                fold(
                    onFailure = { setEffect { ProfileContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) } },
                    onSuccess = {
                        linkAccountWithGoogleUseCase(it).apply {
                            fold(
                                onSuccess = { setEffect { ProfileContract.Effect.ShowSuccessToast("Account linked successfully") } },
                                onFailure = { setEffect { ProfileContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) } },
                            )
                        }
                    }
                )
            }
            setState { copy(loading = false) }
        }
    }
}