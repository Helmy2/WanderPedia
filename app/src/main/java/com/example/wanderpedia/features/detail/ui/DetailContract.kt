package com.example.wanderpedia.features.detail.ui

import com.example.wanderpedia.core.domain.model.WonderWithDetails
import com.example.wanderpedia.core.ui.ViewEffect
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

object DetailContract {
    data class State(
        val loading: Boolean = true,
        val wonder: WonderWithDetails? = null,
        val isFavorite: Boolean = wonder?.isFavorite == true
    ) : ViewState

    sealed class Event : ViewEvent {
        data object NavigateBack : Event()
        data object ToggleFavorite : Event()
    }

    sealed class Effect : ViewEffect {
        data class ShowErrorToast(val message: String) : Effect()
        data object NavigateBack : Effect()
    }
}