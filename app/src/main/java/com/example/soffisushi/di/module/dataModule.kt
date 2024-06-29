package com.example.soffisushi.di.module

import android.content.Context
import android.content.SharedPreferences
import com.example.soffisushi.data.remote.retrofit.api.FrontpadApi
import com.example.soffisushi.data.remote.retrofit.util.Constants.Companion.BASE_URL
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "SoffiSushiSharedPreferences",
            Context.MODE_PRIVATE
        )
    }

    single<FrontpadApi> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FrontpadApi::class.java)
    }

    single<FirebaseFirestore> { Firebase.firestore }
}