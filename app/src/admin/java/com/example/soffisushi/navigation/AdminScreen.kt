package com.example.soffisushi.navigation

const val ADMIN_ROOT_GRAPH_ROUTE = "ADMIN_ROOT_GRAPH_ROUTE"

sealed class AdminScreen(val route: String) {
    data object Points : AdminScreen("points")
    data object CreatePoint : AdminScreen("create_point")
    data object EditPoint : AdminScreen("edit_point")

    data object Products : AdminScreen("products")
    data object CreateProduct : AdminScreen("create_product")
    data object EditProduct : AdminScreen("edit_product")

    data object Categories : AdminScreen("categories")
    data object CreateCategory : AdminScreen("create_category")
    data object EditCategory : AdminScreen("edit_category")

    data object Stocks : AdminScreen("stocks")
    data object CreateStock : AdminScreen("create_stock")
    data object EditStock : AdminScreen("edit_stock")

    data object Deliveries : AdminScreen("deliveries")
    data object CreateDelivery : AdminScreen("create_delivery")
    data object EditDelivery : AdminScreen("edit_delivery")
}