package com.example.wanderpedia.features.home.ui

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
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    navigateToDetail: (id: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest {
            when (it) {
                is HomeContract.Effect.ShowErrorToast -> {
                    snackbarHostState.showSnackbar(message = it.message)
                }

                is HomeContract.Effect.NavigateToDetail -> {
                    navigateToDetail(it.wonder.id)
                }
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