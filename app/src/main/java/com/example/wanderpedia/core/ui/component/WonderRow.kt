package com.example.wanderpedia.core.ui.component

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
