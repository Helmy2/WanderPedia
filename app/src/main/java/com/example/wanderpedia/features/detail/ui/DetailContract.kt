package com.example.wanderpedia.features.detail.ui

import android.content.Context
import com.example.wanderpedia.core.domain.model.WonderWithDetails
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

data class DetailState(
    val loading: Boolean = true,
    val error: String? = null,
    val navigateBack: Boolean = false,
    val wonder: WonderWithDetails? = null,
    val isFavorite: Boolean = wonder?.isFavorite == true
) : ViewState

sealed class DetailEvent : ViewEvent {
    data object NavigateBack : DetailEvent()
    data object ToggleFavorite : DetailEvent()
    data object OnHandelError : DetailEvent()
    data object OnHandelNavigationToDetail : DetailEvent()
    data class OnOpenMapApp(val context: Context) : DetailEvent()
}

