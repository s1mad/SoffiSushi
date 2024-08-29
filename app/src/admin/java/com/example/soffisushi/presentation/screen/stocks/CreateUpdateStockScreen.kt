package com.example.soffisushi.presentation.screen.stocks

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
import com.example.soffisushi.data.remote.firebase.model.Stock
import com.example.soffisushi.navigation.AdminScreen
import com.example.soffisushi.presentation.component.SoffiSushiTextField
import com.example.soffisushi.presentation.component.button.FillMaxWidthButton
import com.example.soffisushi.presentation.viewmodel.AdminViewModel

@Composable
fun CreateUpdateStockScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel,
    navController: NavHostController,
    stock: Stock? = null
) {
    val context = LocalContext.current
    val isLoading = rememberSaveable { mutableStateOf(false) }

    val day = rememberSaveable { mutableStateOf((stock?.day ?: "").toString()) }
    val text = rememberSaveable { mutableStateOf(stock?.text ?: "") }

    fun navigateUp() {
        navController.navigate(AdminScreen.Stocks.route) {
            popUpTo(AdminScreen.Stocks.route) { inclusive = true }
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
            value = day.value,
            onValueChange = {
                if (it.isDigitsOnly()) {
                    day.value = it
                }
            },
            labelString = "День (Пусто для постоянной)",
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = text.value,
            onValueChange = { text.value = it },
            labelString = "Текст"
        )
        Spacer(modifier = Modifier.height(8.dp))

        FillMaxWidthButton(
            onClick = {
                if (text.value.isBlank() || day.value.isNotBlank() && !day.value.isDigitsOnly()) {
                    Toast.makeText(context, "Поля заполнены некоректно", Toast.LENGTH_LONG).show()
                } else {
                    isLoading.value = true

                    fun handleSuccess() {
                        isLoading.value = false
                        Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                        navigateUp()
                    }

                    fun handleFailure(message: String) {
                        isLoading.value = false
                        Toast.makeText(context, "Не успешно: $message", Toast.LENGTH_LONG).show()
                    }

                    viewModel.createStock(
                        id = text.value,
                        stock = Stock(
                            day = if (day.value.isNotBlank() && day.value.isDigitsOnly()) day.value.toInt() else null,
                            text = text.value
                        ),
                        onSuccess = {
                            if (viewModel.selectedStockId.value == text.value) {
                                handleSuccess()
                            } else {
                                viewModel.deleteStock(
                                    id = viewModel.selectedStockId.value.toString(),
                                    onSuccess = { handleSuccess() },
                                    onFailure = {
                                        isLoading.value = false
                                        Toast.makeText(
                                            context,
                                            "Не удалось удалить дубликат: ${viewModel.selectedStockId.value}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        navigateUp()
                                    }
                                )
                            }
                        },
                        onFailure = { exception -> handleFailure(exception.message.toString()) }
                    )
                }
            },
            textEnabled = if (stock == null) "Создать акцию" else "Обновить акцию",
            loading = isLoading.value,
            enabled = !isLoading.value
        )
    }
}