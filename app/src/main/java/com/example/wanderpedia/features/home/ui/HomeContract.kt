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
        val user: User = User(displayName = "user"),
        val ancientWonders: WonderList = WonderList(Category.AncientWonders, emptyList()),
        val modernWonders: WonderList = WonderList(Category.ModernWonders, emptyList()),
    ) : ViewState

    sealed class Event : ViewEvent {
        data class OnItemClick(val id: String) : Event()
    }

    sealed class Effect : ViewEffect {
        data class ShowErrorToast(val message: String) : Effect()
        data class NavigateToDetail(val id: String) : Effect()
    }
}

data class WonderList(private val category: Category, val wonders: List<Wonder>) {
    val name = category.name
}