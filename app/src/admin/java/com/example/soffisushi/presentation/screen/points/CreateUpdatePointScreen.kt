package com.example.soffisushi.presentation.screen.points

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.soffisushi.data.remote.firebase.model.Point
import com.example.soffisushi.navigation.AdminScreen
import com.example.soffisushi.presentation.component.SoffiSushiTextField
import com.example.soffisushi.presentation.component.TextAndSwitch
import com.example.soffisushi.presentation.component.button.FillMaxWidthButton
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.viewmodel.AdminViewModel

@Composable
fun CreateUpdatePointScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel,
    navController: NavHostController,
    point: Point? = null
) {
    val context = LocalContext.current
    val isLoading = rememberSaveable { mutableStateOf(false) }

    val address = rememberSaveable { mutableStateOf(point?.address ?: "") }

    val open = rememberSaveable { mutableStateOf(point?.open ?: true) }
    val timeZone = rememberSaveable { mutableStateOf(point?.timeZone ?: "Europe/Samara") }
    val startTime = rememberSaveable { mutableLongStateOf(point?.startTime ?: 0) }
    val endTime = rememberSaveable { mutableLongStateOf(point?.endTime ?: 86400000) }

    val secretApi = rememberSaveable { mutableStateOf(point?.secretApi ?: "") }

    val mailSender = rememberSaveable { mutableStateOf(point?.mailSender ?: "") }
    val mailPassword = rememberSaveable { mutableStateOf(point?.mailPassword ?: "") }
    val mailRecipient = rememberSaveable { mutableStateOf(point?.mailRecipient ?: "") }

    val phoneNumber = rememberSaveable { mutableStateOf(point?.phoneNumber ?: "") }
    val vkUrl = rememberSaveable { mutableStateOf(point?.vkUrl ?: "") }

    fun showTimePickerDialog(
        initialHour: Int,
        initialMinute: Int,
        onTimeSelected: (Int, Int) -> Unit
    ) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                onTimeSelected(hour, minute)
            },
            initialHour,
            initialMinute,
            true
        ).show()
    }

    fun navigateUp() {
        navController.navigate(AdminScreen.Points.route) {
            popUpTo(AdminScreen.Points.route) { inclusive = true }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (point == null) {
                address.value
            } else {
                viewModel.selectedPointId.value ?: address.value
            },
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
            fontSize = MaterialTheme.typography.labelLarge.fontSize
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = address.value,
            onValueChange = { address.value = it },
            labelString = "Адресс"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = timeZone.value,
            onValueChange = { timeZone.value = it },
            labelString = "Зона времени"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = secretApi.value,
            onValueChange = { secretApi.value = it },
            labelString = "Секрет api"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = mailSender.value,
            onValueChange = { mailSender.value = it },
            labelString = "Отправитель (mail)"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = mailPassword.value,
            onValueChange = { mailPassword.value = it },
            labelString = "Пароль отправителя (mail)"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = mailRecipient.value,
            onValueChange = { mailRecipient.value = it },
            labelString = "Получатель (mail)"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            labelString = "Номер телефона"
        )
        Spacer(modifier = Modifier.height(8.dp))

        SoffiSushiTextField(
            value = vkUrl.value,
            onValueChange = { vkUrl.value = it },
            labelString = "VK URL"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            WrapContentButton(
                onClick = {
                    showTimePickerDialog(
                        initialHour = (startTime.longValue / 3600000).toInt(),
                        initialMinute = (startTime.longValue % 3600000 / 60000).toInt()
                    ) { hour, minute ->
                        startTime.longValue = (hour * 3600000 + minute * 60000).toLong()
                    }
                },
                text = "Открытие (${startTime.longValue / 3600000}:${startTime.longValue % 3600000 / 60000})"
            )
            WrapContentButton(
                onClick = {
                    showTimePickerDialog(
                        initialHour = (endTime.longValue / 3600000).toInt(),
                        initialMinute = (endTime.longValue % 3600000 / 60000).toInt()
                    ) { hour, minute ->
                        endTime.longValue = (hour * 3600000 + minute * 60000).toLong()
                    }
                },
                text = "Закрытие (${endTime.longValue / 3600000}:${endTime.longValue % 3600000 / 60000})"
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextAndSwitch(
            checked = open.value,
            onCheckedChange = { open.value = it },
            text = "Открыто?"
        )
        Spacer(modifier = Modifier.height(8.dp))

        FillMaxWidthButton(
            onClick = {
                if (address.value.isBlank() || timeZone.value.isBlank() || secretApi.value.isBlank() ||
                    mailSender.value.isBlank() || mailPassword.value.isBlank() || mailRecipient.value.isBlank() ||
                    phoneNumber.value.isBlank() || vkUrl.value.isBlank()
                ) {
                    Toast.makeText(context, "Поля заполнены некоректно", Toast.LENGTH_LONG).show()
                } else {
                    if (point == null) {
                        isLoading.value = true
                        viewModel.createPoint(
                            id = address.value,
                            point = Point(
                                address = address.value,
                                open = open.value,
                                timeZone = timeZone.value,
                                startTime = startTime.longValue,
                                endTime = endTime.longValue,
                                secretApi = secretApi.value,
                                mailSender = mailSender.value,
                                mailPassword = mailPassword.value,
                                mailRecipient = mailRecipient.value,
                                phoneNumber = phoneNumber.value,
                                vkUrl = vkUrl.value
                            ),
                            onSuccess = {
                                isLoading.value = false
                                Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
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
                    } else {
                        isLoading.value = true
                        viewModel.updatePoint(
                            id = viewModel.selectedPointId.value ?: address.value,
                            point = Point(
                                address = address.value,
                                open = open.value,
                                timeZone = timeZone.value,
                                startTime = startTime.longValue,
                                endTime = endTime.longValue,
                                secretApi = secretApi.value,
                                mailSender = mailSender.value,
                                mailPassword = mailPassword.value,
                                mailRecipient = mailRecipient.value,
                                phoneNumber = phoneNumber.value,
                                vkUrl = vkUrl.value
                            ),
                            onSuccess = {
                                isLoading.value = false
                                Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
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
                }
            },
            textEnabled = if (point == null) "Создать точку" else "Обновить точку",
            loading = isLoading.value,
            enabled = !isLoading.value
        )
    }
}