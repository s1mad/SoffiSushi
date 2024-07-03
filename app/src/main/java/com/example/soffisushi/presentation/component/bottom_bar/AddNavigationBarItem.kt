package com.example.soffisushi.presentation.component.bottom_bar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.soffisushi.presentation.component.bottom_bar.model.BottomBar

@Composable
fun RowScope.AddNavigationBarItem(
    screen: BottomBar,
    badgeCount: Int = 0,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(
                text = stringResource(screen.title),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        icon = {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(badgeCount.toString())
                        }
                    }
                }
            ) {
                Icon(imageVector = screen.icon, contentDescription = "NavigationIcon")
            }
        },
        selected = currentDestination?.hierarchy?.any {
            it.route?.contains(screen.route) ?: false
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}