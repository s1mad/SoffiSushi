package com.example.soffisushi.presentation.screen.categories

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.soffisushi.data.remote.firebase.model.Category
import com.example.soffisushi.navigation.AdminScreen
import com.example.soffisushi.presentation.component.SoffiSushiTextField
import com.example.soffisushi.presentation.component.button.FillMaxWidthButton
import com.example.soffisushi.presentation.viewmodel.AdminViewModel

@Composable
fun CreateUpdateCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel,
    navController: NavHostController,
    category: Category? = null
) {
    val context = LocalContext.current
    val isLoading = rememberSaveable { mutableStateOf(false) }

    val name = rememberSaveable { mutableStateOf(category?.name ?: "") }
    val image = rememberSaveable { mutableStateOf(category?.image ?: "") }
    val id = rememberSaveable { mutableStateOf((category?.id ?: "").toString()) }
    val parentId = rememberSaveable { mutableStateOf((category?.parentId ?: "").toString()) }
    val sortNumber = rememberSaveable { mutableStateOf((category?.sortNumber ?: "").toString()) }

    fun navigateUp() {
        navController.navigate(AdminScreen.Categories.route) {
            popUpTo(AdminScreen.Categories.route) { inclusive = true }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        SoffiSushiTextField(
            value = name.value,
            onValueChange = { name.value = it },
            labelString = "Название"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row {
            SubcomposeAsyncImage(
                model = image.value,
                contentDescription = name.value,
                loading = { CircularProgressIndicator() },
                error = {
                    Icon(imageVector = Icons.Rounded.ErrorOutline, contentDescription = "error")
                },
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxHeight()
            )
            Spacer(modifier = Modifier.width(8.dp))
            SoffiSushiTextField(
                value = image.value,
                onValueChange = { image.value = it },
                labelString = "Фотография"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = id.value,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    id.value = it
                }
            },
            labelString = "id",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = parentId.value,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    parentId.value = it
                }
            },
            labelString = "Родительский id (Пусто для родителя)",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(8.dp))


        SoffiSushiTextField(
            value = sortNumber.value,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    sortNumber.value = it
                }
            },
            labelString = "Порядковый номер",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(8.dp))

        FillMaxWidthButton(
            onClick = {
                if (name.value.isBlank() ||
                    id.value.isBlank() || !id.value.isDigitsOnly() ||
                    parentId.value.isNotBlank() && !parentId.value.isDigitsOnly() ||
                    sortNumber.value.isBlank() || !sortNumber.value.isDigitsOnly()
                ) {
                    Toast.makeText(context, "Поля заполнены некоректно", Toast.LENGTH_LONG).show()
                } else {
                    isLoading.value = true
                    viewModel.createCategory(
                        id = name.value,
                        category = Category(
                            name = name.value,
                            image = image.value,
                            id = id.value.toLong(),
                            parentId = if (parentId.value.isNotBlank() && parentId.value.isDigitsOnly()) parentId.value.toLong() else null,
                            sortNumber = sortNumber.value.toInt()
                        ),
                        onSuccess = {
                            if (viewModel.selectedCategoryId.value == name.value) {
                                if (category == null || category.id == id.value.toLong()) {
                                    isLoading.value = false
                                    Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                                    navigateUp()
                                } else {
                                    viewModel.updateCategoryInProducts(
                                        oldCategoryId = category.id,
                                        newCategoryId = id.value.toLong(),
                                        onSuccess = {
                                            isLoading.value = false
                                            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT)
                                                .show()
                                            navigateUp()
                                        },
                                        onFailure = { exception ->
                                            isLoading.value = false
                                            Toast.makeText(
                                                context,
                                                "Не успешно: ${exception.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    )
                                }
                            } else {
                                viewModel.deleteCategory(
                                    id = viewModel.selectedCategoryId.value.toString(),
                                    onSuccess = {
                                        if (category == null || category.id == id.value.toLong()) {
                                            isLoading.value = false
                                            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT)
                                                .show()
                                            navigateUp()
                                        } else {
                                            viewModel.updateCategoryInProducts(
                                                oldCategoryId = category.id,
                                                newCategoryId = id.value.toLong(),
                                                onSuccess = {
                                                    isLoading.value = false
                                                    Toast.makeText(
                                                        context,
                                                        "Успешно",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                    navigateUp()
                                                },
                                                onFailure = { exception ->
                                                    isLoading.value = false
                                                    Toast.makeText(
                                                        context,
                                                        "Не успешно: ${exception.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            )
                                        }
                                    },
                                    onFailure = {
                                        isLoading.value = false
                                        Toast.makeText(
                                            context,
                                            "Не удалось удалить дубликат: ${viewModel.selectedCategoryId.value}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navigateUp()
                                    }
                                )
                            }
                        },
                        onFailure = { exception ->
                            isLoading.value = false
                            Toast.makeText(
                                context,
                                "Не успешно: ${exception.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }
            },
            textEnabled = if (category == null) "Создать категорию" else "Обновить категорию",
            loading = isLoading.value,
            enabled = !isLoading.value
        )
    }
}