package com.example.soffisushi.navigation.graph

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.soffisushi.navigation.CART_GRAPH_ROUTE
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.screen.cart.CartScreen
import com.example.soffisushi.presentation.screen.choosing_delivery.ChoosingDeliveryScreen
import com.example.soffisushi.presentation.screen.choosing_user_and_payment.ChoosingUserAndPaymentScreen
import com.example.soffisushi.presentation.screen.confirm_total.ConfirmTotalScreen
import com.example.soffisushi.presentation.screen.success_or_error_order.SuccessOrErrorOrderScreen
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

fun NavGraphBuilder.cartNavGraph(
    navController: NavHostController,
    cartViewModel: SoffiSushiViewModel,
    modifier: Modifier
) {
    navigation(
        startDestination = Screen.Cart.route,
        route = CART_GRAPH_ROUTE
    ) {
        composable(route = Screen.Cart.route) {
            CartScreen(
                viewModel = cartViewModel,
                modifier = modifier,
                navController = navController
            )
        }
        composable(route = Screen.CartDeliveryMethod.route) {
            ChoosingDeliveryScreen(
                viewModel = cartViewModel,
                modifier = modifier,
                navController = navController
            )
        }
        composable(route = Screen.CartUserAndPayment.route) {
            ChoosingUserAndPaymentScreen(
                viewModel = cartViewModel,
                modifier = modifier,
                navController = navController
            )
        }
        composable(route = Screen.CartConfirmTotal.route) {
            ConfirmTotalScreen(
                viewModel = cartViewModel,
                modifier = modifier,
                navController = navController
            )
        }
        composable(route = Screen.CartSuccessOrErrorOrder.route) {
            SuccessOrErrorOrderScreen(
                viewModel = cartViewModel,
                modifier = modifier,
                navController = navController
            )
        }
    }
}


