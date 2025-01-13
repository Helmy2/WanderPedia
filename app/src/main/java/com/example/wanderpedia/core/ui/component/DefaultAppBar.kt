package com.example.wanderpedia.core.ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DefaultAppBar(
    modifier: Modifier = Modifier,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    trailingContent: @Composable () -> Unit = {},
    leadingContent: @Composable () -> Unit = {},
) {
    with(transitionScope) {
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.sharedElement(
                    state = transitionScope.rememberSharedContentState(key = "leading-app-bar"),
                    animatedVisibilityScope = contentScope,
                )
            ) {
                leadingContent.invoke()
            }
            Box(
                modifier = Modifier.sharedElement(
                    state = transitionScope.rememberSharedContentState(key = "trailing-app-bar"),
                    animatedVisibilityScope = contentScope
                )
            ) {
                trailingContent.invoke()
            }
        }
    }
}