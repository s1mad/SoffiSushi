package com.example.soffisushi.util

data class UserInfo(
    val user: User = User(),
    val savedUser: User? = null,
    val showLoadUser: Boolean = true
)
