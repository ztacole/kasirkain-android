package com.takumi.kasirkain.presentation.navigation

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.takumi.kasirkain.R

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onOpenScanner: ()-> Unit
) {
    NavigationBar(
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        val navBackStack by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStack?.destination?.route
        val scanBarcodeRoute = "scan_product_barcode"
        val navItems = listOf(
            NavigationItem(
                name = "Beranda",
                selectedIcon = R.drawable.home_filled,
                unselectedIcon = R.drawable.home_outlined,
                route = Screen.Home.route
            ),
            NavigationItem(
                name = "Scan",
                selectedIcon = R.drawable.ic_scan_filled,
                unselectedIcon = R.drawable.ic_scan_filled,
                route = scanBarcodeRoute
            ),
            NavigationItem(
                name = "Riwayat",
                selectedIcon = R.drawable.history_trx_filled,
                unselectedIcon = R.drawable.history_trx_outlined,
                route = Screen.History.route
            )
        )

        navItems.map { item ->
            NavigationBarItem(
                selected = item.route == currentRoute,
                onClick = {
                    if (item.route == scanBarcodeRoute) {
                        onOpenScanner()
                        return@NavigationBarItem
                    }

                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }

                },
                icon = {
                    if (currentRoute == item.route) Icon(painter = painterResource(item.selectedIcon), null)
                    else Icon(painter = painterResource(item.unselectedIcon), null)
                },
                label = {
                    Text(item.name)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

data class NavigationItem(
    val name: String,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    val route: String
)