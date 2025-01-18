package com.example.wanderpedia.features.favorite.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wanderpedia.core.domain.model.Wonder

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FavoriteScreen(
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    viewModel: FavoriteViewModel = hiltViewModel(),
    navigateToDetail: (Wonder) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.navigateDetail, state.error) {
        if (state.navigateDetail != null) {
            navigateToDetail(state.navigateDetail!!)
            viewModel.handleEvents(FavoriteEvent.OnHandelNavigationToDetail)
        }
        if (state.error != null) {
            viewModel.handleEvents(FavoriteEvent.OnHandelError)
        }
    }

    FavoriteContent(
        state = state,
        transitionScope = transitionScope,
        contentScope = contentScope,
        handleEvents = viewModel::handleEvents
    )
}