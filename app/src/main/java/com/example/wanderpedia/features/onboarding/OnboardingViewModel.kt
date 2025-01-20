package com.example.wanderpedia.features.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.domain.usecase.CreateAnonymousAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val createAnonymousAccountUseCase: CreateAnonymousAccountUseCase,
) : ViewModel() {

    fun createAnonymousAccount() {
        viewModelScope.launch {
            createAnonymousAccountUseCase()
        }
    }
}