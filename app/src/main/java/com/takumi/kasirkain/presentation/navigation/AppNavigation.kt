package com.takumi.kasirkain.presentation.navigation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.waterfall
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.takumi.kasirkain.presentation.common.components.AppDialog
import com.takumi.kasirkain.presentation.common.components.LoadingDialog
import com.takumi.kasirkain.presentation.common.state.UiState
import com.takumi.kasirkain.presentation.features.home.HomeScreen
import com.takumi.kasirkain.presentation.features.auth.login.AuthScreen
import com.takumi.kasirkain.presentation.features.auth.login.AuthTabletScreen
import com.takumi.kasirkain.presentation.features.cart.CartTabletScreen
import com.takumi.kasirkain.presentation.features.checkout.CheckoutTabletScreen
import com.takumi.kasirkain.presentation.features.history.HistoryTabletScreen
import com.takumi.kasirkain.presentation.features.home.HomeViewModel
import com.takumi.kasirkain.presentation.features.home.HomeTabletScreen
import com.takumi.kasirkain.presentation.features.scan.components.AfterScanDialog
import com.takumi.kasirkain.presentation.features.scan.components.ScannerBottomSheet
import com.takumi.kasirkain.presentation.features.splash.SplashViewModel
import com.takumi.kasirkain.presentation.util.DeviceType
import com.takumi.kasirkain.presentation.util.getDeviceType
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    splashViewModel: SplashViewModel,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val scope = rememberCoroutineScope()
    val startDestination = splashViewModel.startDestination.value.ifEmpty { "splash" }
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route
    val deviceType = getDeviceType()

    var previousRoute by rememberSaveable { mutableStateOf("") }

    val productVariant by viewModel.productVariant.collectAsState()

    var showRequestPermission by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        confirmValueChange = {
            it == SheetValue.Hidden
        }
    )
    var showScanner by remember { mutableStateOf(false) }
    var showDeniedDialog by remember { mutableStateOf(false) }
    var scanResult by remember { mutableStateOf("") }

    val navDrawerState = rememberDrawerState(DrawerValue.Closed)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(scanResult) {
        viewModel.getProductVariantDetail(scanResult)
    }

    BackHandler(enabled = navDrawerState.isOpen) {
        scope.launch {
            navDrawerState.close()
        }
    }

    AppNavDrawer(
        modifier = Modifier,
        drawerState = navDrawerState,
        navController = navController,
        onCloseDrawer = {
            scope.launch { navDrawerState.close() }
        },
        onSignOut = {
            viewModel.logout()
            scope.launch { navDrawerState.close() }
            navController.navigate(Screen.Auth.route) {
                popUpTo(Screen.Home.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.waterfall,
            topBar = {
                if (currentRoute != Screen.Auth.route && currentRoute != "splash") {
                    AppTopBar(
                        title = getTitleForRoute(currentRoute),
                        scrollBehavior = scrollBehavior
                    ) {
                        scope.launch { navDrawerState.open() }
                    }
                }
            },
            modifier = Modifier.fillMaxSize().navigationBarsPadding()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                enterTransition = {
                    ScreenTransitions.fadeTransition()
                },
                exitTransition = {
                    ScreenTransitions.fadeExitTransition()
                }
            ) {
                composable("splash") { }
                composable(Screen.Auth.route) {
                    AuthTabletScreen(
                        onLoginSuccess = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Auth.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(
                    Screen.Home.route,
                ) {
                    previousRoute = Screen.Home.route
                    HomeTabletScreen(
                        onNavigateToCart = {
                            navController.navigate(Screen.Cart.route)
                        },
                        scrollBehavior = scrollBehavior
                    )
                }
                composable(
                    Screen.History.route,
                ) {
                    previousRoute = Screen.History.route
                    HistoryTabletScreen(
                        scrollBehavior = scrollBehavior
                    )
                }
                composable(
                    Screen.Cart.route
                ) {
                    CartTabletScreen(
                        onNavigateToCheckoutScreen = {
                            navController.navigate(Screen.Checkout.route) {
                                popUpTo(Screen.Home.route)
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                }
                composable(Screen.Checkout.route) {
                    CheckoutTabletScreen(
                        onNavigateBack = {
                            navController.navigateUp()
                        },
                        onCheckout = {
                            navController.navigate(Screen.History.route) {
                                popUpTo(Screen.Checkout.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermission(
    onGranted: ()-> Unit,
    onDenied: ()-> Unit
) {
    val permissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        permissionState.launchPermissionRequest()
    }

    when {
        permissionState.status.isGranted -> {
            onGranted()
        }
        permissionState.status.shouldShowRationale -> {
            onDenied()
        }
        !permissionState.status.isGranted && !permissionState.status.shouldShowRationale -> {
            onDenied()
        }
    }
}

fun getTitleForRoute(route: String?): String {
    return when (route) {
        Screen.Home.route -> "Beranda"
        Screen.History.route -> "Riwayat"
        Screen.Cart.route -> "Keranjang"
        else -> "KasirKain"
    }
}