package com.example.wanderpedia.features.home.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    navigateToDetail: (id: String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect {
            when (it) {
                is HomeContract.Effect.ShowErrorToast -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(message = it.message)
                }

                is HomeContract.Effect.NavigateToDetail -> {
                    navigateToDetail(it.id)
                }
            }
        }
    }

    HomeContent(
        userImageUrl = state.user.imageUrl,
        ancientWonders = state.ancientWonders,
        modernWonders = state.modernWonders,
        newWonders = state.newWonders,
        modifier = modifier,
        onItemClick = {
            viewModel.handleEvents(
                HomeContract.Event.OnItemClick(it)
            )
        }
    )
}