package com.example.wanderpedia.core.ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.core.domain.model.Wonder


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WonderRow(
    title: String,
    onItemClick: (Wonder) -> Unit,
    wonderList: List<Wonder>,
    itemModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
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
                    id = "",
                    loading = true,
                    imageUrl = "",
                    name = "",
                    location = "",
                    onClick = { },
                    transitionScope = transitionScope,
                    contentScope = contentScope,
                    modifier = itemModifier
                        .fillMaxHeight()
                )
            }
            items(wonderList, key = { it.id }) {
                WonderCard(
                    id = it.id,
                    imageUrl = it.imageUrl,
                    name = it.name,
                    location = it.location,
                    transitionScope = transitionScope,
                    contentScope = contentScope,
                    onClick = { onItemClick(it) },
                    modifier = itemModifier
                        .fillMaxHeight()
                )
            }
        }
    }
}
