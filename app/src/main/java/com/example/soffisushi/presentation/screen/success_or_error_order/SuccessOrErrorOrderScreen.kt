package com.example.soffisushi.presentation.screen.success_or_error_order

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.soffisushi.R
import com.example.soffisushi.presentation.component.CircularProgressBox
import com.example.soffisushi.presentation.screen.success_or_error_order.component.ErrorOrder
import com.example.soffisushi.presentation.screen.success_or_error_order.component.MayProblemOrder
import com.example.soffisushi.presentation.screen.success_or_error_order.component.SuccessOrder
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@Composable
fun SuccessOrErrorOrderScreen(
    viewModel: SoffiSushiViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    when (val resultOrder = viewModel.newOrderResult.value) {
        is Status.Pending -> {
            viewModel.setNewOrderResult(
                Status.Error(
                    Exception(stringResource(R.string.unknown_error))
                )
            )
        }

        is Status.Loading -> {
            CircularProgressBox(modifier = modifier)
        }

        is Status.Error -> {
            when (viewModel.mailAboutNewOrder.value) {
                is Status.Loading -> {
                    CircularProgressBox(modifier = modifier)
                }

                is Status.Pending, is Status.Error -> {
                    ErrorOrder(
                        modifier = modifier,
                        viewModel = viewModel,
                        exception = resultOrder.exception
                    )
                }

                is Status.Success -> {
                    SuccessOrder(
                        modifier = modifier,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }

        is Status.Success -> {
            when (viewModel.mailAboutNewOrder.value) {
                is Status.Loading -> {
                    CircularProgressBox(modifier = modifier)
                }

                is Status.Pending, is Status.Error -> {
                    MayProblemOrder(
                        modifier = modifier,
                        viewModel = viewModel
                    )
                }

                is Status.Success -> {
                    SuccessOrder(
                        modifier = modifier,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}