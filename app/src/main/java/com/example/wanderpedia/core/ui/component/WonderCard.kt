package com.example.wanderpedia.core.ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WonderCard(
    id: String,
    imageUrl: String,
    name: String,
    location: String,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    loading: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    with(transitionScope) {
        Card(
            onClick = onClick,
            modifier = modifier,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(.7f)
                    .fillMaxWidth()
                    .placeholder(loading)
                    .sharedElement(
                        transitionScope.rememberSharedContentState(key = "$id-image"),
                        animatedVisibilityScope = contentScope
                    )
            ) {
                DefaultAsyncImage(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CardDefaults.shape),
                    contentDescription = "Image of $name",
                    error = {
                        Icon(Icons.Outlined.ImageNotSupported, contentDescription = "Error")
                    },
                )
            }

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
                        .sharedElement(
                            transitionScope.rememberSharedContentState(key = "$id-name"),
                            animatedVisibilityScope = contentScope
                        ),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.sharedElement(
                        transitionScope.rememberSharedContentState(key = "$id-location"),
                        animatedVisibilityScope = contentScope
                    )
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
}
