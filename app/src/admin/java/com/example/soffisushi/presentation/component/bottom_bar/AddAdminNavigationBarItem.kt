package com.example.soffisushi.presentation.component.bottom_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.soffisushi.presentation.component.bottom_bar.model.AdminBottomBar

@Composable
fun RowScope.AddAdminNavigationBarItem(
    screen: AdminBottomBar,
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    NavigationBarItem(
        label = {
            Text(
                text = screen.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        icon = {
            Icon(imageVector = screen.icon, contentDescription = "NavigationIcon")
        },
        selected = screen.route == currentDestination?.route,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}