package com.takumi.kasirkain.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Maroon,
    secondary = LightGray,
    tertiary = Pink,
    background = LightGray,
    surface = LightGray,
    primaryContainer = Color.White,
    onPrimary = Color.White,
    onSecondary = Gray,
    onTertiary = Red,
    onBackground = Black,
    onSurface = Black,
    onPrimaryContainer = Black
)

@Composable
fun KasirKainTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}