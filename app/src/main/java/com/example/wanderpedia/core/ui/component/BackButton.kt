package com.example.wanderpedia.core.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BackButton(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    DefaultCircleButton(
        onClick = onClick,
        modifier = modifier.size(48.dp),
    ) {
        Icon(
            Icons.AutoMirrored.Outlined.ArrowBackIos,
            contentDescription = "Back Button",
        )
    }
}


@Preview
@Composable
private fun BackButtonPrev() {
    BackButton()
}