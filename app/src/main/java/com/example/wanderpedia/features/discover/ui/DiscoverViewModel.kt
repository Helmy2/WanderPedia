package com.example.wanderpedia.features.discover.ui

import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.core.domain.model.toCached
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.discover.domain.usecase.GetWondersByUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val getWondersByUseCase: GetWondersByUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<DiscoverContract.State, DiscoverContract.Event, DiscoverContract.Effect>(
    DiscoverContract.State()
) {

    init {
        loadWonders()
    }

    override fun handleEvents(event: DiscoverContract.Event) {
        when (event) {
            is DiscoverContract.Event.ApplyFilters -> applyFilters(event.filters)
            is DiscoverContract.Event.LoadWonders -> loadWonders()
            is DiscoverContract.Event.OnItemClick -> navigateToDetail(event.id)
        }
    }

    private fun loadWonders() {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            getWondersByUseCase().collectLatest {
                when (it) {
                    is Resource.Success -> setState { copy(wonders = it.data, loading = false) }
                    is Resource.Error -> {
                        setEffect {
                            DiscoverContract.Effect.ShowErrorToast(it.exception?.localizedMessage.orEmpty())
                        }
                    }
                }
            }
            setState { copy(loading = false) }
        }
    }

    private fun applyFilters(filters: DiscoverContract.Filters) {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true, filters = filters) }
            delay(1000)
            getWondersByUseCase(
                textQuery = filters.text,
                timePeriodQuery = filters.timePeriod.toCached(),
                categoryQuery = filters.category.toCached()
            ).collectLatest {
                when (it) {
                    is Resource.Success -> setState { copy(wonders = it.data, loading = false) }
                    is Resource.Error -> {
                        setEffect {
                            DiscoverContract.Effect.ShowErrorToast(it.exception?.localizedMessage.orEmpty())
                        }
                    }
                }
            }
            setState { copy(loading = false) }
        }
    }

    private fun navigateToDetail(id: String) {
        setEffect { DiscoverContract.Effect.NavigateToDetail(id) }
    }
}