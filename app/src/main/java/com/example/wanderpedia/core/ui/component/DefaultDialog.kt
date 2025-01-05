package com.example.wanderpedia.core.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DefaultDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = modifier
                .wrapContentSize()
                .padding(16.dp),
            tonalElevation = AlertDialogDefaults.TonalElevation,
            content = content,
            shape = RoundedCornerShape(16.dp),
        )
    }
}