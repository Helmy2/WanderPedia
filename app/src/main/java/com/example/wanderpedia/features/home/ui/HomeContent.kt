package com.example.wanderpedia.features.home.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.wanderpedia.core.ui.component.DefaultAsyncImage
import com.example.wanderpedia.core.ui.component.DefaultCircleButton
import com.example.wanderpedia.core.ui.component.WonderCarousel
import com.example.wanderpedia.core.ui.component.WonderRow

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HomeContent(
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    userImageUrl: String,
    ancientWonders: WonderList,
    modernWonders: WonderList,
    modifier: Modifier = Modifier,
    onItemClick: (id: String) -> Unit,
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
                .padding(top = 16.dp)
        ) {
            ProfileField(
                imageUrl = userImageUrl,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            WonderCarousel(
                wonderList = ancientWonders.wonders,
                onItemClick = { onItemClick(it.id) },
                contentPadding = PaddingValues(horizontal = 50.dp),
                modifier = Modifier.height(200.dp),
                itemModifier = Modifier.width(300.dp),
                transitionScope = transitionScope,
                contentScope = contentScope,
            )
            WonderRow(
                title = modernWonders.name,
                wonderList = modernWonders.wonders,
                onItemClick = { onItemClick(it.id) },
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

@Composable
fun ProfileField(
    modifier: Modifier = Modifier,
    imageUrl: String,
    iconSize: Dp = 48.dp,
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultCircleButton(
            onClick = { },
            modifier = Modifier.size(iconSize)
        ) {
            DefaultAsyncImage(
                contentDescription = "profile",
                imageUrl = imageUrl,
                error = { Image(painterResource(R.drawable.profile), "profile") },
            )
        }

        DefaultCircleButton(
            onClick = { },
            modifier = Modifier.size(iconSize)
        ) {
            Icon(
                Icons.Outlined.Notifications,
                contentDescription = "Notifications",
            )
        }
    }
}
