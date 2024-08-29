package com.example.soffisushi.presentation.component.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.soffisushi.navigation.AdminScreen
import com.example.soffisushi.presentation.viewmodel.AdminViewModel
import androidx.compose.material3.FloatingActionButton as DefaultFloatingActionButton

@Composable
fun FloatingActionButton(viewModel: AdminViewModel, navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    when (currentDestination?.route) {
        AdminScreen.Points.route -> Fab {
            viewModel.setSelectedPointId(null)
            navController.navigate(AdminScreen.CreatePoint.route)
        }

        AdminScreen.Products.route -> Fab {
            viewModel.setSelectedPointId(null)
            navController.navigate(AdminScreen.CreateProduct.route)
        }

        AdminScreen.Categories.route -> Fab {
            viewModel.setSelectedPointId(null)
            navController.navigate(AdminScreen.CreateCategory.route)
        }

        AdminScreen.Stocks.route -> Fab {
            viewModel.setSelectedPointId(null)
            navController.navigate(AdminScreen.CreateStock.route)
        }

        AdminScreen.Deliveries.route -> Fab {
            viewModel.setSelectedPointId(null)
            navController.navigate(AdminScreen.CreateDelivery.route)
        }
    }
}

@Composable
private fun Fab(onClick: () -> Unit) {
    DefaultFloatingActionButton(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Создать точку")
    }
}