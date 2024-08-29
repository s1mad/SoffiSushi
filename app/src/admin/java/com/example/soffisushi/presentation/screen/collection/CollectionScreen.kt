package com.example.soffisushi.presentation.screen.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.presentation.component.CircularProgressBox
import com.example.soffisushi.presentation.component.ConfirmDeleteDialog
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.screen.collection.component.CollectionItem
import com.example.soffisushi.util.Status
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

@Composable
fun CollectionScreen(
    modifier: Modifier = Modifier,
    collectionStatus: MutableState<Status<QuerySnapshot>>,
    withImage: Boolean = false,
    setCollectionListener: () -> Unit,
    onEditClick: (documentId: String) -> Unit,
    deleteDocument: (loading: MutableState<Boolean>, documentId: String, hideDialog: () -> Unit) -> Unit,
    onItemClick: (documentId: String) -> Unit
) {
    when (collectionStatus.value) {
        is Status.Pending -> {
            setCollectionListener()
        }

        is Status.Loading -> {
            CircularProgressBox(modifier = modifier)
        }

        is Status.Error -> {
            val exception = (collectionStatus.value as Status.Error).exception

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = exception.message.toString(),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
                WrapContentButton(
                    text = stringResource(R.string.try_again),
                    onClick = { setCollectionListener() }
                )
            }
        }

        is Status.Success -> {
            val collection = (collectionStatus.value as Status.Success).data.documents
            val documentToDelete: MutableState<DocumentSnapshot?> =
                remember { mutableStateOf(null) }

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                items(collection) { documentSnapshot ->
                    val image = if (withImage) {
                        (documentSnapshot.data?.get("image") as? List<String>)?.getOrNull(0)
                            ?: documentSnapshot.data?.get("image").toString()
                    } else {
                        null
                    }
                    CollectionItem(
                        text = documentSnapshot.id,
                        imageUrl = image,
                        withImage = withImage,
                        onClick = { onItemClick(documentSnapshot.id) },
                        onEditClick = { onEditClick(documentSnapshot.id) },
                        onDeleteClick = { documentToDelete.value = documentSnapshot }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(64.dp))
                }
            }
            if (documentToDelete.value != null) {
                val loading = remember { mutableStateOf(false) }
                ConfirmDeleteDialog(
                    documentToDelete = documentToDelete,
                    onDelete = {
                        deleteDocument(
                            loading,
                            documentToDelete.value!!.id,
                            { documentToDelete.value = null })
                    },
                    titleText = "Удалить: \"${documentToDelete.value!!.id}\"?",
                    loading = loading.value
                )
            }
        }
    }
}