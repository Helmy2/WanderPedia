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
        val showFilterDialog: Boolean = false,
        val wonders: List<Wonder> = emptyList(),
        val filter: Filter = Filter(),
    ) : ViewState

    sealed class Event : ViewEvent {
        data class UpdateFilter(val filters: Filter) : Event()
        data class OnItemClick(val wonder: Wonder) : Event()
        data class UpdateShowFilterDialog(val show: Boolean) : Event()
        data object RestFilters : Event()
    }

    sealed class Effect : ViewEffect {
        data class ShowErrorToast(val message: String) : Effect()
        data class NavigateToDetail(val wonder: Wonder) : Effect()
    }

    data class Filter(
        val text: String? = null,
        val timePeriod: TimePeriod = TimePeriod.All,
        val timePeriodList: List<TimePeriod> = listOf(
            TimePeriod.All,
            TimePeriod.Prehistoric,
            TimePeriod.Ancient,
            TimePeriod.Classical,
            TimePeriod.PostClassical,
            TimePeriod.PostClassical,
            TimePeriod.Modern,
        ),
        val category: Category = Category.ALl,
        val categoryList: List<Category> = listOf(
            Category.ALl,
            Category.AncientWonders,
            Category.ModernWonders,
            Category.NewWonders,
            Category.Civ5Wonders,
            Category.Civ6Wonders,
        )
    )
}