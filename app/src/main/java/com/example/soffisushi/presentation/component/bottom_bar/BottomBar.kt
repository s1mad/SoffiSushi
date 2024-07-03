package com.example.soffisushi.presentation.component.bottom_bar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.soffisushi.presentation.component.bottom_bar.model.BottomBar
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@Composable
fun BottomBar(viewModel: SoffiSushiViewModel, navController: NavHostController) {
    val screens = listOf(
        BottomBar.Home,
        BottomBar.Categories,
        BottomBar.Stocks,
        BottomBar.Cart,
        BottomBar.About
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination =
        screens.any { currentDestination?.route?.contains(it.route) ?: false }
    if (bottomBarDestination) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            screens.forEach { screen ->
                AddNavigationBarItem(
                    screen = screen,
                    badgeCount = if (screen.badge) {
                        viewModel.cartProducts.values.sum()
                    } else {
                        0
                    },
                    currentDestination = currentDestination,
                    navController = navController,
                )
            }
        }
    }
}