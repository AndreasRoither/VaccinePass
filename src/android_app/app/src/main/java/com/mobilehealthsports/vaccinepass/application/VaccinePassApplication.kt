package com.mobilehealthsports.vaccinepass.application

import android.app.Application
import com.mobilehealthsports.vaccinepass.BuildConfig
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.application.injection.InjectionModules
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.get
import com.mobilehealthsports.vaccinepass.util.ThemeColor
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

/**
 * Vaccine pass application
 * Plant timber tree for debug
 * Start koin and load modules
 */
class VaccinePassApplication : Application() {

    override fun onCreate() {
        val sharedPrefs = this.getSharedPreferences("appPreferences", MODE_PRIVATE)
        val color = ThemeColor.fromInt(sharedPrefs[PreferenceHelper.THEME_COLOR, ThemeColor.PURPLE.value]!!)

        // set theme color
        when (color) {
            ThemeColor.PURPLE -> setTheme(R.style.VaccinePass_purple)
            ThemeColor.GREEN -> setTheme(R.style.VaccinePass_green)
            ThemeColor.ORANGE -> setTheme(R.style.VaccinePass_orange)
            null -> setTheme(R.style.VaccinePass_purple)
        }

        super.onCreate()

        startKoin {
            androidLogger()
            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)
            androidContext(this@VaccinePassApplication)
            modules(InjectionModules.getModules())
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}