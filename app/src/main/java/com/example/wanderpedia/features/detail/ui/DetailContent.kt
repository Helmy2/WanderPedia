package com.example.wanderpedia.features.detail.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.core.domain.model.Category
import com.example.wanderpedia.core.ui.component.BackButton
import com.example.wanderpedia.core.ui.component.DefaultAppBar
import com.example.wanderpedia.core.ui.component.DefaultAsyncImage
import com.example.wanderpedia.core.ui.component.DefaultCircleButton
import com.example.wanderpedia.core.ui.component.carouselTransition
import com.example.wanderpedia.core.ui.component.placeholder
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class, ExperimentalSharedTransitionApi::class)
@Composable
fun DetailContent(
    state: DetailState,
    handleEvents: (DetailEvent) -> Unit,
    modifier: Modifier = Modifier,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
) {
    val context = LocalContext.current
    var mapInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        mapInitialized = true
    }

    AnimatedContent(state.wonder, modifier = modifier) { wonder ->
        if (wonder == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (state.loading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Something went wrong")
                }
            }
        } else {
            with(transitionScope) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    TopImageCard(
                        imagesUrl = wonder.images,
                        isFavorite = state.isFavorite,
                        onNavigateBack = { handleEvents(DetailEvent.NavigateBack) },
                        onToggleFavorite = { handleEvents(DetailEvent.ToggleFavorite) },
                        modifier = Modifier
                            .height(200.dp)
                            .sharedElement(
                                rememberSharedContentState(key = "${wonder.id}-image"),
                                animatedVisibilityScope = contentScope,
                            )
                    )

                    CategoriesInfoRow(wonder.categories)
                    Text(
                        text = wonder.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "${wonder.id}-name"),
                            animatedVisibilityScope = contentScope,
                        )
                    )
                    LocationInfoRow(
                        location = wonder.location, modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "${wonder.id}-location"),
                            animatedVisibilityScope = contentScope,
                        )
                    )

                    Text(
                        text = "Time Period: ${wonder.timePeriod.name}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(text = wonder.summary, style = MaterialTheme.typography.bodyMedium)

                    AnimatedVisibility(
                        visible = wonder.lat != null && wonder.lng != null
                    ) {
                        LocationCard(lat = wonder.lat!!,
                            lng = wonder.lng!!,
                            mapInitialized = mapInitialized,
                            modifier = Modifier.height(250.dp),
                            onOpenMapApp = {
                                handleEvents(DetailEvent.OnOpenMapApp(context))
                            })
                    }
                }
            }
        }
    }
}

@Composable
fun TopImageCard(
    imagesUrl: List<String>,
    isFavorite: Boolean,
    onNavigateBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        ImageSlider(
            list = imagesUrl,
            contentPadding = PaddingValues(horizontal = 32.dp),
            itemModifier = Modifier.fillMaxWidth(),
        )
        DefaultAppBar(leadingContent = {
            BackButton(onClick = onNavigateBack)
        }, trailingContent = {
            DefaultCircleButton(
                onClick = onToggleFavorite,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = if (isFavorite == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite == true) Color.Red else LocalContentColor.current,
                )
            }
        })
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun CategoriesInfoRow(categories: List<Category>, modifier: Modifier = Modifier) {
    FlowRow(modifier) {
        categories.forEach {
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
}

@Composable
private fun LocationInfoRow(
    location: String, modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Icon(
            Icons.Outlined.Place,
            contentDescription = "Location",
            Modifier.size(MaterialTheme.typography.bodyMedium.fontSize.value.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Text(
            text = location,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}


@Composable
fun ImageSlider(
    list: List<String>,
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
                .placeholder(list.isEmpty()),
        ) {
            DefaultAsyncImage(
                imageUrl = list[it % list.size],
                contentDescription = "Image of the Wander",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}


@Composable
fun LocationCard(
    lat: Double,
    lng: Double,
    mapInitialized: Boolean,
    modifier: Modifier = Modifier,
    onOpenMapApp: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedContent(mapInitialized) {
            if (it) {
                MapContent(lat, lng, onOpenMapApp)
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun MapContent(
    lat: Double, lng: Double, onOpenMapApp: () -> Unit
) {
    val centerLatLng = remember { LatLng(lat, lng) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.builder().target(centerLatLng).zoom(14f).build()
    }
    Box(
        Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxSize()
    ) {
        GoogleMap(
            cameraPositionState = cameraPositionState, properties = MapProperties(
                mapType = MapType.HYBRID,
            ), uiSettings = MapUiSettings(
                compassEnabled = false,
                zoomControlsEnabled = false,
                mapToolbarEnabled = false,
                rotationGesturesEnabled = false,
                scrollGesturesEnabled = false,
                tiltGesturesEnabled = false,
                zoomGesturesEnabled = false,
            )
        ) {
            Circle(
                center = centerLatLng,
                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                strokeColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                radius = 800.0,
            )
        }
        TextButton(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomEnd), onClick = onOpenMapApp
        ) {
            Text(text = "Open in Maps", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
