package com.takumi.kasirkain.presentation.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppDialog(
    modifier: Modifier = Modifier,
    title: String = "Terjadi Kesalahan!",
    message: String,
    onDismiss: ()-> Unit
) {
    AlertDialog(
        modifier = modifier,
        containerColor = Color.White,
        onDismissRequest = {  },
        confirmButton = {
            AppButton(
                text = "OK",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            )
        },
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}