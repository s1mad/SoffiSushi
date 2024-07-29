package com.example.soffisushi.presentation.screen.success_or_error_order.component

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@Composable
fun ErrorOrder(
    modifier: Modifier,
    viewModel: SoffiSushiViewModel,
    exception: Exception
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Icon(
            modifier = Modifier.size(144.dp),
            imageVector = Icons.Rounded.ErrorOutline,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = "Error"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(
                R.string.order_has_not_been_shipped,
                exception.message.toString()
            ),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        WrapContentButton(
            text = stringResource(R.string.try_again),
            onClick = {
                val products =
                    viewModel.cartProducts.keys.map { it.id }.toMutableList()
                val productsKol = viewModel.cartProducts.values.toMutableList()
                if (viewModel.deliveryInfo.value.isDelivery) {
                    products.add(viewModel.deliveryInfo.value.address.city!!.id)
                    productsKol.add(1)
                }

                if (!viewModel.pointIsOpen.value) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.we_close),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (viewModel.selectedPoint.value is Status.Success) {
                    viewModel.postNewOrder()
                }
            }
        )
    }
}