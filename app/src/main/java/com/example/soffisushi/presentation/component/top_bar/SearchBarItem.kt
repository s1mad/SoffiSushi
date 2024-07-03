package com.example.soffisushi.presentation.component.top_bar

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.soffisushi.R
import com.example.soffisushi.data.remote.firebase.model.Product
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun SearchBarItem(product: Product, viewModel: SoffiSushiViewModel) {
    val imageUrl = mutableStateOf(product.image.last())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .clip(MaterialTheme.shapes.medium)
            .clickable { viewModel.productToDetails.value = product }
            .padding(8.dp)
    ) {
        SubcomposeAsyncImage(
            model = imageUrl.value,
            contentDescription = product.name,
            loading = { CircularProgressIndicator() },
            error = {
                IconButton(
                    onClick = { imageUrl.value = product.image.last() }) {
                    Icon(imageVector = Icons.Rounded.Update, contentDescription = "Try again")
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
                overflow = TextOverflow.Ellipsis,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                maxLines = 2
            )
            Text(
                text = "${if (product.singleProduct) "" else stringResource(R.string.from)}${product.price[0]} ₽",
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                maxLines = 1
            )
        }
    }
}
