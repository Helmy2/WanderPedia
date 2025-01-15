package com.example.wanderpedia.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.usecase.CreateAnonymousAccountUseCase
import com.example.wanderpedia.core.domain.usecase.GetCurrentUserFlowUseCase
import com.example.wanderpedia.core.domain.usecase.RefreshAllWondersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase,
    val createAnonymousAccountUseCase: CreateAnonymousAccountUseCase,
    val refreshAllWondersUseCase: RefreshAllWondersUseCase,
) : ViewModel() {
    val showOnboarding: StateFlow<Boolean?> = getCurrentUserFlowUseCase()
        .map {
            when (it) {
                is Resource.Error -> {
                    createAnonymousAccountUseCase()
                    true
                }

                is Resource.Success -> false
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        viewModelScope.launch {
            refreshAllWondersUseCase()
        }
    }
}
