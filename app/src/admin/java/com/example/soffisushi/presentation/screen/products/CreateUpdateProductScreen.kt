package com.example.soffisushi.presentation.screen.products

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.soffisushi.data.remote.firebase.model.Category
import com.example.soffisushi.data.remote.firebase.model.Product
import com.example.soffisushi.navigation.AdminScreen
import com.example.soffisushi.presentation.component.SoffiSushiTextField
import com.example.soffisushi.presentation.component.TextAndSwitch
import com.example.soffisushi.presentation.component.button.FillMaxWidthButton
import com.example.soffisushi.presentation.screen.products.component.CategoriesExposedDropdownMenuBox
import com.example.soffisushi.presentation.viewmodel.AdminViewModel
import com.example.soffisushi.util.Status

@Composable
fun CreateUpdateProductScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel,
    navController: NavHostController,
    product: Product? = null
) {
    val context = LocalContext.current
    val isLoading = rememberSaveable { mutableStateOf(false) }

    val categories = (viewModel.categories.value as? Status.Success)
        ?.data?.toObjects(Category::class.java)
        ?: emptyList()
    val selectedCategory =
        remember { mutableStateOf(categories.find { it.id == product?.categoryId }) }

    val name = rememberSaveable { mutableStateOf(product?.name ?: "") }
    val structure = rememberSaveable { mutableStateOf(product?.structure ?: "") }

    val putGingerAndWasabi =
        rememberSaveable { mutableStateOf(product?.putGingerAndWasabi ?: true) }
    val putSticks = rememberSaveable { mutableStateOf(product?.putSticks ?: true) }
    val isNew = rememberSaveable { mutableStateOf(product?.new ?: false) }
    val isHot = rememberSaveable { mutableStateOf(product?.hot ?: false) }

    val countInternalProducts = rememberSaveable { mutableIntStateOf(product?.id?.size ?: 1) }
    val idList =
        rememberSaveable { mutableStateOf(product?.id?.map { it.toString() } ?: listOf("")) }
    val imageList = rememberSaveable { mutableStateOf(product?.image ?: listOf("")) }
    val priceList =
        rememberSaveable { mutableStateOf(product?.price?.map { it.toString() } ?: listOf("")) }
    val choiceList = rememberSaveable { mutableStateOf(product?.choices ?: listOf("")) }
    val stopList = rememberSaveable { mutableStateOf(product?.stop ?: listOf(false)) }

    fun navigateUp() {
        navController.navigate(AdminScreen.Products.route) {
            popUpTo(AdminScreen.Products.route) { inclusive = true }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        CategoriesExposedDropdownMenuBox(
            selectedCategory = selectedCategory.value,
            onSelectedCategoryChange = { chooseCategory ->
                selectedCategory.value = chooseCategory
            },
            categories = categories,
            labelString = "Категория"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = name.value,
            onValueChange = { name.value = it },
            labelString = "Название"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = structure.value,
            onValueChange = { structure.value = it },
            labelString = "Состав"
        )
        Spacer(modifier = Modifier.height(8.dp))

        for (index in 0 until countInternalProducts.intValue) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Продукт №${index + 1}",
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                )
                SoffiSushiTextField(
                    value = idList.value.getOrElse(index) { "" },
                    onValueChange = {
                        val newList = idList.value.toMutableList()
                        if (it.isDigitsOnly()) {
                            newList[index] = it
                        }
                        idList.value = newList
                    },
                    labelString = "Артикул",
                    keyboardType = KeyboardType.Number
                )
                Row {
                    SubcomposeAsyncImage(
                        model = imageList.value.getOrElse(index) { "" },
                        contentDescription = name.value,
                        loading = { CircularProgressIndicator() },
                        error = {
                            Icon(
                                imageVector = Icons.Rounded.ErrorOutline,
                                contentDescription = "error"
                            )
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
                        value = imageList.value.getOrElse(index) { "" },
                        onValueChange = {
                            val newList = imageList.value.toMutableList()
                            newList[index] = it
                            imageList.value = newList
                        },
                        labelString = "URL Фото",
                        keyboardType = KeyboardType.Uri
                    )
                }
                SoffiSushiTextField(
                    value = priceList.value.getOrElse(index) { "" },
                    onValueChange = {
                        val newList = priceList.value.toMutableList()
                        if (it.isDigitsOnly()) {
                            newList[index] = it
                        }
                        priceList.value = newList
                    },
                    labelString = "Цена",
                    keyboardType = KeyboardType.Number
                )
                if (countInternalProducts.intValue != 1) {
                    SoffiSushiTextField(
                        value = choiceList.value.getOrElse(index) { "" },
                        onValueChange = {
                            val newList = choiceList.value.toMutableList()
                            newList[index] = it
                            choiceList.value = newList
                        },
                        labelString = "Название выбора"
                    )
                }
                TextAndSwitch(
                    checked = stopList.value.getOrElse(index) { false },
                    onCheckedChange = { stop ->
                        val newList = stopList.value.toMutableList()
                        newList[index] = stop
                        stopList.value = newList
                    },
                    text = "Stop?"
                )
                FillMaxWidthButton(
                    onClick = {
                        if (countInternalProducts.intValue > 1) {
                            countInternalProducts.intValue -= 1
                            idList.value =
                                idList.value.toMutableList().apply { removeAt(index) }
                            imageList.value =
                                imageList.value.toMutableList().apply { removeAt(index) }
                            priceList.value =
                                priceList.value.toMutableList().apply { removeAt(index) }
                            choiceList.value =
                                choiceList.value.toMutableList().apply { removeAt(index) }
                            stopList.value =
                                stopList.value.toMutableList().apply { removeAt(index) }
                        }
                    },
                    textEnabled = "Убрать внутренний продукт"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        FillMaxWidthButton(
            onClick = {
                countInternalProducts.intValue += 1
                idList.value += ""
                imageList.value += ""
                priceList.value += ""
                choiceList.value += ""
                stopList.value += false
            },
            textEnabled = "Добавить внутренний продукт"
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextAndSwitch(
            checked = putGingerAndWasabi.value,
            onCheckedChange = { putGingerAndWasabi.value = it },
            text = "Васаби и имбирь к продукту?"
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextAndSwitch(
            checked = putSticks.value,
            onCheckedChange = { putSticks.value = it },
            text = "Палочки к продукту?"
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextAndSwitch(
            checked = isNew.value,
            onCheckedChange = { isNew.value = it },
            text = "New?"
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextAndSwitch(
            checked = isHot.value,
            onCheckedChange = { isHot.value = it },
            text = "Hot?"
        )
        Spacer(modifier = Modifier.height(16.dp))

        FillMaxWidthButton(
            onClick = {
                if (selectedCategory.value == null || name.value.isBlank() ||
                    idList.value.fastAny { it.isBlank() || !it.isDigitsOnly() } ||
                    imageList.value.fastAny { it.isBlank() } ||
                    priceList.value.fastAny { it.isBlank() || !it.isDigitsOnly() } ||
                    countInternalProducts.intValue != 1 && choiceList.value.fastAny { it.isBlank() }
                ) {
                    Toast.makeText(context, "Поля заполнены некоректно", Toast.LENGTH_LONG).show()
                } else {
                    isLoading.value = true
                    viewModel.createProduct(
                        id = name.value,
                        product = Product(
                            id = idList.value.map { it.toLong() },
                            categoryId = selectedCategory.value?.id ?: 0,
                            singleProduct = countInternalProducts.intValue == 1,
                            name = name.value,
                            choices = choiceList.value,
                            image = imageList.value,
                            price = priceList.value.map { it.toInt() },
                            structure = structure.value,
                            putGingerAndWasabi = putGingerAndWasabi.value,
                            putSticks = putSticks.value,
                            new = isNew.value,
                            hot = isHot.value,
                            stop = stopList.value
                        ),
                        onSuccess = {
                            if (viewModel.selectedProductId.value == name.value) {
                                isLoading.value = false
                                Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                                navigateUp()
                            } else {
                                viewModel.deleteProduct(
                                    id = viewModel.selectedProductId.value.toString(),
                                    onSuccess = {
                                        isLoading.value = false
                                        Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT)
                                            .show()
                                        navigateUp()
                                    },
                                    onFailure = {
                                        isLoading.value = false
                                        Toast.makeText(
                                            context,
                                            "Не удалось удалить дубликат: ${viewModel.selectedProductId.value}",
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
            textEnabled = if (product == null) "Создать продукт" else "Обновить продукт",
            loading = isLoading.value,
            enabled = !isLoading.value
        )
    }
}
