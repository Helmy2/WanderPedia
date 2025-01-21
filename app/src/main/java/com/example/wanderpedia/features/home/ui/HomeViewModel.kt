package com.example.wanderpedia.features.home.ui

import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.domain.usecase.GetCurrentUserFlowUseCase
import com.example.wanderpedia.core.ui.BaseViewModel
import com.example.wanderpedia.features.home.domain.usecase.GetWondersByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
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
            val ancientWondersDeferred = async { fetchWondersByCategory(Category.AncientWonders) }
            val modernWondersDeferred = async { fetchWondersByCategory(Category.ModernWonders) }

            val ancientWonders = ancientWondersDeferred.await()
            val modernWonders = modernWondersDeferred.await()

            setState {
                copy(
                    ancientWonders = WonderList(Category.AncientWonders, ancientWonders),
                    modernWonders = WonderList(Category.ModernWonders, modernWonders),
                    loading = false
                )
            }
        }
    }

    private suspend fun fetchWondersByCategory(category: Category): List<Wonder> {
        val result = getWondersByCategoryUseCase(category)
        result.onFailure {
            setEffect {
                HomeContract.Effect.ShowErrorToast(
                    it.localizedMessage ?: ""
                )
            }
        }
        return result.getOrNull() ?: emptyList()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch(ioDispatcher) {
            getCurrentUserFlowUseCase().collect { resource ->
                resource.fold(
                    onSuccess = { user -> setState { copy(user = user) } },
                    onFailure = {
                        setEffect {
                            HomeContract.Effect.ShowErrorToast(
                                it.localizedMessage ?: ""
                            )
                        }
                    },
                )
            }
        }
    }

    private fun navigateToDetail(wonder: Wonder) {
        setEffect { HomeContract.Effect.NavigateToDetail(wonder) }
    }
}

