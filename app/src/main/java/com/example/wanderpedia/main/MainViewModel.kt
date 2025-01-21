package com.example.wanderpedia.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.domain.usecase.GetCurrentUserFlowUseCase
import com.example.wanderpedia.core.domain.usecase.RefreshAllWondersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase,
    val refreshAllWondersUseCase: RefreshAllWondersUseCase,
) : ViewModel() {
    private val _showOnboarding = MutableStateFlow<Boolean?>(null)
    val showOnboarding: StateFlow<Boolean?> = _showOnboarding.onStart {
        _showOnboarding.value = getCurrentUserFlowUseCase().map { it.isFailure }.first()
    }.stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = null
    )

    init {
        viewModelScope.launch {
            refreshAllWondersUseCase()
        }
    }
}
