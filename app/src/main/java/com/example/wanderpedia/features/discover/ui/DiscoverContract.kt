package com.example.wanderpedia.features.discover.ui

import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.domain.model.TimePeriod
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.ui.ViewEffect
import com.example.wanderpedia.core.ui.ViewEvent
import com.example.wanderpedia.core.ui.ViewState

class DiscoverContract {
    data class State(
        val loading: Boolean = true,
        val wonders: List<Wonder> = emptyList(),
        val filters: Filters = Filters()
    ) : ViewState

    sealed class Event : ViewEvent {
        data class ApplyFilters(val filters: Filters) : Event()
        data class OnItemClick(val id: String) : Event()
        object LoadWonders : Event()
    }

    sealed class Effect : ViewEffect {
        data class ShowErrorToast(val message: String) : Effect()
        data class NavigateToDetail(val id: String) : Effect()
    }

    data class Filters(
        val text: String? = null,
        val timePeriod: TimePeriod = TimePeriod.All,
        val timePeriodList: List<TimePeriod> = listOf(
            TimePeriod.Prehistoric,
            TimePeriod.Ancient,
            TimePeriod.Classical,
            TimePeriod.PostClassical,
            TimePeriod.PostClassical,
            TimePeriod.Modern,
            TimePeriod.All
        ),
        val category: Category = Category.ALl,
        val categoryList: List<Category> = listOf(
            Category.AncientWonders,
            Category.ModernWonders,
            Category.NewWonders,
            Category.Civ5Wonders,
            Category.Civ6Wonders,
            Category.ALl
        )
    )
}