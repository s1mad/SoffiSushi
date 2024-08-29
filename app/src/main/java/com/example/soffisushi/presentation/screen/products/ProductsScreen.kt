package com.example.soffisushi.presentation.screen.products

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.presentation.component.CircularProgressBox
import com.example.soffisushi.presentation.component.ProductDetailsDialog
import com.example.soffisushi.presentation.component.TapOnProductItemGuide
import com.example.soffisushi.presentation.component.WorkTimeItem
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.component.point_address.ChoosingPointDialog
import com.example.soffisushi.presentation.component.point_address.PointAddressItem
import com.example.soffisushi.presentation.screen.products.component.ProductItem
import com.example.soffisushi.presentation.screen.products.component.header
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.SearchFilter
import com.example.soffisushi.util.Status
import com.example.soffisushi.util.filter

@Composable
fun ProductsScreen(
    viewModel: SoffiSushiViewModel,
    modifier: Modifier,
    searchFilter: SearchFilter = SearchFilter()
) {
    val resultProducts = remember { viewModel.products }
    val showChoosePointDialog = remember { mutableStateOf(false) }

    when (resultProducts.value) {
        is Status.Pending -> {
            if (viewModel.selectedPoint.value is Status.Success) {
                viewModel.setProductsListener()
            }
        }

        is Status.Loading -> {
            CircularProgressBox(modifier = modifier)
        }

        is Status.Error -> {
            val exception = (resultProducts.value as Status.Error).exception

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = exception.message.toString(),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
                WrapContentButton(
                    text = stringResource(R.string.try_again),
                    onClick = {
                        viewModel.setProductsListener()
                    }
                )
            }
        }

        is Status.Success -> {
            val products = (resultProducts.value as Status.Success).data

            if (products.isEmpty()) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    PointAddressItem(
                        viewModel = viewModel,
                        onClick = { showChoosePointDialog.value = true })

                    if (!viewModel.pointIsOpen.value) {
                        WorkTimeItem()
                    }
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = stringResource(R.string.products_is_absent),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                val filteredProducts = products.filter(
                    searchFilter,
                    categories = if (viewModel.categories.value is Status.Success) {
                        (viewModel.categories.value as Status.Success).data
                    } else {
                        emptyList()
                    }
                )

                if (filteredProducts.isEmpty()) {
                    Column(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(48.dp))
                        Text(
                            text = stringResource(R.string.not_found),
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    val sortedProducts = if (viewModel.categories.value is Status.Success) {
                        filteredProducts
                            .sortedWith(compareBy {
                                (viewModel.categories.value as Status.Success).data.associateBy { category -> category.id }[it.categoryId]?.sortNumber
                                    ?: Int.MAX_VALUE
                            })
                            .sortedByDescending { it.hot }
                            .sortedByDescending { it.new }
                    } else {
                        filteredProducts
                            .sortedByDescending { it.hot }
                            .sortedByDescending { it.new }
                    }

                    LazyVerticalGrid(
                        modifier = modifier,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        columns = GridCells.Adaptive(144.dp)
                    ) {
                        header {
                            PointAddressItem(
                                viewModel = viewModel,
                                onClick = { showChoosePointDialog.value = true })
                        }
                        if (!viewModel.pointIsOpen.value) {
                            header {
                                WorkTimeItem()
                            }
                        }
                        if (viewModel.isFirstEntrance.value) {
                            header {
                                TapOnProductItemGuide()
                            }
                        }
                        items(sortedProducts) { product ->
                            ProductItem(
                                viewModel = viewModel,
                                product = product,
                                onClick = {
                                    viewModel.productToDetails.value = product
                                }
                            )
                        }
                    }
                    ProductDetailsDialog(
                        product = viewModel.productToDetails,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    if (showChoosePointDialog.value) {
        ChoosingPointDialog(
            viewModel = viewModel,
            hideDialog = {
                showChoosePointDialog.value = false
            }
        )
    }
}