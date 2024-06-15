package com.example.soffisushi.util

import com.example.soffisushi.data.remote.firebase.model.Delivery

data class DeliveryInfo(
    val cities: Status<List<Delivery>> = Status.Pending,
    val isDelivery: Boolean = true,
    val address: Address = Address(),
    val savedAddress: Address? = null,
    val showLoadAddress: Boolean = true
)
