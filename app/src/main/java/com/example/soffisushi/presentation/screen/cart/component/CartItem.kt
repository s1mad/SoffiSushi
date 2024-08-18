package com.example.soffisushi.presentation.screen.cart.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.soffisushi.R
import com.example.soffisushi.domain.model.SingleProduct
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@SuppressLint("UnrememberedMutableState")
@Composable
fun CartItem(
    product: SingleProduct,
    viewModel: SoffiSushiViewModel
) {
    val imageUrl = mutableStateOf(product.image)
    val count = viewModel.cartProducts[product] ?: return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                if (viewModel.products.value is Status.Success) {
                    viewModel.productToDetails.value =
                        (viewModel.products.value as Status.Success).data.find { findProduct ->
                            findProduct.id.any { it == product.id }
                        }
                }
            }
            .padding(8.dp)
    ) {
        SubcomposeAsyncImage(
            model = imageUrl.value,
            contentDescription = product.name,
            loading = { CircularProgressIndicator() },
            error = {
                IconButton(
                    onClick = { imageUrl.value = product.image }) {
                    Icon(
                        imageVector = Icons.Rounded.Update,
                        contentDescription = stringResource(R.string.try_again)
                    )
                }
            },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .fillMaxHeight()
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = product.name,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
            Text(
                text = "${product.price * count} ₽",
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
            if (count > 1) {
                Text(
                    text = "${product.price} / " + stringResource(R.string.pcs) + " ₽",
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { viewModel.removeCartProducts(product) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Remove product from cart",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Box(
                    modifier = if (count > 1) {
                        Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { viewModel.removeCartProduct(product) }
                    } else {
                        Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface)
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Remove,
                        contentDescription = "Reduce products",
                        tint = if (count > 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Clip,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { viewModel.addCartProduct(product) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add products",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}