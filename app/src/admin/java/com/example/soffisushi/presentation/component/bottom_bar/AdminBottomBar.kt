package com.example.soffisushi.presentation.component.bottom_bar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.soffisushi.presentation.component.bottom_bar.model.AdminBottomBar

@Composable
fun AdminBottomBar(navController: NavHostController) {
    val screens = listOf(
        AdminBottomBar.Products,
        AdminBottomBar.Categories,
        AdminBottomBar.Stocks,
        AdminBottomBar.Deliveries,
    )

    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            screens.forEach { screen ->
                AddAdminNavigationBarItem(
                    screen = screen,
                    navController = navController,
                    currentDestination = currentDestination,
                )
            }
        }
    }
}