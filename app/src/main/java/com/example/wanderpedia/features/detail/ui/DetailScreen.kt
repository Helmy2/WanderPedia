package com.example.wanderpedia.features.detail.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.SnackbarController
import com.example.wanderpedia.core.ui.SnackbarEvent


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.error, state.navigateBack) {
        if (state.error != null) {
            SnackbarController.sendEvent(
                event = SnackbarEvent(
                    message = state.error!!
                )
            )
            viewModel.handleEvents(DetailEvent.OnHandelError)
        }
        if (state.navigateBack) {
            navigateBack()
            viewModel.handleEvents(DetailEvent.OnHandelNavigationToDetail)
        }
    }

    DetailContent(
        state = state,
        handleEvents = viewModel::handleEvents,
        transitionScope = transitionScope,
        contentScope = contentScope
    )
}



