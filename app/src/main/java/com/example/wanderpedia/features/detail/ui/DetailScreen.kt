package com.example.wanderpedia.features.detail.ui

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
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveEffect(viewModel.effect, viewModel) {
        when (it) {
            is DetailContract.Effect.ShowErrorToast -> {
                SnackbarController.sendEvent(
                    event = SnackbarEvent(
                        message = it.message
                    )
                )
            }

            is DetailContract.Effect.NavigateBack -> {
                navigateBack()
            }
        }
    }

    DetailContent(
        wonder = state.wonder,
        navigateBack = { viewModel.setEvent(DetailContract.Event.NavigateBack) },
        loading = state.loading,
        transitionScope = transitionScope,
        contentScope = contentScope
    )
}



