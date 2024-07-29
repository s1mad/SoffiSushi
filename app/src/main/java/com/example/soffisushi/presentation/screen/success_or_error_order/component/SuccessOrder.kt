package com.example.soffisushi.presentation.screen.success_or_error_order.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.soffisushi.R
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.ui.theme.success
import com.example.soffisushi.util.Status

@Composable
fun SuccessOrder(
    modifier: Modifier,
    viewModel: SoffiSushiViewModel,
    navController: NavHostController
) {
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
            imageVector = Icons.Rounded.CheckCircleOutline,
            tint = MaterialTheme.colorScheme.success,
            contentDescription = "Success"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(
                R.string.the_order_has_been_successfully_created_we_can_call_you,
                (viewModel.selectedPoint.value as Status.Success).data.phoneNumber
            ),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        WrapContentButton(
            text = stringResource(R.string.go_to_home),
            onClick = {
                navController.navigate(Screen.Products.route)
            }
        )
    }
}