package com.mobilehealthsports.vaccinepass.application.injection

import android.app.Application
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mobilehealthsports.vaccinepass.business.database.AppDatabase
import com.mobilehealthsports.vaccinepass.business.repository.*
import com.mobilehealthsports.vaccinepass.presentation.services.messages.AppMessageService
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.AppNavigationService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import com.mobilehealthsports.vaccinepass.ui.main.MainViewModel
import com.mobilehealthsports.vaccinepass.ui.main.add_vaccine.AddViewModel
import com.mobilehealthsports.vaccinepass.ui.main.calendar.CalendarViewModel
import com.mobilehealthsports.vaccinepass.ui.main.settings.SettingsViewModel
import com.mobilehealthsports.vaccinepass.ui.main.user.UserViewModel
import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel
import com.mobilehealthsports.vaccinepass.ui.testing.TestViewModel
import com.mobilehealthsports.vaccinepass.ui.user_creation.UserCreationViewModel
import com.mobilehealthsports.vaccinepass.ui.user_select.SelectUserViewModel
import com.mobilehealthsports.vaccinepass.ui.vaccination.VaccinationViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object InjectionModules {
    fun getModules() = listOf(

        // AppModule
        module {
            single<SharedPreferences> { get<Application>().getSharedPreferences("appPreferences", 0) }
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

        // database module
        module {
            single { AppDatabase.getDatabase(androidApplication()) }
            single<UserRepository> { UserRepositoryImpl(get()) }
            single<VaccinationRepository> { VaccinationRepositoryImpl(get()) }
            single<VaccineRepository> { VaccineRepositoryImpl(get()) }
        },

        // ViewModel module
        module {
            viewModel { AddViewModel() }
            viewModel { CalendarViewModel(get()) }
            viewModel { TestViewModel() }
            viewModel { MainViewModel() }
            viewModel { PinViewModel() }
            viewModel { SelectUserViewModel(get(), get()) }
            viewModel { SettingsViewModel() }
            viewModel { UserCreationViewModel(get(), get()) }
            viewModel { UserViewModel(get(), get()) }
            viewModel { VaccinationViewModel(get(), get()) }
        }
    )
}