package com.mobilehealthsports.vaccinepass.application.injection

import org.koin.dsl.module

object InjectionModules {
    fun getModules() = listOf(
        module {
            // androidx.lifecycle.viewmodel
            // viewModel { My´ViewModel(get(), get())}
        }
    )
}