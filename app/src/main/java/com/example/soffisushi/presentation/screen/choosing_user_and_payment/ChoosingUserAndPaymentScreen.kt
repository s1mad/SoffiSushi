package com.example.soffisushi.presentation.screen.choosing_user_and_payment

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.soffisushi.R
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.component.button.FillMaxWidthButton
import com.example.soffisushi.presentation.screen.choosing_user_and_payment.component.ClientInfoItem
import com.example.soffisushi.presentation.screen.choosing_user_and_payment.component.FillUserInfoItem
import com.example.soffisushi.presentation.screen.choosing_user_and_payment.component.PaymentMethodItem
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@Composable
fun ChoosingUserAndPaymentScreen(
    viewModel: SoffiSushiViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(
            visible = viewModel.userInfo.value.showLoadUser &&
                    viewModel.userInfo.value.savedUser != null
        ) {
            FillUserInfoItem(viewModel)
        }
        ClientInfoItem(viewModel)
        PaymentMethodItem(viewModel)
        FillMaxWidthButton(
            textEnabled = stringResource(R.string.continue_str),
            textDisabled = stringResource(R.string.temporary_unavailable),
            onClick = {
                if (viewModel.userInfo.value.user.phoneNumber.isBlank()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.fill_required_fields) + " " + context.getString(
                            (R.string.phone_number)
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!with(viewModel.userInfo.value.user) {
                        phoneNumber.take(2) == "+7" && phoneNumber.length == 12 ||
                                phoneNumber.take(1) == "8" && phoneNumber.length == 11
                    }
                ) {
                    Toast.makeText(
                        context,
                        "Введите корректный номер телефона",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (viewModel.moneyInfo.value.isCash && viewModel.change < 0) {
                        Toast.makeText(
                            context,
                            "Вы не можете дать меньше, чем сумма к оплате",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        navController.navigate(Screen.CartConfirmTotal.route)
                        viewModel.saveUser()
                    }
                }
            }
        )
    }
}