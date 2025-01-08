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
        val newWonders: WonderList = WonderList(Category.NewWonders, emptyList()),
        val civ5Wonders: WonderList = WonderList(Category.Civ5Wonders, emptyList()),
        val civ6Wonders: WonderList = WonderList(Category.Civ6Wonders, emptyList()),
    ) : ViewState

    sealed class Event : ViewEvent

    sealed class Effect : ViewEffect {
        class ShowErrorToast(val message: String) : Effect()
    }
}

data class WonderList(private val category: Category, val wonders: List<Wonder>) {
    val name = category.name
}