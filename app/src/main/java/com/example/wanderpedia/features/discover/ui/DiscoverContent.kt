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
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.ui.component.DefaultButton
import com.example.wanderpedia.core.ui.component.DefaultCircleButton
import com.example.wanderpedia.core.ui.component.DefaultDropdown
import com.example.wanderpedia.core.ui.component.DefaultTextField
import com.example.wanderpedia.core.ui.component.WonderGrid

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DiscoverContent(
    wonders: List<Wonder>,
    loading: Boolean,
    filters: DiscoverContract.Filters,
    modifier: Modifier = Modifier,
    showFilterDialog: Boolean,
    transitionScope: SharedTransitionScope,
    contentScope: AnimatedContentScope,
    onItemClick: (id: String) -> Unit,
    onShowDialog: () -> Unit,
    onShowFilterDialogChange: (Boolean) -> Unit,
    onApplyFilters: (DiscoverContract.Filters) -> Unit,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    Box(modifier) {
        WonderGrid(
            wonders = wonders,
            loading = loading,
            onItemClick = onItemClick,
            transitionScope = transitionScope,
            contentScope = contentScope,
            header = {
                Column {
                    DiscoverHeader(
                        text = filters.text,
                        onTextChange = { onApplyFilters(filters.copy(text = it)) },
                        onShowDialog = onShowDialog,
                        modifier = Modifier
                            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                    AnimatedVisibility(wonders.isEmpty()) {
                        Column(
                            Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("No wonders found")
                            DefaultButton(onClick = {
                                onApplyFilters(DiscoverContract.Filters())
                                keyboard?.hide()
                            }) {
                                Text("Rest filters")
                            }
                        }
                    }
                }
            },
        )
        AnimatedVisibility(showFilterDialog) {
            FilterDialog(
                filters = filters,
                onDismiss = { onShowFilterDialogChange(false) },
                onApplyFilters = onApplyFilters,
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
    filters: DiscoverContract.Filters,
    onDismiss: () -> Unit,
    onApplyFilters: (DiscoverContract.Filters) -> Unit
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

