package com.mobilehealthsports.vaccinepass.application

import android.app.Application
import com.mobilehealthsports.vaccinepass.BuildConfig
import com.mobilehealthsports.vaccinepass.application.injection.InjectionModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Vaccine pass application
 * Plant timber tree for debug
 * Start koin and load modules
 */
class VaccinePassApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) androidLogger()
            androidContext(this@VaccinePassApplication)
            modules(InjectionModules.getModules())
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}