package com.example.wanderpedia.core.ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.wanderpedia.core.domain.model.Wonder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun WonderCarousel(
    wonderList: List<Wonder>,
    onItemClick: (Wonder) -> Unit,
    autoScrollDuration: Long = 2000L,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
    itemModifier: Modifier = Modifier,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
) {
    val pageCount = remember { wonderList.size + 400 }

    val pagerState = rememberPagerState(initialPage = pageCount / 2) { pageCount }

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        with(pagerState) {
            var currentPageKey by remember { mutableIntStateOf(wonderList.size / 2) }
            LaunchedEffect(key1 = currentPageKey) {
                launch {
                    delay(timeMillis = autoScrollDuration)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    animateScrollToPage(page = nextPage)
                    currentPageKey = nextPage
                }
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        contentPadding = contentPadding,
        pageSpacing = 8.dp,
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        beyondViewportPageCount = 1,
    ) {
        if (wonderList.isNotEmpty()) {
            val item = wonderList[it % wonderList.size]
            CarouselItem(
                id = item.id,
                name = item.name,
                imageUrl = item.images.firstOrNull() ?: "",
                onClick = { onItemClick(item) },
                modifier = itemModifier
                    .carouselTransition(it, pagerState),
                transitionScope = transitionScope,
                contentScope = contentScope
            )
        } else {
            CarouselItem(
                loading = true,
                id = "",
                name = "",
                imageUrl = "",
                onClick = {},
                modifier = itemModifier
                    .carouselTransition(it, pagerState),
                transitionScope = transitionScope,
                contentScope = contentScope
            )
        }
    }
}

fun Modifier.carouselTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    val pageOffset =
        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

    val transformation = lerp(
        start = 0.7f, stop = 1f, fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
    alpha = transformation
    scaleY = transformation
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CarouselItem(
    id: String,
    imageUrl: String,
    name: String,
    onClick: () -> Unit,
    loading: Boolean = false,
    modifier: Modifier = Modifier,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
) {
    with(transitionScope) {
        Card(
            onClick = onClick,
            modifier = Modifier.sharedElement(
                transitionScope.rememberSharedContentState(key = "$id-image"),
                animatedVisibilityScope = contentScope
            ) then modifier,
        ) {
            Box {
                DefaultAsyncImage(
                    imageUrl,
                    contentDescription = name,
                    modifier = Modifier
                        .fillMaxSize()
                        .placeholder(loading)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface
                                ),
                            )
                        )
                )
                Text(
                    text = name,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomCenter)
                        .sharedElement(
                            transitionScope.rememberSharedContentState(key = "$id-name"),
                            animatedVisibilityScope = contentScope
                        ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}