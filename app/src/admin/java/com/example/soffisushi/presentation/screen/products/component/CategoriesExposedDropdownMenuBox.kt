package com.example.soffisushi.presentation.screen.products.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupProperties
import com.example.soffisushi.data.remote.firebase.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesExposedDropdownMenuBox(
    selectedCategory: Category?,
    onSelectedCategoryChange: (Category) -> Unit,
    categories: List<Category>,
    labelString: String
) {
    val expanded = remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = {
            expanded.value = !expanded.value
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = selectedCategory?.name ?: "",
            onValueChange = { },
            label = { Text(text = labelString) },
            readOnly = true,
            singleLine = true,
            maxLines = 1,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            shape = MaterialTheme.shapes.medium
        )
        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            properties = PopupProperties(
                focusable = true,
                dismissOnClickOutside = true,
                dismissOnBackPress = true
            ),
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            if (categories.isEmpty()) {
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "Категории отсутствуют или ещё не загрузились"
                        )
                    },
                    onClick = { }
                )
            } else {
                categories.forEach { category ->
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = { Text(text = category.name) },
                        onClick = {
                            onSelectedCategoryChange(category)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}