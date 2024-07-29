package com.example.soffisushi.presentation.screen.choosing_delivery.component

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.data.remote.firebase.model.Delivery
import com.example.soffisushi.presentation.component.TabSelector
import com.example.soffisushi.presentation.screen.choosing_delivery.model.DeliveryMethod
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status


@SuppressLint("QueryPermissionsNeeded")
@Composable
fun DeliveryMethodItem(viewModel: SoffiSushiViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val items = remember {
        listOf(
            DeliveryMethod.Delivery(context).name,
            DeliveryMethod.Pickup(context).name,
        )
    }

    val selectedIndex =
        remember { mutableIntStateOf(if (viewModel.deliveryInfo.value.isDelivery) 0 else 1) }

    val maxLength2 = 2
    val maxLength50 = 50
    val maxLength100 = 100

    val soffiAddress =
        if (viewModel.selectedPoint.value is Status.Success) (viewModel.selectedPoint.value as Status.Success).data.address else stringResource(
            R.string.reload_app
        )

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
                selectedIndex.intValue = it
                viewModel.editDeliveryMethod()
            }
        )
        Spacer(modifier = Modifier.height(0.dp))
        when (items[selectedIndex.intValue]) {
            DeliveryMethod.Pickup(context).name -> {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .clickable {
                            try {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("geo:?q=${Uri.encode(soffiAddress)}")
                                    )
                                )
                            } catch (e: Exception) {
                                clipboardManager.setText(AnnotatedString(soffiAddress))
                                Toast
                                    .makeText(
                                        context,
                                        context.getString(R.string.address_copied_to_clipboard),
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    text = buildAnnotatedString {
                        append(stringResource(R.string.pickup_by_address) + " ")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        ) {
                            append(soffiAddress)
                        }
                    }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.additionalInfo.value.descr,
                    onValueChange = {
                        if (it.length <= maxLength100) {
                            viewModel.editDescr(it)
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.exceeding_max_length) + " $maxLength100",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    label = { Text(text = stringResource(R.string.delivery_descr)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    shape = MaterialTheme.shapes.medium
                )
            }

            DeliveryMethod.Delivery(context).name -> {
                CityExposedDropdownMenuBox(
                    cities = viewModel.deliveryInfo.value.cities,
                    selectedCity = viewModel.deliveryInfo.value.address.city,
                    editSelectedCity = { city: Delivery? ->
                        viewModel.editDeliveryAddress(
                            viewModel.deliveryInfo.value.address.copy(city = city)
                        )
                    },
                    getCities = { viewModel.getDeliveryCities() }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = stringResource(R.string.delivery_street)) },
                    value = viewModel.deliveryInfo.value.address.street,
                    onValueChange = {
                        if (it.length <= maxLength50) {
                            viewModel.editDeliveryAddress(
                                viewModel.deliveryInfo.value.address.copy(
                                    street = it
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
                    value = viewModel.deliveryInfo.value.address.home,
                    onValueChange = {
                        if (it.length <= maxLength50) {
                            viewModel.editDeliveryAddress(
                                viewModel.deliveryInfo.value.address.copy(
                                    home = it
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
                    label = { Text(text = stringResource(R.string.delivery_home)) },
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
                    value = viewModel.deliveryInfo.value.address.pod,
                    onValueChange = {
                        if (it.length <= maxLength2) {
                            viewModel.editDeliveryAddress(
                                viewModel.deliveryInfo.value.address.copy(
                                    pod = it
                                )
                            )
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.exceeding_max_length) + " $maxLength2",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    label = { Text(text = stringResource(R.string.delivery_pod)) },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.deliveryInfo.value.address.et,
                    onValueChange = {
                        if (it.length <= maxLength2) {
                            viewModel.editDeliveryAddress(
                                viewModel.deliveryInfo.value.address.copy(
                                    et = it
                                )
                            )
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.exceeding_max_length) + " $maxLength2",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    label = { Text(text = stringResource(R.string.delivery_et)) },
                    maxLines = 1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.deliveryInfo.value.address.apart,
                    onValueChange = {
                        if (it.length <= maxLength50) {
                            viewModel.editDeliveryAddress(
                                viewModel.deliveryInfo.value.address.copy(
                                    apart = it
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
                    label = { Text(text = stringResource(R.string.delivery_apart)) },
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
                    value = viewModel.additionalInfo.value.descr,
                    onValueChange = {
                        if (it.length <= maxLength100) {
                            viewModel.editDescr(it)
                        } else {
                            Toast.makeText(
                                context,
                                context.getString(R.string.exceeding_max_length) + " $maxLength100",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    label = { Text(text = stringResource(R.string.delivery_descr)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    shape = MaterialTheme.shapes.medium
                )
            }
        }
    }
}