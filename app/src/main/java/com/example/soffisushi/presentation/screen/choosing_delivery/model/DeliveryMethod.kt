package com.example.soffisushi.presentation.screen.choosing_delivery.model

import android.content.Context
import com.example.soffisushi.R

sealed class DeliveryMethod(val name: String) {
    data class Pickup(val context: Context) : DeliveryMethod(context.getString(R.string.pickup))
    data class Delivery(val context: Context) : DeliveryMethod(context.getString(R.string.delivery))
}