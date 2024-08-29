package com.example.soffisushi.presentation.screen.about

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.soffisushi.R
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.component.CircularProgressBox
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.component.point_address.ChoosingPointDialog
import com.example.soffisushi.presentation.screen.about.component.AboutPhoneNumberRow
import com.example.soffisushi.presentation.screen.about.component.AboutRow
import com.example.soffisushi.presentation.screen.about.component.AboutVkRow
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@Composable
fun AboutScreen(
    viewModel: SoffiSushiViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    val resultPoint = remember { viewModel.selectedPoint }

    when (resultPoint.value) {
        is Status.Pending, Status.Loading -> {
            CircularProgressBox()
        }

        is Status.Error -> {
            val exception = (resultPoint.value as Status.Error).exception
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = exception.message.toString(),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
                WrapContentButton(
                    text = stringResource(R.string.try_again),
                    onClick = {
                        with(viewModel) {
                            loadPointDocumentId(onComplete = {
                                setPointListener(onComplete = {
                                    setProductsListener()
                                    pointOpenChecker()
                                })
                            })
                        }
                    }
                )
            }
        }

        is Status.Success -> {
            val point = (resultPoint.value as Status.Success).data
            val context = LocalContext.current
            val showChoosePointDialog = rememberSaveable { mutableStateOf(false) }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AboutRow(
                    text = stringResource(R.string.point, point.address),
                    onClick = {
                        showChoosePointDialog.value = true
                    }
                )
                AboutRow(
                    text = stringResource(R.string.term_use),
                    onClick = {
                        navController.navigate(Screen.UserAgreement.route)
                    }
                )
                AboutPhoneNumberRow(
                    text = point.phoneNumber.take(2) + " " +
                            point.phoneNumber.drop(2).take(3) + " " +
                            point.phoneNumber.drop(5).take(3) + "-" +
                            point.phoneNumber.dropLast(2).takeLast(2) + "-" +
                            point.phoneNumber.takeLast(2),
                    onClick = {
                        try {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_DIAL,
                                    Uri.parse("tel:${point.phoneNumber}")
                                )
                            )
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.an_error_occurred),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
                AboutVkRow(
                    imageVector = painterResource(R.drawable.ic_vk_logo),
                    onClick = {
                        try {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(point.vkUrl)
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

            if (showChoosePointDialog.value) {
                ChoosingPointDialog(
                    viewModel = viewModel,
                    hideDialog = {
                        showChoosePointDialog.value = false
                    }
                )
            }
        }
    }
}