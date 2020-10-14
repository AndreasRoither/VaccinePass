package com.mobilehealthsports.vaccinepass.application.injection

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

object InjectionModules {
    fun getModules() = listOf(

        // AppModule
        module {
            single { get<Application>().getSharedPreferences("appPreferences", 0) }
            factory<Gson> { GsonBuilder().create() }
        },

        // ViewModel module
        module {
            // androidx.lifecycle.viewmodel
            // viewModel { My´ViewModel(get(), get())}
        }
    )
}