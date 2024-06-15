package com.example.soffisushi.presentation.component.point_address

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.soffisushi.R
import com.example.soffisushi.presentation.component.CircularProgressBox
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@Composable
fun ChoosingPointDialog(
    viewModel: SoffiSushiViewModel,
    hideDialog: () -> Unit
) {
    Dialog(
        onDismissRequest = { hideDialog() }
    ) {
        when (viewModel.points.value) {
            is Status.Pending -> {
                viewModel.getPoints()
            }

            is Status.Loading -> {
                CircularProgressBox(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 96.dp)
                )
            }

            is Status.Error -> {
                val exception = (viewModel.points.value as Status.Error).exception
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = exception.message.toString(),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                    )
                    WrapContentButton(
                        text = stringResource(R.string.try_again),
                        onClick = {
                            viewModel.getPoints()
                        }
                    )
                }
            }

            is Status.Success -> {
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .verticalScroll(rememberScrollState())
                        .padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = stringResource(R.string.choose_address),
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                    )
                    (viewModel.points.value as Status.Success).data.forEach { point ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .clickable {
                                    if (viewModel.pointDocumentId is Status.Success &&
                                        point.documentId != (viewModel.pointDocumentId as Status.Success).data
                                    ) {
                                        viewModel.editSelectedPoint(point.documentId)
                                    }
                                    hideDialog()
                                }
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = point.address,
                                textAlign = TextAlign.Center
                            )
                            if (viewModel.pointDocumentId is Status.Success &&
                                point.documentId == (viewModel.pointDocumentId as Status.Success).data
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = "Location"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}