package com.example.wanderpedia.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.core.domain.model.Wonder


@Composable
fun WonderRow(
    title: String,
    onItemClick: (Wonder) -> Unit,
    wonderList: List<Wonder>,
    itemModifier: Modifier = Modifier,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(if (wonderList.isEmpty()) 2 else 0) {
                WonderCard(
                    loading = true,
                    imageUrl = "",
                    name = "",
                    location = "",
                    onClick = { },
                    modifier = itemModifier
                        .fillMaxHeight()
                )
            }
            items(wonderList) {
                WonderCard(
                    imageUrl = it.images.firstOrNull() ?: "",
                    name = it.name,
                    location = it.location,
                    onClick = { onItemClick(it) },
                    modifier = itemModifier
                        .fillMaxHeight()
                )
            }
        }
    }
}


@Composable
fun WonderCard(
    imageUrl: String,
    name: String,
    location: String,
    loading: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        DefaultAsyncImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .fillMaxHeight(.7f)
                .fillMaxWidth()
                .placeholder(loading),
            contentDescription = "Image of $name",
            error = {
                Icon(Icons.Outlined.ImageNotSupported, contentDescription = "Error")
            },
        )
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .placeholder(loading)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Place,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .size(MaterialTheme.typography.bodySmall.fontSize.value.dp)
                        .placeholder(loading),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .placeholder(loading)
                )
            }
        }
    }
}
