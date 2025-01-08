package com.example.wanderpedia.core.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.placeholder
import com.eygraber.compose.placeholder.material3.shimmer

@Composable
fun Modifier.placeholder(
    visible: Boolean,
): Modifier = composed {
    Modifier.placeholder(
        visible = visible,
        highlight = PlaceholderHighlight.shimmer()
    )
}
