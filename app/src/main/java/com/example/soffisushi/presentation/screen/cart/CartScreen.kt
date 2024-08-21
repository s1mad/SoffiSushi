package com.example.soffisushi.presentation.screen.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.soffisushi.R
import com.example.soffisushi.domain.model.SingleProduct
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.component.ProductDetailsDialog
import com.example.soffisushi.presentation.component.WorkTimeItem
import com.example.soffisushi.presentation.component.button.FillMaxWidthButton
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.component.point_address.ChoosingPointDialog
import com.example.soffisushi.presentation.component.point_address.PointAddressItem
import com.example.soffisushi.presentation.screen.cart.component.CartItem
import com.example.soffisushi.presentation.screen.cart.component.CountPersItem
import com.example.soffisushi.presentation.screen.cart.component.GingerAndWasabiItem
import com.example.soffisushi.presentation.screen.cart.component.TotalCartPriceItem
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@Composable
fun CartScreen(
    viewModel: SoffiSushiViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    val products = remember { viewModel.cartProducts }
    val showChoosePointDialog = remember { mutableStateOf(false) }

    if (products.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = stringResource(R.string.empty_cart),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(48.dp))
            WrapContentButton(
                text = stringResource(R.string.go_to_home),
                onClick = {
                    navController.navigate(Screen.Products.route)
                }
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                PointAddressItem(
                    viewModel = viewModel,
                    onClick = {
                        showChoosePointDialog.value = true
                    }
                )
            }

            if (!viewModel.pointIsOpen.value) {
                item {
                    WorkTimeItem()
                }
            }

            items(products.keys.toList<SingleProduct>()) { product ->
                CartItem(product = product, viewModel = viewModel)
            }

            if (viewModel.withGingerWasabi) {
                item {
                    GingerAndWasabiItem(viewModel = viewModel)
                }
            }

            if (viewModel.withSticks) {
                item {
                    CountPersItem(viewModel = viewModel)
                }
            }

            item {
                TotalCartPriceItem(viewModel = viewModel)
            }

            item {
                FillMaxWidthButton(
                    textEnabled = stringResource(R.string.go_to_make_order),
                    textDisabled = stringResource(R.string.temporary_unavailable),
                    enabled = viewModel.pointIsOpen.value,
                    onClick = {
                        navController.navigate(Screen.CartDeliveryMethod.route)
                        if (viewModel.deliveryInfo.value.cities !is Status.Success) {
                            viewModel.getDeliveryCities()
                        }
                        viewModel.loadSavedAddress()
                    }
                )
            }

            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .clickable { navController.navigate(Screen.UserAgreement.route) }
                        .padding(horizontal = 8.dp),
                    text = stringResource(R.string.press_you_agree_with_term_use),
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        }
        ProductDetailsDialog(
            product = viewModel.productToDetails,
            viewModel = viewModel
        )
        if (showChoosePointDialog.value) {
            ChoosingPointDialog(
                viewModel = viewModel,
                hideDialog = {
                    showChoosePointDialog.value = false
                }
            )
        }
    }
}
