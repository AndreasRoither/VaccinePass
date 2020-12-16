package com.mobilehealthsports.vaccinepass.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.get

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPrefs = this.getSharedPreferences("appPreferences", MODE_PRIVATE)
        val color = ThemeColor.fromInt(sharedPrefs["selectedThemeColor", ThemeColor.PURPLE.value]!!)

        // set theme color
        when (color) {
            ThemeColor.PURPLE -> setTheme(R.style.VaccinePass_purple)
            ThemeColor.GREEN -> setTheme(R.style.VaccinePass_green)
            ThemeColor.ORANGE -> setTheme(R.style.VaccinePass_orange)
            null -> setTheme(R.style.VaccinePass_purple)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        // TODO: recreate if color is not the same
    }
}