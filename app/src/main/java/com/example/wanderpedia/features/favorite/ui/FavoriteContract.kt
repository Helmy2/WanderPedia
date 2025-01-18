package com.example.wanderpedia.features.favorite.ui

import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

data class FavoriteState(
    val loading: Boolean = false,
    val wonders: List<Wonder> = emptyList(),
    val navigateDetail: Wonder? = null,
    val error: String? = null
) : ViewState

sealed class FavoriteEvent : ViewEvent {
    data class OnItemClick(val wonder: Wonder) : FavoriteEvent()
    data object OnHandelNavigationToDetail : FavoriteEvent()
    data object OnHandelError : FavoriteEvent()
}

