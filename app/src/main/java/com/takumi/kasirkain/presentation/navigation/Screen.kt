package com.takumi.kasirkain.presentation.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("home")
    data object History: Screen("history")
    data object Auth: Screen("auth")
}