package com.example.soffisushi.presentation.screen.deliveries

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavHostController
import com.example.soffisushi.data.remote.firebase.model.Delivery
import com.example.soffisushi.navigation.AdminScreen
import com.example.soffisushi.presentation.component.SoffiSushiTextField
import com.example.soffisushi.presentation.component.button.FillMaxWidthButton
import com.example.soffisushi.presentation.viewmodel.AdminViewModel

@Composable
fun CreateUpdateDeliveryScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel,
    navController: NavHostController,
    delivery: Delivery? = null
) {
    val context = LocalContext.current
    val isLoading = rememberSaveable { mutableStateOf(false) }

    val id = rememberSaveable { mutableStateOf((delivery?.id ?: "").toString()) }
    val name = rememberSaveable { mutableStateOf(delivery?.name ?: "") }
    val price = rememberSaveable { mutableStateOf((delivery?.price ?: "").toString()) }

    fun navigateUp() {
        navController.navigate(AdminScreen.Deliveries.route) {
            popUpTo(AdminScreen.Deliveries.route) { inclusive = true }
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
            value = id.value,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    id.value = it
                }
            },
            labelString = "Артикул",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = name.value,
            onValueChange = { name.value = it },
            labelString = "Название"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = price.value,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    price.value = it
                }
            },
            labelString = "Цена",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(8.dp))

        FillMaxWidthButton(
            onClick = {
                if (id.value.isBlank() || !id.value.isDigitsOnly() ||
                    name.value.isBlank() ||
                    price.value.isBlank() || !price.value.isDigitsOnly()
                ) {
                    Toast.makeText(context, "Поля заполнены некоректно", Toast.LENGTH_LONG).show()
                } else {
                    isLoading.value = true
                    viewModel.createDelivery(
                        id = name.value,
                        delivery = Delivery(
                            id = id.value.toLong(),
                            name = name.value,
                            price = price.value.toInt()
                        ),
                        onSuccess = {
                            if (viewModel.selectedDeliveryId.value == name.value) {
                                isLoading.value = false
                                Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                                navigateUp()
                            } else {
                                viewModel.deleteDelivery(
                                    id = viewModel.selectedDeliveryId.value.toString(),
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
                                            "Не удалось удалить дубликат: ${viewModel.selectedDeliveryId.value}",
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
            textEnabled = if (delivery == null) "Создать доставку" else "Обновить доставку",
            loading = isLoading.value,
            enabled = !isLoading.value
        )
    }
}