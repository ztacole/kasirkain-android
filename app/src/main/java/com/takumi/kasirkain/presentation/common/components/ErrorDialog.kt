package com.takumi.kasirkain.presentation.common.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
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
                onClick = onDismiss
            )
        },
        title = {  Text(text = "Terjadi Kesalahan!") },
        text = { Text(text = message) }
    )
}