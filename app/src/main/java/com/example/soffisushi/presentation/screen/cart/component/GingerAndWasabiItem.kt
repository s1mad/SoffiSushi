package com.example.soffisushi.presentation.screen.cart.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GingerAndWasabiItem(viewModel: SoffiSushiViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.put_ginger),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Start
                )
                Switch(
                    checked = viewModel.additionalInfo.value.ginger,
                    onCheckedChange = { viewModel.editPutGinger() }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.put_wasabi),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    textAlign = TextAlign.Start
                )
                Switch(
                    checked = viewModel.additionalInfo.value.wasabi,
                    onCheckedChange = { viewModel.editPutWasabi() }
                )
            }
        }
    }
}