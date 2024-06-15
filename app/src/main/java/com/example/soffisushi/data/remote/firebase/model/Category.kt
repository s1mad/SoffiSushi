package com.example.soffisushi.data.remote.firebase.model

data class Category(
    val id: Long = 0,
    val name: String = "",
    val image: String = "",
    val parentId: Long? = null
)
