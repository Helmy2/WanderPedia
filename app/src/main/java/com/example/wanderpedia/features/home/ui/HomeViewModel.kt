package com.example.wanderpedia.features.home.ui

import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.WonderWithDigitalis
import com.example.wanderpedia.core.domain.model.handleResource
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.home.domain.usecase.GetCurrentUserFlowUseCase
import com.example.wanderpedia.features.home.domain.usecase.GetWondersByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getWondersByCategoryUseCase: GetWondersByCategoryUseCase,
    private val getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>(
    HomeContract.State()
) {

    init {
        fetchInitialData()
        observeCurrentUser()
    }

    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.OnItemClick -> navigateToDetail(event.wonder)
        }
    }

    private fun fetchInitialData() {
        setState { copy(loading = true) }

        viewModelScope.launch(ioDispatcher) {
            val ancientWondersJob = launch {
                fetchDataForCategory(Category.AncientWonders) {
                    setState { copy(ancientWonders = it) }
                }
            }
            val modernWondersJob = launch {
                fetchDataForCategory(Category.ModernWonders) {
                    setState { copy(modernWonders = it) }
                }
            }

            // Wait for both jobs to complete before setting loading to false
            ancientWondersJob.join()
            modernWondersJob.join()
            setState { copy(loading = false) }
        }
    }

    private fun observeCurrentUser() {
        viewModelScope.launch(ioDispatcher) {
            getCurrentUserFlowUseCase().collect { resource ->
                resource.handleResource(
                    onSuccess = { user -> setState { copy(user = user) } },
                    onError = { errorMessage ->
                        setEffect {
                            HomeContract.Effect.ShowErrorToast(
                                errorMessage
                            )
                        }
                    }
                )
            }
        }
    }

    private fun fetchDataForCategory(
        category: Category,
        updateStateWithData: (WonderList) -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            getWondersByCategoryUseCase(category).collectLatest { resource ->
                resource.handleResource(
                    onSuccess = { data -> updateStateWithData(WonderList(category, data)) },
                    onError = { errorMessage ->
                        setEffect {
                            HomeContract.Effect.ShowErrorToast(
                                errorMessage
                            )
                        }
                    }
                )
            }
        }
    }

    private fun navigateToDetail(wonder: WonderWithDigitalis) {
        setEffect { HomeContract.Effect.NavigateToDetail(wonder) }
    }
}

