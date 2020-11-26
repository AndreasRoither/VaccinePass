package com.mobilehealthsports.vaccinepass.application.injection

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mobilehealthsports.vaccinepass.presentation.services.messages.AppMessageService
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.AppNavigationService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.ui.main.MainViewModel
import com.mobilehealthsports.vaccinepass.ui.main.UserViewModel
import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel
import com.mobilehealthsports.vaccinepass.ui.testing.TestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object InjectionModules {
    fun getModules() = listOf(

        // AppModule
        module {
            single { get<Application>().getSharedPreferences("appPreferences", 0) }
            factory<Gson> { GsonBuilder().create() }
        },

        // Activity
        module {
            factory<NavigationService> { (activityOrFragment: Any) ->
                when (activityOrFragment) {
                    is Fragment -> AppNavigationService(activityOrFragment)
                    is FragmentActivity -> AppNavigationService(activityOrFragment)
                    else -> throw IllegalArgumentException("Can not provide NavigationService for class ${activityOrFragment::class.java}")
                }
            }

            // private val messageService: MessageService by inject { parametersOf(this) }
            // messageService.subscribeMessageRequests(viewModel.messageRequest)
            factory<MessageService> { (activityOrFragment: Any) ->
                when (activityOrFragment) {
                    is Fragment -> AppMessageService(activityOrFragment)
                    is FragmentActivity -> AppMessageService(activityOrFragment)
                    else -> throw IllegalArgumentException("Can't provide MessageService for class ${activityOrFragment::class.java}")
                }
            }
        },

        // ViewModel module
        module {
            viewModel { TestViewModel() }
            viewModel { PinViewModel() }
            viewModel { MainViewModel() }
            viewModel { UserViewModel() }
        }
    )
}