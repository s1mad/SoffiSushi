package com.example.soffisushi.presentation.screen.categories.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.soffisushi.data.remote.firebase.model.Category

@Composable
fun CategoryItem(
    category: Category,
    onClick: () -> Unit
) {
    val imageUrl = remember { mutableStateOf(category.image) }

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
            contentDescription = category.name,
            loading = { CircularProgressIndicator() },
            error = {
                IconButton(
                    onClick = { imageUrl.value = category.image }) {
                    Icon(imageVector = Icons.Rounded.Update, contentDescription = "Try again")
                }
            },
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
        )
        Text(text = category.name, overflow = TextOverflow.Clip, maxLines = 2, minLines = 2)
    }
}
