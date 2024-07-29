package com.example.soffisushi.presentation.screen.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@Composable
fun TotalCartPriceItem(
    viewModel: SoffiSushiViewModel
) {
    val countFood = viewModel.cartProducts.toList().sumOf { it.second }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.your_cart),
            fontSize = MaterialTheme.typography.titleLarge.fontSize
        )
        TotalCartItemRow(
            name = stringResource(R.string.total),
            count = countFood,
            sum = viewModel.sumOfCartProducts
        )
    }
}

@Composable
private fun TotalCartItemRow(name: String, count: Int, sum: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$name ($count)")
        Text(text = sum.toString())
    }
}