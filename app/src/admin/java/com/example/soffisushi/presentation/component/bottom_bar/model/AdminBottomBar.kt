package com.example.soffisushi.presentation.component.bottom_bar.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.DeliveryDining
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.soffisushi.navigation.AdminScreen

sealed class AdminBottomBar(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Products : AdminBottomBar(
        route = AdminScreen.Products.route,
        title = "Продукты",
        icon = Icons.Rounded.Fastfood
    )

    data object Categories : AdminBottomBar(
        route = AdminScreen.Categories.route,
        title = "Категории",
        icon = Icons.Rounded.Category
    )

    data object Stocks : AdminBottomBar(
        route = AdminScreen.Stocks.route,
        title = "Акции",
        icon = Icons.Rounded.Upcoming
    )

    data object Deliveries : AdminBottomBar(
        route = AdminScreen.Deliveries.route,
        title = "Доставки",
        icon = Icons.Rounded.DeliveryDining
    )
}