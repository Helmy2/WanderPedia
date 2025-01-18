package com.example.wanderpedia.features.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.features.favorite.domain.usecase.GetFavoriteWondersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteWondersUseCase: GetFavoriteWondersUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(FavoriteState())
    val state = _state.onStart {
        viewModelScope.launch {
            getFavoriteWondersUseCase().collect {
                it.fold(
                    onSuccess = {
                        _state.value = _state.value.copy(wonders = it)
                    }, onFailure = {
                        _state.value = _state.value.copy(error = it.message)
                    }
                )
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoriteState())


    fun handleEvents(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.OnItemClick -> {
                _state.value = _state.value.copy(navigateDetail = event.wonder)
            }

            is FavoriteEvent.OnHandelNavigationToDetail -> {
                _state.value = _state.value.copy(navigateDetail = null)
            }

            is FavoriteEvent.OnHandelError -> {
                _state.value = _state.value.copy(error = null)
            }
        }
    }
}