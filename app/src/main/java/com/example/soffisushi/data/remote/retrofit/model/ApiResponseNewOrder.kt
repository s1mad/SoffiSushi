package com.example.soffisushi.data.remote.retrofit.model

import com.google.gson.annotations.SerializedName

data class ApiResponseNewOrder(
    val result: String,
    @SerializedName("order_id")
    val orderId: String?,
    @SerializedName("order_number")
    val orderNumber: String?,
    val error: String?,
    val warnings: Map<String, Any>?
)
