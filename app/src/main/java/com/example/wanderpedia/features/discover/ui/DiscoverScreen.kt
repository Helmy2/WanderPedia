package com.example.wanderpedia.features.discover.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DiscoverScreen(
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    viewModel: DiscoverViewModel = hiltViewModel(),
    navigateToDetail: (id: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DiscoverContract.Effect.ShowErrorToast -> {
                    snackbarHostState.showSnackbar(message = effect.message)
                }

                is DiscoverContract.Effect.NavigateToDetail -> {
                    navigateToDetail(effect.id)
                }
            }
        }
    }

    DiscoverContent(
        state = state,
        transitionScope = transitionScope,
        contentScope = contentScope,
        handleEvents = viewModel::handleEvents
    )
}