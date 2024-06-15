package com.example.soffisushi.data.remote.firebase.model

data class Point(
    val address: String = "",
    val open: Boolean = false,
    val timeZone: String = "Europe/Samara",
    val startTime: Long = 0,
    val endTime: Long = 86400000,

    val secretApi: String = "",

    val mailSender: String? = null,
    val mailPassword: String? = null,
    val mailRecipient: String? = null,

    val phoneNumber: String = "",
    val vkUrl: String = ""
)
