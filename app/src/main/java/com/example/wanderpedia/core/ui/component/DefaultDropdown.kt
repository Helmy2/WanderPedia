package com.example.wanderpedia.core.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefaultDropdown(
    label: @Composable (() -> Unit),
    selectedText: String,
    dropdownOptions: List<String>,
    onCategorySelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onClick = { expanded = true }
        ) {
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    label()
                    Text(
                        text = selectedText,
                        modifier = Modifier.padding(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Icon(
                    Icons.Outlined.ExpandMore,
                    contentDescription = "Select Category",
                )
            }
        }

        AnimatedVisibility(expanded) {
            DefaultDialog(
                onDismissRequest = { expanded = false },
                content = {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dropdownOptions.forEach {
                            Card(
                                content = { Text(it, modifier = Modifier.padding(8.dp)) },
                                onClick = {
                                    onCategorySelected(it)
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            )
        }
    }
}