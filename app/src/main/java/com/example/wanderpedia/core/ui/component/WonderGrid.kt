package com.example.wanderpedia.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.core.domain.model.Wonder


@Composable
fun WonderGrid(
    loading: Boolean,
    wonders: List<Wonder>,
    header: @Composable () -> Unit = {},
    onItemClick: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
        contentPadding = PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp),
        modifier = modifier
    ) {
        item(
            span = StaggeredGridItemSpan.FullLine,
        ) {
            header()
        }
        items(if (loading) 6 else 0) { wonder ->
            WonderCard(
                name = "",
                location = "",
                imageUrl = "",
                loading = true,
                onClick = { },
                modifier = Modifier.height(300.dp)
            )
        }
        items(wonders) { wonder ->
            WonderCard(
                name = wonder.name,
                location = wonder.location,
                imageUrl = wonder.images.firstOrNull() ?: "",
                onClick = { onItemClick(wonder.id) },
            )
        }
    }
}