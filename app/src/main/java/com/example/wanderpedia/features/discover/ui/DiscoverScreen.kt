package com.example.wanderpedia.features.discover.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel = hiltViewModel(),
    navigateToDetail: (id: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showFilterDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DiscoverContract.Effect.ShowErrorToast -> {
                    // Handle error toast
                }

                is DiscoverContract.Effect.NavigateToDetail -> {
                    navigateToDetail(effect.id)
                }
            }
        }
    }

    DiscoverContent(
        wonders = state.wonders,
        loading = state.loading,
        filters = state.filters,
        modifier = Modifier,
        onItemClick = { viewModel.handleEvents(DiscoverContract.Event.OnItemClick(it)) },
        showFilterDialog = showFilterDialog,
        onShowFilterDialogChange = { showFilterDialog = it },
        onShowDialog = { showFilterDialog = true },
        onApplyFilters = {
            viewModel.handleEvents(DiscoverContract.Event.ApplyFilters(it))
            showFilterDialog = false
        },
    )

}