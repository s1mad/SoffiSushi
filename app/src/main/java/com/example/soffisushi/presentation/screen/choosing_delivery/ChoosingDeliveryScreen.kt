package com.example.soffisushi.presentation.screen.choosing_delivery

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.soffisushi.R
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.component.button.FillMaxWidthButton
import com.example.soffisushi.presentation.screen.choosing_delivery.component.DeliveryMethodItem
import com.example.soffisushi.presentation.screen.choosing_delivery.component.FillAddressItem
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@Composable
fun ChoosingDeliveryScreen(
    viewModel: SoffiSushiViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(
            visible = viewModel.deliveryInfo.value.showLoadAddress &&
                    viewModel.deliveryInfo.value.isDelivery &&
                    viewModel.deliveryInfo.value.savedAddress != null
        ) {
            FillAddressItem(viewModel = viewModel)
        }
        DeliveryMethodItem(viewModel = viewModel)
        FillMaxWidthButton(
            textEnabled = stringResource(R.string.continue_str),
            textDisabled = stringResource(R.string.temporary_unavailable),
            onClick = {
                if (viewModel.deliveryInfo.value.isDelivery) {
                    val blankFields = mutableListOf<String>()
                    with(viewModel.deliveryInfo.value.address) {
                        if (city == null) blankFields.add(context.getString(R.string.delivery_city))
                        if (street.isBlank()) blankFields.add(context.getString(R.string.delivery_street))
                        if (home.isBlank()) blankFields.add(context.getString(R.string.delivery_home))
                    }
                    if (blankFields.isNotEmpty()) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.fill_required_fields) + " " +
                                    blankFields.toString().drop(1).dropLast(1),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        navController.navigate(Screen.CartUserAndPayment.route)
                        viewModel.saveAddress()
                    }
                } else {
                    navController.navigate(Screen.CartUserAndPayment.route)
                }
                viewModel.loadSavedUser()
            }
        )
    }
}