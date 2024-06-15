package com.example.soffisushi.util

data class User(
    val name: String = "",
    val phoneNumber: String = ""
) {
    fun getNameNumber() = name + (if (name.isBlank()) "" else ", ") + phoneNumber
}
