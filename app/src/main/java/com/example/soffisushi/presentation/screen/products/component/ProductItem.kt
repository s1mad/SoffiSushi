package com.example.soffisushi.presentation.screen.products.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.soffisushi.R
import com.example.soffisushi.data.remote.firebase.model.Product
import com.example.soffisushi.domain.model.toSingleProduct
import com.example.soffisushi.presentation.component.button.AddToCartButton
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun ProductItem(product: Product, onClick: () -> Unit, viewModel: SoffiSushiViewModel) {
    val cartItem = viewModel.cartProducts.keys.lastOrNull { it.id in product.id }
    val index = if (cartItem != null) {
        product.id.indexOf(cartItem.id)
    } else {
        product.stop.lastIndexOf(false).takeIf { it != -1 } ?: product.stop.lastIndex
    }
    val imageUrl = mutableStateOf(product.image[index])
    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.TopEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { onClick() }
                .padding(8.dp)
        ) {
            SubcomposeAsyncImage(
                model = imageUrl.value,
                contentDescription = product.name,
                loading = { CircularProgressIndicator() },
                error = {
                    IconButton(
                        onClick = { imageUrl.value = product.image[index] }) {
                        Icon(
                            imageVector = Icons.Rounded.Update,
                            contentDescription = stringResource(R.string.try_again)
                        )
                    }
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium),
                alpha = if (product.stop.all { it }) 0.4f else 1f
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = product.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = "${if (product.singleProduct) "" else stringResource(R.string.from)}${product.price.min()} ₽")

            Spacer(modifier = Modifier.height(4.dp))
            AddToCartButton(product.toSingleProduct(index), viewModel)
        }
        if (product.new || product.hot) {
            val cornerRadius = 4.dp
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(RoundedCornerShape(cornerRadius, 0.dp, 0.dp, cornerRadius))
            ) {
                Text(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(start = cornerRadius, end = cornerRadius),
                    text = if (product.new) "new" else "hot",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                )
            }
        }
    }
}
