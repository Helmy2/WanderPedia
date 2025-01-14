package com.example.wanderpedia.features.home.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.R
import com.example.wanderpedia.core.ui.component.DefaultAppBar
import com.example.wanderpedia.core.ui.component.DefaultAsyncImage
import com.example.wanderpedia.core.ui.component.DefaultCircleButton
import com.example.wanderpedia.core.ui.component.WonderCarousel
import com.example.wanderpedia.core.ui.component.WonderRow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeContent(
    state: HomeContract.State,
    handleEvents: (HomeContract.Event) -> Unit,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            ProfileBar(
                imageUrl = state.user.imageUrl,
                modifier = Modifier.padding(horizontal = 16.dp),
                transitionScope = transitionScope,
                contentScope = contentScope,
            )
            WonderCarousel(
                wonderList = state.ancientWonders.wonders,
                onItemClick = { handleEvents(HomeContract.Event.OnItemClick(it)) },
                contentPadding = PaddingValues(horizontal = 50.dp),
                modifier = Modifier.height(200.dp),
                itemModifier = Modifier.width(300.dp),
                transitionScope = transitionScope,
                contentScope = contentScope,
            )
            WonderRow(
                title = state.modernWonders.category.name,
                wonderList = state.modernWonders.wonders,
                onItemClick = { handleEvents(HomeContract.Event.OnItemClick(it)) },
                modifier = Modifier.height(350.dp),
                itemModifier = Modifier
                    .width(200.dp)
                    .padding(horizontal = 8.dp),
                transitionScope = transitionScope,
                contentScope = contentScope,
            )
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ProfileBar(
    imageUrl: String,
    iconSize: Dp = 48.dp,
    modifier: Modifier = Modifier,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
) {
    DefaultAppBar(modifier = modifier,
        transitionScope = transitionScope,
        contentScope = contentScope,
        leadingContent = {
            DefaultCircleButton(
                onClick = { }, modifier = Modifier.size(iconSize)
            ) {
                DefaultAsyncImage(
                    contentDescription = "profile",
                    imageUrl = imageUrl,
                    error = {
                        Image(
                            painterResource(R.drawable.profile),
                            "profile",
                            Modifier.fillMaxSize()
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        },
        trailingContent = {
            DefaultCircleButton(
                onClick = { }, modifier = Modifier.size(iconSize)
            ) {
                Icon(
                    Icons.Outlined.Notifications,
                    contentDescription = "Notifications",
                )
            }
        })
}

