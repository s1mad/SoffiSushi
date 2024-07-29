package com.example.soffisushi.presentation.screen.choosing_user_and_payment.component

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.example.soffisushi.R
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@Composable
fun ClientInfoItem(viewModel: SoffiSushiViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val context = LocalContext.current
        val maxLength50 = 50

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.userInfo.value.user.name,
            onValueChange = {
                if (it.length <= maxLength50) {
                    viewModel.editUser(
                        viewModel.userInfo.value.user.copy(
                            name = it
                        )
                    )
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.exceeding_max_length) + " $maxLength50",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            label = { Text(text = stringResource(R.string.your_name)) },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            shape = MaterialTheme.shapes.medium
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.userInfo.value.user.phoneNumber,
            onValueChange = {
                if (it.take(1) == "8" && it.isDigitsOnly() && it.length <= 11
                    || (it.take(1) == "+" && it.drop(1).isDigitsOnly() && it.length <= 12
                            || viewModel.userInfo.value.user.phoneNumber.dropLast(1) == it)
                ) {
                    viewModel.editUser(
                        viewModel.userInfo.value.user.copy(
                            phoneNumber = it
                        )
                    )
                }
            },
            label = { Text(text = stringResource(R.string.phone_number)) },
            maxLines = 1,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            shape = MaterialTheme.shapes.medium
        )
    }
}
