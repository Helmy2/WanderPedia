package com.example.wanderpedia.navigation

import androidx.lifecycle.ViewModel
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.usecase.CreateAnonymousAccountUseCase
import com.example.wanderpedia.core.domain.usecase.GetCurrentUserFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class WonderViewModel @Inject constructor(
    val getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase,
    val createAnonymousAccountUseCase: CreateAnonymousAccountUseCase
) : ViewModel() {

    suspend fun initialize(
        showOnboarding: (Boolean) -> Unit,
    ) {
        getCurrentUserFlowUseCase().first().also {
            when (it) {
                is Resource.Error -> {
                    createAnonymousAccountUseCase()
                    showOnboarding(true)
                }

                is Resource.Success -> showOnboarding(false)
            }
        }
    }
}
