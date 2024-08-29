package com.example.soffisushi.di.module

import com.example.soffisushi.presentation.viewmodel.AdminViewModel
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AdminViewModel(
            firestore = get()
        )
    }
    viewModel {
        SoffiSushiViewModel(
            sharedPreferences = get(),
            firestore = get(),
            frontpadApi = get()
        )
    }
}