package com.example.wanderpedia.features.detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.detail.domain.usecase.GetWonderByIdUseCase
import com.example.wanderpedia.navigation.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getWonderByIdUseCase: GetWonderByIdUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailContract.State, DetailContract.Event, DetailContract.Effect>(
    DetailContract.State()
) {

    init {
        val wonderId: String = savedStateHandle.toRoute<AppDestinations.Detail>().id
        loadWonder(wonderId)
    }

    override fun handleEvents(event: DetailContract.Event) {
        when (event) {
            is DetailContract.Event.NavigateBack -> setEffect { DetailContract.Effect.NavigateBack }
        }
    }

    private fun loadWonder(wonderId: String) {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            when (val result = getWonderByIdUseCase(wonderId)) {
                is Resource.Success -> setState { copy(wonder = result.data) }
                is Resource.Error -> {
                    setEffect { DetailContract.Effect.ShowErrorToast(result.exception?.localizedMessage.orEmpty()) }
                }
            }
            setState { copy(loading = false) }
        }
    }
}