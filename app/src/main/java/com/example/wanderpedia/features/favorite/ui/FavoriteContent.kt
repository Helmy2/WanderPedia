package com.example.wanderpedia.features.favorite.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.wanderpedia.core.ui.component.WonderGrid


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FavoriteContent(
    state: FavoriteState,
    modifier: Modifier = Modifier,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    handleEvents: (FavoriteEvent) -> Unit
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        WonderGrid(
            wonders = state.wonders,
            onItemClick = { handleEvents(FavoriteEvent.OnItemClick(it)) },
            transitionScope = transitionScope,
            contentScope = contentScope,
            header = {},
            modifier = Modifier.fillMaxSize()
        )
        AnimatedVisibility(state.loading) {
            CircularProgressIndicator()
        }
        AnimatedVisibility(state.wonders.isEmpty() && !state.loading) {
            Text("No wonders found")
        }
    }
}