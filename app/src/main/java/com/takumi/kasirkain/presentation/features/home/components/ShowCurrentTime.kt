package com.takumi.kasirkain.presentation.features.home.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ShowCurrentTime(modifier: Modifier) {
    val timeFormat = remember { SimpleDateFormat("HH:mm 'WIB'", Locale.getDefault()) }
    val currentTime by produceState(initialValue = timeFormat.format(Date())) {
        while (true) {
            value = timeFormat.format(Date())
            delay(1000)
        }
    }

    Text(
        text = currentTime,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}