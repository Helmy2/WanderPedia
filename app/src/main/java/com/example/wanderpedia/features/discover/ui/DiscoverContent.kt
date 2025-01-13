package com.example.wanderpedia.features.discover.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wanderpedia.core.ui.component.DefaultButton
import com.example.wanderpedia.core.ui.component.DefaultCircleButton
import com.example.wanderpedia.core.ui.component.DefaultDropdown
import com.example.wanderpedia.core.ui.component.DefaultTextField
import com.example.wanderpedia.core.ui.component.WonderGrid

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DiscoverContent(
    state: DiscoverContract.State,
    modifier: Modifier = Modifier,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    handleEvents: (DiscoverContract.Event) -> Unit
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Box(modifier) {
        WonderGrid(
            wonders = state.wonders,
            loading = state.loading,
            onItemClick = { handleEvents(DiscoverContract.Event.OnItemClick(it)) },
            transitionScope = transitionScope,
            contentScope = contentScope,
            header = {
                Column {
                    DiscoverHeader(
                        text = state.filter.text,
                        onTextChange = {
                            handleEvents(
                                DiscoverContract.Event.UpdateFilter(
                                    state.filter.copy(text = it)
                                )
                            )
                        },
                        onShowDialog = { DiscoverContract.Event.UpdateShowFilterDialog(true) },
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    AnimatedVisibility(state.wonders.isEmpty() && !state.loading) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("No wonders found")
                            DefaultButton(onClick = {
                                handleEvents(DiscoverContract.Event.RestFilters)
                                keyboard?.hide()
                            }) {
                                Text("Rest filters")
                            }
                        }
                    }
                }
            },
        )
        AnimatedVisibility(state.showFilterDialog) {
            FilterDialog(
                filters = state.filter,
                onDismiss = { handleEvents(DiscoverContract.Event.UpdateShowFilterDialog(false)) },
                onApplyFilters = {
                    handleEvents(DiscoverContract.Event.UpdateShowFilterDialog(false))
                    handleEvents(DiscoverContract.Event.UpdateFilter(it))
                },
            )
        }
    }
}

@Composable
fun DiscoverHeader(
    modifier: Modifier = Modifier,
    text: String?,
    onTextChange: (String) -> Unit,
    onShowDialog: () -> Unit,
) {
    DefaultTextField(
        value = text ?: "",
        onValueChange = onTextChange,
        placeholder = { Text("Search wonders") },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text,
        ),
        trailingIcon = {
            DefaultCircleButton(
                onClick = onShowDialog, containerColor = Color.Transparent
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filter"
                )
            }
        },
    )
}

@Composable
fun FilterDialog(
    filters: DiscoverContract.Filter,
    onDismiss: () -> Unit,
    onApplyFilters: (DiscoverContract.Filter) -> Unit
) {
    var selectedTimePeriod by remember { mutableStateOf(filters.timePeriod) }
    var selectedCategory by remember { mutableStateOf(filters.category) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Filter Wonders", style = MaterialTheme.typography.headlineSmall)

                DefaultDropdown(
                    label = { Text("Category") },
                    selectedText = selectedCategory.name,
                    dropdownOptions = filters.categoryList.map { it.name },
                    onCategorySelected = { name ->
                        selectedCategory =
                            filters.categoryList.find { it.name == name } ?: filters.category
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                DefaultDropdown(
                    label = { Text("Time Period") },
                    selectedText = selectedTimePeriod.name,
                    dropdownOptions = filters.timePeriodList.map { it.name },
                    onCategorySelected = { name ->
                        selectedTimePeriod =
                            filters.timePeriodList.find { it.name == name } ?: filters.timePeriod
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onApplyFilters(
                            filters.copy(
                                timePeriod = selectedTimePeriod, category = selectedCategory
                            )
                        )
                    }) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

