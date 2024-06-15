package com.example.soffisushi.presentation.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.domain.model.SingleProduct
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToCartButton(product: SingleProduct, viewModel: SoffiSushiViewModel) {
    val products = remember { viewModel.cartProducts }
    if (product in products && !product.stop) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { viewModel.removeCartProduct(product) }) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = "Reduce products",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text(
                text = viewModel.cartProducts[product].toString(),
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Clip,
                color = MaterialTheme.colorScheme.onPrimary,
                fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                fontSize = MaterialTheme.typography.labelLarge.fontSize,
                fontFamily = MaterialTheme.typography.labelLarge.fontFamily
            )
            IconButton(onClick = { viewModel.addCartProduct(product) }) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add products",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    } else {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                onClick = {
                    viewModel.addCartProduct(product)
                },
                shape = MaterialTheme.shapes.medium,
                enabled = !product.stop
            ) {
                Text(
                    text = if (product.stop) stringResource(R.string.out_of_stock)
                    else stringResource(R.string.add_to_cart),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
