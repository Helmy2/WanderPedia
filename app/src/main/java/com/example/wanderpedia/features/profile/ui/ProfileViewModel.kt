package com.example.wanderpedia.features.profile.ui

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Resource
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
            val result = updateDisplayNameUseCase(string)
            when (result) {
                is Resource.Error -> setEffect { ProfileContract.Effect.ShowErrorToast(result.exception?.localizedMessage.orEmpty()) }
                is Resource.Success -> setEffect { ProfileContract.Effect.ShowSuccessToast("Display name updated successfully") }
            }
            setState { copy(loading = false) }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch(ioDispatcher) {
            getCurrentUserFlowUseCase().collect { resource ->
                when (resource) {
                    is Resource.Success -> setState { copy(user = resource.data) }

                    is Resource.Error -> setEffect {
                        ProfileContract.Effect.ShowErrorToast(
                            resource.error.localizedMessage ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            val result = logoutUseCase()
            when (result) {
                is Resource.Error -> setEffect { ProfileContract.Effect.ShowErrorToast(result.exception?.localizedMessage.orEmpty()) }
                is Resource.Success -> setEffect { ProfileContract.Effect.ShowSuccessToast("Logout successfully") }
            }
            setState { copy(loading = false) }
        }
    }

    private fun linkToGoogleAccount(context: Context) {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }

            val credential = credentialUseCase(context)
            val result = when (credential) {
                is Resource.Error -> Resource.Error(credential.error)
                is Resource.Success -> linkAccountWithGoogleUseCase(credential.data)
            }
            when (result) {
                is Resource.Error -> setEffect { ProfileContract.Effect.ShowErrorToast(result.exception?.localizedMessage.orEmpty()) }
                is Resource.Success -> setEffect { ProfileContract.Effect.ShowSuccessToast("Account linked successfully") }
            }

            setState { copy(loading = false) }
        }
    }
}