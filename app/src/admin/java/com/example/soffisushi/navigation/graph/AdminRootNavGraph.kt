package com.example.soffisushi.navigation.graph

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.soffisushi.data.remote.firebase.model.Category
import com.example.soffisushi.data.remote.firebase.model.Delivery
import com.example.soffisushi.data.remote.firebase.model.Point
import com.example.soffisushi.data.remote.firebase.model.Product
import com.example.soffisushi.data.remote.firebase.model.Stock
import com.example.soffisushi.navigation.ADMIN_ROOT_GRAPH_ROUTE
import com.example.soffisushi.navigation.AdminScreen
import com.example.soffisushi.presentation.screen.categories.CreateUpdateCategoryScreen
import com.example.soffisushi.presentation.screen.collection.CollectionScreen
import com.example.soffisushi.presentation.screen.deliveries.CreateUpdateDeliveryScreen
import com.example.soffisushi.presentation.screen.points.CreateUpdatePointScreen
import com.example.soffisushi.presentation.screen.products.CreateUpdateProductScreen
import com.example.soffisushi.presentation.screen.stocks.CreateUpdateStockScreen
import com.example.soffisushi.presentation.viewmodel.AdminViewModel
import com.example.soffisushi.util.Status

@Composable
fun AdminRootNavGraph(
    modifier: Modifier,
    viewModel: AdminViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = AdminScreen.Points.route,
        route = ADMIN_ROOT_GRAPH_ROUTE
    ) {
        // Point
        composable(route = AdminScreen.Points.route) {
            CollectionScreen(
                modifier = modifier,
                collectionStatus = viewModel.points,
                withImage = false,
                setCollectionListener = { viewModel.setPointsListener() },
                onEditClick = { documentId ->
                    viewModel.setSelectedPointId(documentId)
                    navController.navigate(AdminScreen.EditPoint.route)
                },
                deleteDocument = { loading: MutableState<Boolean>, documentId: String, hideDialog: () -> Unit ->
                    loading.value = true
                    viewModel.deletePoint(
                        id = documentId,
                        onSuccess = {
                            hideDialog()
                            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { exception ->
                            loading.value = false
                            Toast.makeText(
                                context,
                                "Не успешно: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                },
                onItemClick = { documentId ->
                    viewModel.setSelectedPointId(documentId)
                    viewModel.setPointListener()
                    navController.navigate(AdminScreen.Products.route)
                }
            )
        }
        composable(route = AdminScreen.CreatePoint.route) {
            CreateUpdatePointScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = AdminScreen.EditPoint.route) {
            CreateUpdatePointScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                point = (viewModel.points.value as? Status.Success)
                    ?.data?.documents?.find { documentSnapshot ->
                        documentSnapshot.id == viewModel.selectedPointId.value
                    }?.toObject(Point::class.java)
            )
        }

        // Products

        composable(route = AdminScreen.Products.route) {
            CollectionScreen(
                modifier = modifier,
                collectionStatus = viewModel.products,
                withImage = true,
                setCollectionListener = { viewModel.setProductsListener() },
                onEditClick = { documentId ->
                    viewModel.setSelectedProductId(documentId)
                    navController.navigate(AdminScreen.EditProduct.route)
                },
                deleteDocument = { loading: MutableState<Boolean>, documentId: String, hideDialog: () -> Unit ->
                    loading.value = true
                    viewModel.deleteProduct(
                        id = documentId,
                        onSuccess = {
                            hideDialog()
                            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { exception ->
                            loading.value = false
                            Toast.makeText(
                                context,
                                "Не успешно: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                },
                onItemClick = { documentId ->
                    viewModel.setSelectedProductId(documentId)
                    navController.navigate(AdminScreen.EditProduct.route)
                }
            )
        }
        composable(route = AdminScreen.CreateProduct.route) {
            CreateUpdateProductScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = AdminScreen.EditProduct.route) {
            CreateUpdateProductScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                product = (viewModel.products.value as? Status.Success)
                    ?.data?.documents?.find { documentSnapshot ->
                        documentSnapshot.id == viewModel.selectedProductId.value
                    }?.toObject(Product::class.java)
            )
        }

        // Categories

        composable(route = AdminScreen.Categories.route) {
            CollectionScreen(
                modifier = modifier,
                collectionStatus = viewModel.categories,
                withImage = true,
                setCollectionListener = { viewModel.setCategoriesListener() },
                onEditClick = { documentId ->
                    viewModel.setSelectedCategoryId(documentId)
                    navController.navigate(AdminScreen.EditCategory.route)
                },
                deleteDocument = { loading: MutableState<Boolean>, documentId: String, hideDialog: () -> Unit ->
                    loading.value = true
                    viewModel.deleteCategory(
                        id = documentId,
                        onSuccess = {
                            hideDialog()
                            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { exception ->
                            loading.value = false
                            Toast.makeText(
                                context,
                                "Не успешно: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                },
                onItemClick = { documentId ->
                    viewModel.setSelectedCategoryId(documentId)
                    navController.navigate(AdminScreen.EditCategory.route)
                }
            )
        }
        composable(route = AdminScreen.CreateCategory.route) {
            CreateUpdateCategoryScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = AdminScreen.EditCategory.route) {
            CreateUpdateCategoryScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                category = (viewModel.categories.value as? Status.Success)
                    ?.data?.documents?.find { documentSnapshot ->
                        documentSnapshot.id == viewModel.selectedCategoryId.value
                    }?.toObject(Category::class.java)
            )
        }

        // Stocks

        composable(route = AdminScreen.Stocks.route) {
            CollectionScreen(
                modifier = modifier,
                collectionStatus = viewModel.stocks,
                withImage = false,
                setCollectionListener = { viewModel.setStocksListener() },
                onEditClick = { documentId ->
                    viewModel.setSelectedStockId(documentId)
                    navController.navigate(AdminScreen.EditStock.route)
                },
                deleteDocument = { loading: MutableState<Boolean>, documentId: String, hideDialog: () -> Unit ->
                    loading.value = true
                    viewModel.deleteStock(
                        id = documentId,
                        onSuccess = {
                            hideDialog()
                            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { exception ->
                            loading.value = false
                            Toast.makeText(
                                context,
                                "Не успешно: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                },
                onItemClick = { documentId ->
                    viewModel.setSelectedStockId(documentId)
                    navController.navigate(AdminScreen.EditStock.route)
                }
            )
        }
        composable(route = AdminScreen.CreateStock.route) {
            CreateUpdateStockScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = AdminScreen.EditStock.route) {
            CreateUpdateStockScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                stock = (viewModel.stocks.value as? Status.Success)
                    ?.data?.documents?.find { documentSnapshot ->
                        documentSnapshot.id == viewModel.selectedStockId.value
                    }?.toObject(Stock::class.java)
            )
        }

        // Deliveries

        composable(route = AdminScreen.Deliveries.route) {
            CollectionScreen(
                modifier = modifier,
                collectionStatus = viewModel.deliveries,
                withImage = false,
                setCollectionListener = { viewModel.setDeliveriesListener() },
                onEditClick = { documentId ->
                    viewModel.setSelectedDeliveryId(documentId)
                    navController.navigate(AdminScreen.EditDelivery.route)
                },
                deleteDocument = { loading: MutableState<Boolean>, documentId: String, hideDialog: () -> Unit ->
                    loading.value = true
                    viewModel.deleteDelivery(
                        id = documentId,
                        onSuccess = {
                            hideDialog()
                            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                        },
                        onFailure = { exception ->
                            loading.value = false
                            Toast.makeText(
                                context,
                                "Не успешно: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                },
                onItemClick = { documentId ->
                    viewModel.setSelectedDeliveryId(documentId)
                    navController.navigate(AdminScreen.EditDelivery.route)
                }
            )
        }
        composable(route = AdminScreen.CreateDelivery.route) {
            CreateUpdateDeliveryScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = AdminScreen.EditDelivery.route) {
            CreateUpdateDeliveryScreen(
                modifier = modifier,
                viewModel = viewModel,
                navController = navController,
                delivery = (viewModel.deliveries.value as? Status.Success)
                    ?.data?.documents?.find { documentSnapshot ->
                        documentSnapshot.id == viewModel.selectedDeliveryId.value
                    }?.toObject(Delivery::class.java)
            )
        }
    }
}