package com.example.soffisushi.util

sealed class Status<out T> {
    data object Pending : Status<Nothing>()
    data object Loading : Status<Nothing>()
    data class Success<out T>(val data: T) : Status<T>()
    data class Error(val exception: Exception) : Status<Nothing>()
}