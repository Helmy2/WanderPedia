package com.example.wanderpedia.features.home.ui

import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.User
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.ui.ViewEffect
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

class HomeContract {
    data class State(
        val loading: Boolean = true,
        val user: User = User(),
        val ancientWonders: WonderList = WonderList(Category.AncientWonders),
        val modernWonders: WonderList = WonderList(Category.ModernWonders),
    ) : ViewState

    sealed class Event : ViewEvent {
        data class OnItemClick(val wonder: Wonder) : Event()
    }

    sealed class Effect : ViewEffect {
        data class ShowErrorToast(val message: String) : Effect()
        data class NavigateToDetail(val wonder: Wonder) : Effect()
    }
}

data class WonderList(
    val category: Category,
    val wonders: List<Wonder> = emptyList()
)

