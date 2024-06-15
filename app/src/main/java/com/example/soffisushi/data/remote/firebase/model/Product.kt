package com.example.soffisushi.data.remote.firebase.model

data class Product(
    val id: List<Long> = listOf(-1),
    val categoryId: Long = 0,

    val singleProduct: Boolean = true,
    val name: String = "unknown",
    val choices: List<String> = listOf(""),
    val image: List<String> = listOf(""),
    val price: List<Int> = listOf(-1),
    val structure: String = "",

    val putGingerAndWasabi: Boolean = true,
    val putSticks: Boolean = true,

    val new: Boolean = false,
    val hot: Boolean = false,
    val stop: List<Boolean> = listOf(false)
)