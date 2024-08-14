package com.example.soffisushi.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.soffisushi.navigation.ROOT_GRAPH_ROUTE
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.screen.about.AboutScreen
import com.example.soffisushi.presentation.screen.categories.CategoriesScreen
import com.example.soffisushi.presentation.screen.products.ProductsScreen
import com.example.soffisushi.presentation.screen.stocks.StocksScreen
import com.example.soffisushi.presentation.screen.user_agreement.UserAgreementScreen
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.SearchFilter
import com.google.gson.Gson

@Composable
fun RootNavGraph(
    navController: NavHostController,
    viewModel: SoffiSushiViewModel,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Products.route,
        route = ROOT_GRAPH_ROUTE
    ) {
        cartNavGraph(
            cartViewModel = viewModel,
            navController = navController,
            modifier = modifier
        )

        composable(route = Screen.Products.route) {
            ProductsScreen(
                viewModel = viewModel,
                modifier = modifier
            )
        }
        composable(
            route = Screen.Products.routeAndArg(),
            arguments = listOf(
                navArgument(Screen.Products.ARG_NAME) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString(Screen.Products.ARG_NAME)
            val searchFilter = Gson().fromJson(json, SearchFilter::class.java)
            ProductsScreen(
                viewModel = viewModel,
                modifier = modifier,
                searchFilter = searchFilter
            )
        }

        composable(
            route = Screen.Categories.route,
        ) {
            CategoriesScreen(
                viewModel = viewModel,
                navController = navController,
                modifier = modifier
            )
        }
        composable(
            route = Screen.Categories.routeAndArg(),
            arguments = listOf(
                navArgument(Screen.Categories.ARG_NAME) {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            CategoriesScreen(
                viewModel = viewModel,
                navController = navController,
                selectedCategoryId = backStackEntry.arguments?.getLong(Screen.Categories.ARG_NAME),
                modifier = modifier
            )
        }
        composable(route = Screen.Stocks.route) {
            StocksScreen(modifier = modifier, viewModel = viewModel)
        }
        composable(route = Screen.AboutUs.route) {
            AboutScreen(viewModel = viewModel, modifier = modifier, navController = navController)
        }
        composable(route = Screen.UserAgreement.route) {
            UserAgreementScreen(modifier = modifier)
        }
    }
}