package com.example.soffisushi.presentation.screen.confirm_total.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.data.remote.firebase.model.Delivery
import com.example.soffisushi.domain.model.SingleProduct
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@Composable
fun ConfirmTotalItem(viewModel: SoffiSushiViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        viewModel.cartProducts.keys.forEach { product: SingleProduct ->
            viewModel.cartProducts[product]?.let { count ->
                TotalCheckRow(product = product, count = count)
            }
        }
        if (viewModel.deliveryInfo.value.isDelivery) {
            DeliveryCheckRow(city = viewModel.deliveryInfo.value.address.city!!)
        }
        if (viewModel.withSticks) {
            SticksCheckRow(count = viewModel.additionalInfo.value.sticks)
        }
        if (viewModel.withGingerWasabi) {
            GingerAndWasabiCheckRow(
                withGinger = viewModel.additionalInfo.value.ginger,
                withWasabi = viewModel.additionalInfo.value.wasabi
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.onSurfaceVariant)
        )
        PaymentCheckRow(
            price = viewModel.sumToPay,
            isCash = viewModel.moneyInfo.value.isCash,
            change = viewModel.change,
            userMoney = viewModel.moneyInfo.value.inputtedMoney
        )
    }
}

@Composable
private fun TotalCheckRow(product: SingleProduct, count: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = product.name, modifier = Modifier.weight(1f))
        Text(text = "$count * ${product.price} = ${product.price * count}")
    }
}

@Composable
private fun DeliveryCheckRow(city: Delivery) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = city.name, modifier = Modifier.weight(1f))
        Text(text = city.price.toString())
    }
}

@Composable
private fun SticksCheckRow(count: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = stringResource(R.string.instrumentation), modifier = Modifier.weight(1f))
        Text(text = "$count " + stringResource(R.string.pcs))
    }
}

@Composable
private fun GingerAndWasabiCheckRow(withGinger: Boolean, withWasabi: Boolean) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = if (withGinger) stringResource(R.string.ginger) else stringResource(R.string.without_ginger))
        Text(text = if (withWasabi) stringResource(R.string.wasabi) else stringResource(R.string.without_wasabi))
    }
}

@Composable
private fun PaymentCheckRow(
    price: Int,
    isCash: Boolean,
    change: Int,
    userMoney: String
) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val paymentMethod = stringResource(if (isCash) R.string.cash else R.string.card_qr)
            Text(
                text = stringResource(R.string.to_pay, paymentMethod),
                modifier = Modifier.weight(1f)
            )
            Text(text = price.toString())
        }
        if (isCash && change != 0) {
            Text(text = stringResource(R.string.yours_and_change, userMoney, change))
        }
    }
}