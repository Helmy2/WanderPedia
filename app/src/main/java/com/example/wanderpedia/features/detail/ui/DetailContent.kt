package com.example.wanderpedia.features.detail.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.ui.component.BackButton
import com.example.wanderpedia.core.ui.component.DefaultAppBar
import com.example.wanderpedia.core.ui.component.DefaultAsyncImage
import com.example.wanderpedia.core.ui.component.carouselTransition
import com.example.wanderpedia.core.ui.component.placeholder

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun DetailContent(
    wonder: Wonder?,
    loading: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope
) {
    AnimatedContent(wonder, modifier = modifier) { wonder ->
        if (wonder == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (loading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Something went wrong")
                }
            }
        } else {
            with(transitionScope) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "${wonder.id}-image"),
                            animatedVisibilityScope = contentScope,
                        )
                    ) {
                        ImageSlider(
                            list = wonder.images,
                            loading = loading,
                            contentPadding = PaddingValues(horizontal = 32.dp),
                            modifier = Modifier.height(200.dp),
                            itemModifier = Modifier.fillMaxWidth(),
                        )
                        DefaultAppBar(
                            transitionScope = transitionScope,
                            contentScope = contentScope,
                            leadingContent = {
                                BackButton(onClick = navigateBack)
                            },
                        )
                    }

                    FlowRow {
                        wonder.categories.forEach {
                            Card(
                                modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)
                            ) {
                                Text(
                                    text = it.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = wonder.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "${wonder.id}-name"),
                            animatedVisibilityScope = contentScope,
                        )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "${wonder.id}-location"),
                            animatedVisibilityScope = contentScope,
                        )
                    ) {
                        Icon(
                            Icons.Outlined.Place,
                            contentDescription = "Location",
                            Modifier.size(MaterialTheme.typography.bodyMedium.fontSize.value.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = wonder.location,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Text(
                        text = "Time Period: ${wonder.timePeriod.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(text = wonder.summary, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}


@Composable
fun ImageSlider(
    list: List<String>,
    loading: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
) {
    val pageCount = remember { if (list.isEmpty()) 3 else list.size * 10 }

    val pagerState = rememberPagerState(initialPage = pageCount / 2) { pageCount }

    HorizontalPager(
        state = pagerState,
        contentPadding = contentPadding,
        pageSpacing = 8.dp,
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        beyondViewportPageCount = 1,
    ) {
        Card(
            modifier = itemModifier
                .carouselTransition(it, pagerState)
                .placeholder(loading),
        ) {
            DefaultAsyncImage(
                imageUrl = list[it % list.size],
                contentDescription = "Image of the Wander",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
