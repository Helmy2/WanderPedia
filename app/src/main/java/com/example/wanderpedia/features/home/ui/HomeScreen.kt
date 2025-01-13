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

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest {
            when (it) {
                is HomeContract.Effect.ShowErrorToast -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = it.message)
                }

                is HomeContract.Effect.NavigateToDetail -> {
                    navigateToDetail(it.id)
                }
            }
        }
    }

    HomeContent(
        userImageUrl = state.user.imageUrl,
        ancientWonders = state.ancientWonders,
        modernWonders = state.modernWonders,
        transitionScope = transitionScope,
        contentScope = contentScope,
        onItemClick = { viewModel.handleEvents(HomeContract.Event.OnItemClick(it)) }
    )
}