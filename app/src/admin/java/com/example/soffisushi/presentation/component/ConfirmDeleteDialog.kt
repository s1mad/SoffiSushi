package com.example.soffisushi.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.DocumentSnapshot

@Composable
fun ConfirmDeleteDialog(
    documentToDelete: MutableState<DocumentSnapshot?>,
    onDelete: () -> Unit,
    titleText: String,
    loading: Boolean
) {
    AlertDialog(
        onDismissRequest = { documentToDelete.value = null },
        confirmButton = {
            Button(
                onClick = onDelete,
                shape = MaterialTheme.shapes.medium,
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = "УДАЛИТЬ")
                }
            }
        },
        dismissButton = {
            Button(
                onClick = { documentToDelete.value = null },
                shape = MaterialTheme.shapes.medium,
                enabled = !loading,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.12f)
                )
            ) {
                Text(text = "Отмена")
            }
        },
        shape = MaterialTheme.shapes.medium,
        title = { Text(text = titleText) }
    )
}