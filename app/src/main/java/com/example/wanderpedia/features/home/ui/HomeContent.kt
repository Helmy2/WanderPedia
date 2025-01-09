package com.example.wanderpedia.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.R
import com.example.wanderpedia.core.ui.component.DefaultAsyncImage
import com.example.wanderpedia.core.ui.component.WonderCarousel
import com.example.wanderpedia.core.ui.component.WonderListRowField

@Composable
fun HomeContent(
    userImageUrl: String,
    ancientWonders: WonderList,
    modernWonders: WonderList,
    newWonders: WonderList,
    civ5Wonders: WonderList,
    civ6Wonders: WonderList,
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
                onItemClick = onItemClick
            )
            WonderListRowField(
                title = modernWonders.name,
                wonderList = modernWonders.wonders,
                onItemClick = onItemClick
            )
            WonderListRowField(
                title = newWonders.name,
                wonderList = newWonders.wonders,
                onItemClick = onItemClick
            )
            WonderListRowField(
                title = newWonders.name,
                wonderList = civ5Wonders.wonders,
                onItemClick = onItemClick
            )
            WonderListRowField(
                title = civ6Wonders.name,
                wonderList = civ6Wonders.wonders,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
fun ProfileField(
    modifier: Modifier = Modifier,
    imageUrl: String,
    minIconSize: Dp = 40.dp,
    profileColorBackground: Color = MaterialTheme.colorScheme.primaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DefaultAsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .background(profileColorBackground)
                .padding(8.dp)
                .defaultMinSize(minIconSize, minIconSize),
            contentDescription = "profile",
            imageUrl = imageUrl,
            error = { Image(painterResource(R.drawable.profile), "profile") },
        )
        Icon(
            Icons.Outlined.Notifications,
            contentDescription = "Notifications",
            Modifier
                .clip(CircleShape)
                .background(containerColor)
                .padding(8.dp)
                .defaultMinSize(minIconSize, minIconSize),
            tint = contentColor
        )
    }
}
