package com.takumi.kasirkain.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf

data class Dimensions(
    val paddingSmall: Int,
    val paddingMedium: Int,
    val paddingLarge: Int,
    val smallIconSize: Int,
    val mediumIconSize: Int,
    val largeIconSize: Int
)

val PhoneDimensions = Dimensions(
    paddingSmall = 8,
    paddingMedium = 16,
    paddingLarge = 24,
    smallIconSize = 24,
    mediumIconSize = 32,
    largeIconSize = 48
)

val TabletDimensions = Dimensions(
    paddingSmall = 8,
    paddingMedium = 16,
    paddingLarge = 24,
    smallIconSize = 24,
    mediumIconSize = 32,
    largeIconSize = 48
)

val LocalSpacing = staticCompositionLocalOf { PhoneDimensions }