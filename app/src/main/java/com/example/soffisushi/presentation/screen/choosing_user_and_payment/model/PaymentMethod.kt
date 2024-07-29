package com.example.soffisushi.presentation.screen.choosing_user_and_payment.model

import android.content.Context
import com.example.soffisushi.R

sealed class PaymentMethod(val name: String) {
    data class None(val context: Context) : PaymentMethod(context.getString(R.string.unselected))
    data class Terminal(val context: Context) : PaymentMethod(context.getString(R.string.card_qr))
    data class Cash(val context: Context) : PaymentMethod(context.getString(R.string.cash))
}