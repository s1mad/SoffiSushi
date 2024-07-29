package com.example.soffisushi.presentation.screen.choosing_delivery.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.example.soffisushi.R
import com.example.soffisushi.data.remote.firebase.model.Delivery
import com.example.soffisushi.util.Status


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityExposedDropdownMenuBox(
    cities: Status<List<Delivery>>,
    selectedCity: Delivery?,
    editSelectedCity: (city: Delivery?) -> Unit,
    getCities: () -> Unit
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
            value = selectedCity?.name ?: "",
            onValueChange = { },
            label = { Text(text = stringResource(R.string.delivery_city)) },
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
            when (cities) {
                is Status.Pending -> {
                    getCities()
                }

                is Status.Loading -> {
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = stringResource(R.string.loading)
                                )
                                CircularProgressIndicator(modifier = Modifier.size(32.dp))
                            }
                        },
                        onClick = { }
                    )
                }

                is Status.Error -> {
                    DropdownMenuItem(
                        modifier = Modifier.fillMaxWidth(),
                        text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = cities.exception.message.toString()
                                )
                                IconButton(onClick = { getCities() }) {
                                    Icon(
                                        imageVector = Icons.Default.Update,
                                        contentDescription = "Try again get cities"
                                    )
                                }
                            }
                        },
                        onClick = { getCities() }
                    )
                }

                is Status.Success -> {
                    cities.data.forEach { city ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = city.name
                                    )
                                    Text(text = "${city.price} ₽")
                                }
                            },
                            onClick = {
                                editSelectedCity(city)
                                expanded.value = false
                            }
                        )
                    }
                }
            }
        }
    }
}
