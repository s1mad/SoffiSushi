package com.example.soffisushi.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.SubcomposeAsyncImage
import com.example.soffisushi.data.remote.firebase.model.Product
import com.example.soffisushi.domain.model.toSingleProduct
import com.example.soffisushi.presentation.component.button.AddToCartButton
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@Composable
fun ProductDetailsDialog(
    product: MutableState<Product?>,
    viewModel: SoffiSushiViewModel
) {
    if (product.value != null) {
        if (viewModel.isFirstEntrance.value) {
            viewModel.saveIsFirstEntrance()
        }

        val selectedIndex =
            rememberSaveable {
                mutableIntStateOf(viewModel.productToDetails.value?.stop?.lastIndexOf(false)
                    .takeIf { it != -1 }
                    ?: viewModel.productToDetails.value?.stop?.lastIndex
                    ?: (product.value!!.id.size - 1))
            }
        val singleProduct =
            remember { mutableStateOf(product.value!!.toSingleProduct(selectedIndex.intValue)) }
        val imageUrl = remember { mutableStateOf(singleProduct.value.image) }

        val changeChoice = fun(index: Int) {
            selectedIndex.intValue = index
            singleProduct.value = product.value!!.toSingleProduct(selectedIndex.intValue)
            imageUrl.value = singleProduct.value.image
        }

        Dialog(
            onDismissRequest = { product.value = null }
        ) {
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = imageUrl.value,
                        contentDescription = singleProduct.value.name,
                        loading = { CircularProgressIndicator() },
                        error = {
                            IconButton(
                                onClick = { imageUrl.value = singleProduct.value.image }) {
                                Icon(
                                    imageVector = Icons.Rounded.Update,
                                    contentDescription = "Try again"
                                )
                            }
                        },
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.medium),
                        alpha = if (singleProduct.value.stop) 0.4f else 1f
                    )

                    Text(
                        text = singleProduct.value.name,
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
                        maxLines = 2,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = singleProduct.value.structure,
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                    )
                    Text(
                        text = "${singleProduct.value.price} ₽",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (!product.value!!.singleProduct) {
                        TabSelector(
                            selectedIndex = selectedIndex,
                            items = product.value!!.choices,
                            onSelectionChange = changeChoice
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    AddToCartButton(product = singleProduct.value, viewModel = viewModel)
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopStart
                ) {
                    Icon(
                        modifier = Modifier
                            .alpha(0.95f)
                            .padding(top = 15.dp, start = 15.dp)
                            .size(32.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { product.value = null },
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close"
                    )
                }
                if (product.value!!.new || product.value!!.hot) {
                    val cornerRadius = 4.dp
                    Box(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .clip(
                                RoundedCornerShape(cornerRadius, 0.dp, 0.dp, cornerRadius)
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(start = cornerRadius, end = cornerRadius),
                            text = if (product.value!!.new) "new" else "hot",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle
                        )
                    }
                }
            }
        }
    }
}
