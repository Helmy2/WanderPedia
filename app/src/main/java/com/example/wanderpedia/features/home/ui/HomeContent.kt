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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.R
import com.example.wanderpedia.core.ui.component.DefaultAsyncImage
import com.example.wanderpedia.core.ui.component.WonderListField

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
            ExploreTheWorldText(modifier = Modifier.padding(horizontal = 16.dp))
            WonderListField(
                title = ancientWonders.name,
                wonderList = ancientWonders.wonders,
                onItemClick = onItemClick
            )
            WonderListField(
                title = modernWonders.name,
                wonderList = modernWonders.wonders,
                onItemClick = onItemClick
            )
            WonderListField(
                title = newWonders.name,
                wonderList = newWonders.wonders,
                onItemClick = onItemClick
            )
            WonderListField(
                title = newWonders.name,
                wonderList = civ5Wonders.wonders,
                onItemClick = onItemClick
            )
            WonderListField(
                title = civ6Wonders.name,
                wonderList = civ6Wonders.wonders,
                onItemClick = onItemClick
            )
        }
    }
}


@Composable
private fun ExploreTheWorldText(modifier: Modifier = Modifier) {
    Text(
        buildAnnotatedString {
            append("Explore the\n")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("Beautiful")
            }
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                )
            ) {
                append(" world")
            }
        },
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
    )
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
