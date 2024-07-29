package com.example.soffisushi.presentation.screen.choosing_delivery.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillAddressItem(
    viewModel: SoffiSushiViewModel
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = stringResource(R.string.fill_address) + " ${viewModel.deliveryInfo.value.savedAddress!!.getStreetHome()}?")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    modifier = Modifier.weight(50f),
                    onClick = {
                        viewModel.editDeliveryAddress(viewModel.deliveryInfo.value.savedAddress!!)
                        viewModel.hideLoadAddress()
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = stringResource(R.string.fill))
                }
                Button(
                    modifier = Modifier.weight(50f),
                    onClick = { viewModel.hideLoadAddress() },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = stringResource(R.string.no))
                }
            }
        }
    }
}