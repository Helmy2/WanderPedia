package com.example.wanderpedia.features.discover.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.ui.component.DefaultCircleButton
import com.example.wanderpedia.core.ui.component.DefaultTextField
import com.example.wanderpedia.core.ui.component.WonderCard

@Composable
fun DiscoverContent(
    wonders: List<Wonder>,
    loading: Boolean,
    filters: DiscoverContract.Filters,
    modifier: Modifier = Modifier,
    onItemClick: (id: String) -> Unit,
    showFilterDialog: Boolean,
    onShowDialog: () -> Unit,
    onShowFilterDialogChange: (Boolean) -> Unit,
    onApplyFilters: (DiscoverContract.Filters) -> Unit,
) {
    Column(modifier) {
        DefaultTextField(
            value = filters.text ?: "",
            onValueChange = { onApplyFilters(filters.copy(text = it)) },
            placeholder = { Text("Search wonders") },
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text,
            ),
            trailingIcon = {
                DefaultCircleButton(
                    onClick = onShowDialog,
                    containerColor = Color.Transparent
                ) {
                    Icon(imageVector = Icons.Filled.FilterList, contentDescription = "Filter")
                }
            },
        )

        AnimatedContent(wonders.isEmpty()) {
            if (it) {
                Text("No wonders found", modifier = Modifier.padding(16.dp))
            } else {
                WonderList(
                    wonders = wonders,
                    loading = loading,
                    onItemClick = onItemClick
                )
            }
        }

        AnimatedContent(showFilterDialog) {
            if (it) {
                FilterDialog(
                    filters = filters,
                    onDismiss = { onShowFilterDialogChange(false) },
                    onApplyFilters = onApplyFilters,
                )
            }
        }
    }
}

@Composable
fun WonderList(
    loading: Boolean,
    wonders: List<Wonder>, onItemClick: (id: String) -> Unit,
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
        contentPadding = PaddingValues(top = 8.dp, start = 8.dp, end = 8.dp)
    ) {
        items(if (loading) 6 else 0) { wonder ->
            WonderCard(
                name = "",
                location = "",
                imageUrl = "",
                loading = true,
                onClick = { },
                modifier = Modifier.height(300.dp)
            )
        }
        items(wonders) { wonder ->
            WonderCard(
                name = wonder.name,
                location = wonder.location,
                imageUrl = wonder.images.firstOrNull() ?: "",
                onClick = { onItemClick(wonder.id) },
            )
        }
    }
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
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Filter Wonders", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                DefaultDropdown(
                    selectedText = selectedCategory.name,
                    dropdownOptions = filters.categoryList.map { it.name },
                    onCategorySelected = { name ->
                        selectedCategory =
                            filters.categoryList.find { it.name == name } ?: filters.category
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                DefaultDropdown(
                    selectedText = selectedTimePeriod.name,
                    dropdownOptions = filters.timePeriodList.map { it.name },
                    onCategorySelected = { name ->
                        selectedTimePeriod =
                            filters.timePeriodList.find { it.name == name } ?: filters.timePeriod
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

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
                                timePeriod = selectedTimePeriod,
                                category = selectedCategory
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


@Composable
fun DefaultDropdown(
    selectedText: String,
    dropdownOptions: List<String>,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        DefaultTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        Icons.Outlined.ExpandMore,
                        contentDescription = "Select Category",
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            dropdownOptions.forEach {
                DropdownMenuItem(text = { Text(it) }, onClick = {
                    onCategorySelected(it)
                    expanded = false
                })
            }
        }
    }
}