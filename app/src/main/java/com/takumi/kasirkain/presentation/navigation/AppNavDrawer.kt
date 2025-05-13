package com.takumi.kasirkain.presentation.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.takumi.kasirkain.R
import com.takumi.kasirkain.presentation.theme.LocalSpacing
import kotlinx.coroutines.launch

@Composable
fun AppNavDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    navController: NavHostController,
    onCloseDrawer: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route
    val navItems = listOf(
        NavigationItem(
            name = "Beranda",
            selectedIcon = R.drawable.home_filled,
            unselectedIcon = R.drawable.home_outlined,
            route = Screen.Home.route
        ),
        NavigationItem(
            name = "Riwayat",
            selectedIcon = R.drawable.history_trx_filled,
            unselectedIcon = R.drawable.history_trx_outlined,
            route = Screen.History.route
        )
    )

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = LocalSpacing.current.paddingSmall.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onCloseDrawer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            modifier = Modifier.size(LocalSpacing.current.largeIconSize.dp),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.kasirkain_logo),
                        contentDescription = null,
                        modifier = Modifier.size((LocalSpacing.current.largeIconSize * 1.6).dp).padding(LocalSpacing.current.paddingSmall.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.width(LocalSpacing.current.paddingSmall.dp))
                    Text(
                        text = "KasirKain",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.offset(x = -LocalSpacing.current.paddingLarge.dp)
                    )
                }
                Spacer(Modifier.height(LocalSpacing.current.paddingMedium.dp))
                navItems.map { item ->
                    NavigationDrawerItem(
                        label = { Text(item.name) },
                        icon = {
                            Icon(
                                painter = painterResource(if (currentRoute == item.route) item.selectedIcon else item.unselectedIcon),
                                contentDescription = null,
                                modifier = Modifier.size(LocalSpacing.current.smallIconSize.dp)
                            )
                        },
                        selected = currentRoute == item.route,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedContainerColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            }
        }
    ) {
        content()
    }
}