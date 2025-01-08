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
        geDataForCategory(Category.Civ5Wonders) { setState { copy(civ5Wonders = it) } }
        geDataForCategory(Category.Civ6Wonders) { setState { copy(civ6Wonders = it) } }
        setState { copy(loading = false) }
    }

    private fun geDataForCategory(category: Category, onSuccess: (WonderList) -> Unit) {
        viewModelScope.launch(ioDispatcher) {
            val result = getWondersByCategoryUseCase(category)
            when (result) {
                is Resource.Error -> setEffect { HomeContract.Effect.ShowErrorToast(result.exception?.localizedMessage.orEmpty()) }
                is Resource.Success -> onSuccess(WonderList(category, result.data))
            }
        }
    }
}