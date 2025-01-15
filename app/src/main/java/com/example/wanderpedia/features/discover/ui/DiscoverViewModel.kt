package com.example.wanderpedia.features.discover.ui

import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.toCached
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.discover.domain.usecase.GetWondersByUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
            is DiscoverContract.Event.UpdateFilter -> applyFilters(event.filters)
            is DiscoverContract.Event.UpdateShowDialog -> setState { copy(showDialog = event.show) }
            is DiscoverContract.Event.RestFilters -> applyFilters(DiscoverContract.Filter())
            is DiscoverContract.Event.OnItemClick -> setEffect {
                DiscoverContract.Effect.NavigateToDetail(event.wonder)
            }
        }
    }

    private fun loadWonders() {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true) }
            getWondersByUseCase().apply {
                fold(
                    onSuccess = { setState { copy(wonders = it) } },
                    onFailure = { setEffect { DiscoverContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) } },
                )
            }
            setState { copy(loading = false) }
        }
    }

    private fun applyFilters(filters: DiscoverContract.Filter) {
        viewModelScope.launch(ioDispatcher) {
            setState { copy(loading = true, filter = filters) }
            getWondersByUseCase(
                textQuery = filters.text,
                timePeriodQuery = filters.timePeriod.toCached(),
                categoryQuery = filters.category.toCached()
            ).apply {
                fold(
                    onSuccess = { setState { copy(wonders = it) } },
                    onFailure = { setEffect { DiscoverContract.Effect.ShowErrorToast(it.localizedMessage.orEmpty()) } },
                )
            }
            setState { copy(loading = false) }
        }
    }
}