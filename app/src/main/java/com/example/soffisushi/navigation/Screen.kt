package com.example.soffisushi.navigation

import com.example.soffisushi.util.SearchFilter
import com.google.gson.Gson

const val ROOT_GRAPH_ROUTE = "root_graph"
const val CART_GRAPH_ROUTE = "cart_graph"

sealed class Screen(val route: String) {
    data object Products : Screen(route = "products") {
        const val ARG_NAME = "searchFilter"
        fun routeAndArg() = "$route/{$ARG_NAME}"
        fun routeWithArg(searchFilter: SearchFilter) = "$route/${Gson().toJson(searchFilter)}"
    }

    data object Categories : Screen(route = "categories") {
        const val ARG_NAME = "parentId"
        fun routeAndArg() = "$route/{$ARG_NAME}"
        fun routeWithArg(id: Long) = "$route/$id"
    }

    data object Stocks : Screen(route = "stocks")

    data object Cart : Screen(route = "cart")
    data object CartDeliveryMethod : Screen(route = "cart_delivery_method")
    data object CartUserAndPayment : Screen(route = "cart_user_and_payment")
    data object CartConfirmTotal : Screen(route = "cart_confirm_total")
    data object CartSuccessOrErrorOrder : Screen(route = "cart_success_or_error")

    data object AboutUs : Screen(route = "about_us")
    data object UserAgreement : Screen(route = "user_agreement")
}