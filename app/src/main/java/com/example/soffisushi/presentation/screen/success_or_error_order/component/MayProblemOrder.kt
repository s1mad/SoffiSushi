package com.example.soffisushi.presentation.screen.success_or_error_order.component

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@Composable
fun MayProblemOrder(
    modifier: Modifier,
    viewModel: SoffiSushiViewModel
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Icon(
            modifier = Modifier.size(144.dp),
            imageVector = Icons.Rounded.ErrorOutline,
            tint = MaterialTheme.colorScheme.error,
            contentDescription = "Error"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.please_call_operator),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        WrapContentButton(
            text = stringResource(R.string.call),
            onClick = {
                try {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_DIAL,
                            Uri.parse("tel:${(viewModel.selectedPoint.value as Status.Success).data.phoneNumber}")
                        )
                    )
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.an_error_occurred),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }
}