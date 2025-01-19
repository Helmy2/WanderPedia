package com.example.wanderpedia.features.detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.features.detail.domain.usecase.GetWonderByIdUseCase
import com.example.wanderpedia.features.detail.domain.usecase.UpdateWonderFavoriteUseCase
import com.example.wanderpedia.main.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getWonderByIdUseCase: GetWonderByIdUseCase,
    private val updateWonderFavoriteUseCase: UpdateWonderFavoriteUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.onStart {
        val wonderId: String = savedStateHandle.toRoute<AppDestinations.Detail>().id
        loadWonder(wonderId)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DetailState()
    )

    fun handleEvents(event: DetailEvent) {
        when (event) {
            DetailEvent.ToggleFavorite -> toggleFavorite()

            DetailEvent.NavigateBack -> {
                _state.value = _state.value.copy(navigateBack = true)
            }

            DetailEvent.OnHandelError -> {
                _state.value = _state.value.copy(error = null)
            }

            DetailEvent.OnHandelNavigationToDetail -> {
                _state.value = _state.value.copy(navigateBack = false)
            }
        }
    }

    private fun loadWonder(wonderId: String) {
        viewModelScope.launch(ioDispatcher) {
            _state.value = _state.value.copy(loading = true)
            getWonderByIdUseCase(wonderId).apply {
                fold(
                    onSuccess = {
                        _state.value = _state.value.copy(wonder = it, isFavorite = it.isFavorite)
                    },
                    onFailure = {
                        _state.value = _state.value.copy(error = it.localizedMessage.orEmpty())
                    },
                )
            }
            _state.value = _state.value.copy(loading = false)
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch(ioDispatcher) {
            _state.value = _state.value.copy(loading = true)
            updateWonderFavoriteUseCase(
                id = state.value.wonder?.id.orEmpty(),
                isFavorite = state.value.isFavorite.not()
            ).apply {
                fold(
                    onSuccess = {
                        _state.value = _state.value.copy(isFavorite = state.value.isFavorite.not())
                    },
                    onFailure = {
                        _state.value = _state.value.copy(error = it.localizedMessage.orEmpty())
                    },
                )
            }
            _state.value = _state.value.copy(loading = false)
        }
    }
}