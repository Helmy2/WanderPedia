package com.example.wanderpedia.features.detail.ui

import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.ui.ViewEffect
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

class DetailContract {
    data class State(
        val loading: Boolean = true,
        val wonder: Wonder? = null
    ) : ViewState

    sealed class Event : ViewEvent {
        data object NavigateBack : Event()
    }

    sealed class Effect : ViewEffect {
        data class ShowErrorToast(val message: String) : Effect()
        data object NavigateBack : Effect()
    }
}