package com.example.soffisushi.presentation.component.bottom_bar.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.soffisushi.R
import com.example.soffisushi.navigation.Screen

sealed class BottomBar(
    val route: String,
    val title: Int,
    val icon: ImageVector,
    val badge: Boolean = false
) {
    data object Home : BottomBar(
        route = Screen.Products.route,
        title = R.string.home,
        icon = Icons.Rounded.Home
    )

    data object Categories : BottomBar(
        route = Screen.Categories.route,
        title = R.string.categories,
        icon = Icons.Rounded.Category
    )

    data object Stocks : BottomBar(
        route = Screen.Stocks.route,
        title = R.string.stocks,
        icon = Icons.Rounded.Upcoming
    )

    data object Cart : BottomBar(
        route = Screen.Cart.route,
        title = R.string.cart,
        icon = Icons.Rounded.ShoppingCart,
        badge = true
    )

    data object About : BottomBar(
        route = Screen.AboutUs.route,
        title = R.string.about_us,
        icon = Icons.Rounded.Menu
    )
}