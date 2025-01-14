package com.example.wanderpedia.features.home.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.ui.ObserveEffect
import com.example.wanderpedia.core.ui.SnackbarController
import com.example.wanderpedia.core.ui.SnackbarEvent

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    navigateToDetail: (id: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.effect, viewModel) {
        when (it) {
            is HomeContract.Effect.ShowErrorToast -> {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = it.message
                    )
                )
            }

            is HomeContract.Effect.NavigateToDetail -> {
                navigateToDetail(it.wonder.id)
            }
        }
    }

    HomeContent(
        state = state,
        transitionScope = transitionScope,
        contentScope = contentScope,
        handleEvents = viewModel::handleEvents
    )
}