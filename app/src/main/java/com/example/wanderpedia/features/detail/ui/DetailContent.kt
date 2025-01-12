package com.example.wanderpedia.features.detail.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wanderpedia.core.domain.model.Wonder
import com.example.wanderpedia.core.ui.component.DefaultAsyncImage

@Composable
fun DetailContent(wonder: Wonder) {
    Column(modifier = Modifier.padding(16.dp)) {
        DefaultAsyncImage(
            imageUrl = wonder.images.firstOrNull().orEmpty(),
            contentDescription = wonder.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Text(text = wonder.name, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = wonder.location, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = wonder.summary, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Category: ${wonder.categories.joinToString(", ")}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Time Period: ${wonder.timePeriod.name}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}