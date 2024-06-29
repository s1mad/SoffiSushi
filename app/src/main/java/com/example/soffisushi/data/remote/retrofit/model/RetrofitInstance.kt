package com.example.soffisushi.data.remote.retrofit.model

import com.example.soffisushi.data.remote.retrofit.api.FrontpadApi
import com.example.soffisushi.data.remote.retrofit.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val instance: FrontpadApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FrontpadApi::class.java)
    }
}