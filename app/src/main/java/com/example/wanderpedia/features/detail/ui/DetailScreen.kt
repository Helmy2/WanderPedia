package com.example.wanderpedia.features.detail.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DetailContract.Effect.ShowErrorToast -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = effect.message)
                }

                is DetailContract.Effect.NavigateBack -> {
                    navigateBack()
                }
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



