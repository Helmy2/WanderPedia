package com.example.wanderpedia.features.home.ui

import androidx.lifecycle.viewModelScope
import com.example.wanderpedia.core.di.IoDispatcher
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.Resource
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
    val getWondersByCategoryUseCase: GetWondersByCategoryUseCase,
    val getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : BaseViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect>(
    HomeContract.State()
) {
    override fun handleEvents(event: HomeContract.Event) {
        when (event) {
            is HomeContract.Event.OnItemClick -> setEffect {
                HomeContract.Effect.NavigateToDetail(
                    event.id
                )
            }
        }
    }

    init {
        viewModelScope.launch(ioDispatcher) {
            getCurrentUserFlowUseCase().collect {
                when (it) {
                    is Resource.Error -> setEffect { HomeContract.Effect.ShowErrorToast(it.exception?.localizedMessage.orEmpty()) }
                    is Resource.Success -> setState { copy(user = user) }
                }
            }
        }

        setState { copy(loading = true) }
        geDataForCategory(Category.AncientWonders) { setState { copy(ancientWonders = it) } }
        geDataForCategory(Category.NewWonders) { setState { copy(newWonders = it) } }
        geDataForCategory(Category.ModernWonders) { setState { copy(modernWonders = it) } }
        setState { copy(loading = false) }
    }

    private fun geDataForCategory(category: Category, onSuccess: (WonderList) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            getWondersByCategoryUseCase(category).collectLatest {
                when (it) {
                    is Resource.Error -> setEffect { HomeContract.Effect.ShowErrorToast(it.exception?.localizedMessage.orEmpty()) }
                    is Resource.Success -> onSuccess(WonderList(category, it.data))
                }
            }
        }
    }
}