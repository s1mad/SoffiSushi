package com.example.soffisushi.util

import com.example.soffisushi.data.remote.firebase.model.Delivery

data class Address(
    val city: Delivery? = null,
    val street: String = "",
    val home: String = "",
    val pod: String = "",
    val et: String = "",
    val apart: String = "",
) {
    fun getStreetHome(): String = "$street, $home"
    fun toStringAddress(): String = "${city?.name}, $street, $home ${
        if (pod.isNotBlank()) {
            ", подъезд: $pod"
        } else {
            ""
        } +
                if (et.isNotBlank()) {
                    ", этаж: $et"
                } else {
                    ""
                } +
                if (apart.isNotBlank()) {
                    ", этаж: $apart"
                } else {
                    ""
                }
    }"
}
