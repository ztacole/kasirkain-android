package com.takumi.kasirkain.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.takumi.kasirkain.presentation.features.main.home.HomeScreen
import com.takumi.kasirkain.presentation.features.auth.login.AuthScreen
import com.takumi.kasirkain.presentation.features.main.history.HistoryScreen
import com.takumi.kasirkain.presentation.features.splash.SplashViewModel

@Composable
fun AppNavigation(
    splashViewModel: SplashViewModel,
    navController: NavHostController = rememberNavController()
) {
    val startDestination = splashViewModel.startDestination.value.ifEmpty { "splash" }
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route

    var showScanner by remember { mutableStateOf(false) }

    Scaffold(
        contentWindowInsets = WindowInsets.ime,
        bottomBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.History.route) {
                AppBottomBar(
                    modifier = Modifier,
                    navController = navController,
                ) { showScanner = it }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            enterTransition = {
                ScreenTransitions.slideTransition()
            },
            exitTransition = {
                ScreenTransitions.slideExitTransition()
            }
        ) {
            composable("splash") {  }
            composable(Screen.Auth.route) {
                AuthScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Auth.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen()
            }
        }
    }
}