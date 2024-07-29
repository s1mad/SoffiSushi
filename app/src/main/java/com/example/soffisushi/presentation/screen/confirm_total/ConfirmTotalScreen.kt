package com.example.soffisushi.presentation.screen.confirm_total

import android.widget.Toast
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
import com.example.soffisushi.presentation.screen.confirm_total.component.ConfirmTotalItem
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@Composable
fun ConfirmTotalScreen(
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
        ConfirmTotalItem(viewModel = viewModel)
        FillMaxWidthButton(
            textEnabled = stringResource(R.string.confirm),
            textDisabled = stringResource(R.string.we_close),
            enabled = viewModel.pointIsOpen.value,
            onClick = {
                if (!viewModel.pointIsOpen.value) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.we_close),
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (viewModel.selectedPoint.value is Status.Success) {
                    viewModel.postNewOrder()
                    navController.navigate(Screen.CartSuccessOrErrorOrder.route) {
                        popUpTo(Screen.Cart.route) { inclusive = true }
                    }
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
}