package com.example.soffisushi.presentation.screen.choosing_user_and_payment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.soffisushi.R
import com.example.soffisushi.presentation.component.TabSelector
import com.example.soffisushi.presentation.screen.choosing_user_and_payment.model.PaymentMethod
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodItem(viewModel: SoffiSushiViewModel) {
    val context = LocalContext.current
    val items = remember {
        listOf(
            PaymentMethod.Cash(context).name,
            PaymentMethod.Terminal(context).name
        )
    }

    val selectedIndex = remember { mutableIntStateOf(0) }

    if (viewModel.moneyInfo.value.isCash) {
        selectedIndex.intValue = 0
    } else {
        selectedIndex.intValue = 1
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TabSelector(
            selectedIndex = selectedIndex,
            items = items,
            onSelectionChange = {
                viewModel.editPaymentMethod()
            }
        )
        if (items[selectedIndex.intValue] == PaymentMethod.Cash(context).name) {
            Spacer(modifier = Modifier.height(0.dp))
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Text(text = stringResource(R.string.to_pay_will, viewModel.sumToPay))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.moneyInfo.value.inputtedMoney,
                    onValueChange = {
                        if (it.isDigitsOnly()) {
                            viewModel.editUserMoney(it)
                        }
                    },
                    label = { Text(text = stringResource(R.string.how_much_money_will_you_give)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    shape = MaterialTheme.shapes.medium
                )
            }
        }
    }
}