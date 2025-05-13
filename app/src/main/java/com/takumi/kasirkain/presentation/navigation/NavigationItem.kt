package com.takumi.kasirkain.presentation.navigation

import androidx.annotation.DrawableRes

data class NavigationItem(
    val name: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val route: String
)
