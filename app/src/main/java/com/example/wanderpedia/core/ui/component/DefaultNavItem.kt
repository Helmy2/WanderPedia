package com.example.wanderpedia.core.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.wanderpedia.navigation.AppDestinations


data class TopLevelRoute(
    val name: String, val route: AppDestinations, val icon: ImageVector
)

val TOP_LEVEL_ROUTES = listOf(
    TopLevelRoute(
        "Home", AppDestinations.Home, Icons.Filled.Home
    ),
    TopLevelRoute(
        "Discover", AppDestinations.Discover, Icons.Filled.Explore
    ),
    TopLevelRoute(
        "Favorite", AppDestinations.Favorite, Icons.Filled.Favorite
    ),
    TopLevelRoute(
        "Profile", AppDestinations.Profile, Icons.Filled.Person
    ),
)

@Composable
fun DefaultNavBar(
    navyItems: List<TopLevelRoute> = TOP_LEVEL_ROUTES,
    label: @Composable (TopLevelRoute) -> Unit,
    icon: @Composable (TopLevelRoute) -> Unit,
    selected: (TopLevelRoute) -> Boolean,
    onClick: (TopLevelRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
    ) {
        navyItems.forEachIndexed { i, topLevelRoute ->
            DefaultNavItem(
                selected = selected(topLevelRoute),
                icon = { icon(topLevelRoute) },
                label = { label(topLevelRoute) },
                onClick = { onClick(topLevelRoute) },
            )
        }
    }
}


@Composable
fun RowScope.DefaultNavItem(
    label: @Composable () -> Unit,
    selected: Boolean,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBarItem(
        modifier = modifier,
        alwaysShowLabel = false,
        label = label,
        selected = selected,
        icon = icon,
        onClick = onClick
    )
}