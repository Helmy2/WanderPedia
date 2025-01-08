package com.example.wanderpedia.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun DefaultAsyncImage(
    imageUrl: String,
    contentDescription: String?,
    error: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    if (imageUrl.isNotBlank()) {
        SubcomposeAsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .crossfade(true).data(imageUrl)
                .build(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            error = { error },
            loading = {
                Box(
                    modifier = modifier.placeholder(true)
                )
            }
        )
    } else {
        Box(
            modifier = modifier
        ) {
            error?.invoke()
        }
    }

}