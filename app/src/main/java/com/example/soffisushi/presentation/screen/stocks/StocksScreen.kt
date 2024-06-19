package com.example.soffisushi.presentation.screen.stocks

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.presentation.component.CircularProgressBox
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.screen.stocks.component.StockRow
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.Status

@Composable
fun StocksScreen(modifier: Modifier, viewModel: SoffiSushiViewModel) {
    val resultStocks = remember { viewModel.stocks }

    when (resultStocks.value) {
        is Status.Pending -> {
            if (viewModel.selectedPoint.value is Status.Success) {
                viewModel.getStocks()
            } else {
                CircularProgressBox(modifier = modifier)
            }
        }

        is Status.Loading -> {
            CircularProgressBox(modifier = modifier)
        }

        is Status.Error -> {
            val exception = (resultStocks.value as Status.Error).exception
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
                        viewModel.getStocks()
                    }
                )
            }
        }

        is Status.Success -> {
            val stocks = (resultStocks.value as Status.Success).data.sortedBy { it.day }

            if (stocks.isNotEmpty()) {
                val everydayStocks = stocks.filter { it.day == null }
                val weekdayStocks = stocks.filter { it.day != null }

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (weekdayStocks.isNotEmpty()) {
                        Text(text = stringResource(R.string.valid_by_the_day))
                        weekdayStocks.forEach {
                            StockRow(stock = it)
                        }
                    }
                    if (everydayStocks.isNotEmpty()) {
                        Text(text = stringResource(R.string.valid_everyday))
                        everydayStocks.forEach {
                            StockRow(stock = it)
                        }
                    }
                }
            } else {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = stringResource(R.string.no_shares),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
