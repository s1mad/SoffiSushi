package com.example.soffisushi.presentation.screen.stocks.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.data.remote.firebase.model.Stock
import java.util.Calendar

@Composable
fun StockRow(
    stock: Stock
) {
    val weekDay = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> 1
        Calendar.TUESDAY -> 2
        Calendar.WEDNESDAY -> 3
        Calendar.THURSDAY -> 4
        Calendar.FRIDAY -> 5
        Calendar.SATURDAY -> 6
        Calendar.SUNDAY -> 7
        else -> null

    }
    val primaryContainerColor = if (weekDay == stock.day) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val onPrimaryContainerColor = if (weekDay == stock.day) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(primaryContainerColor)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val day = getDayOfWeekText(stock.day)
        if (day != null) {
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = day, color = onPrimaryContainerColor
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            textAlign = if (stock.day == null) TextAlign.Start else TextAlign.End,
            text = stock.text,
            color = onPrimaryContainerColor
        )
    }
}


@Composable
private fun getDayOfWeekText(dayOfWeek: Int?): String? {
    return when (dayOfWeek) {
        1 -> stringResource(R.string.monday)
        2 -> stringResource(R.string.tuesday)
        3 -> stringResource(R.string.wednesday)
        4 -> stringResource(R.string.thursday)
        5 -> stringResource(R.string.friday)
        6 -> stringResource(R.string.saturday)
        7 -> stringResource(R.string.sunday)
        null -> null
        else -> stringResource(R.string.unknown_day)
    }
}