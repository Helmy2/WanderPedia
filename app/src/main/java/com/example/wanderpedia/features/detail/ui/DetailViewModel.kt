package com.example.wanderpedia.features.detail.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.detail.domain.usecase.GetWonderByIdUseCase
import com.example.wanderpedia.features.detail.domain.usecase.UpdateWonderFavoriteUseCase
import com.example.wanderpedia.main.AppDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getWonderByIdUseCase: GetWonderByIdUseCase,
    private val updateWonderFavoriteUseCase: UpdateWonderFavoriteUseCase,
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
            DetailContract.Event.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun loadWonder(wonderId: String) {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            getWonderByIdUseCase(wonderId).apply {
                fold(
                    onSuccess = {
                        setState { copy(wonder = it, isFavorite = it.isFavorite) }
                    },
                    onFailure = {
                        setEffect { DetailContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) }
                    },
                )
            }
            setState { copy(loading = false) }
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            updateWonderFavoriteUseCase(
                id = state.value.wonder?.id.orEmpty(),
                isFavorite = state.value.isFavorite.not()
            ).apply {
                fold(
                    onSuccess = {
                        setState { copy(isFavorite = isFavorite.not()) }
                    },
                    onFailure = {
                        setEffect { DetailContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) }
                    },
                )
            }
            setState { copy(loading = false) }
        }
    }
}