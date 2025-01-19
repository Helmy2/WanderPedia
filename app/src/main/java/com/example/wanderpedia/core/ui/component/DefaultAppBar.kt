package com.example.wanderpedia.core.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DefaultAppBar(
    modifier: Modifier = Modifier,
    trailingContent: @Composable () -> Unit = {},
    leadingContent: @Composable () -> Unit = {},
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box {
            leadingContent.invoke()
        }
        Box {
            trailingContent.invoke()
        }
    }
}