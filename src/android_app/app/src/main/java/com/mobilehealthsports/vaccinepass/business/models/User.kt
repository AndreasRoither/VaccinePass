package com.mobilehealthsports.vaccinepass.business.models

import android.content.Context
import androidx.core.content.ContextCompat
import java.io.Serializable
import java.util.*

data class User(
        val uid: Int,
        val firstName: String?,
        val lastName: String?,
        val bloodType: String?,
        val birthDay: Date?,
        val weight: Float?,
        val height: Float?,
        val themeColor: Int,
) : Serializable {
    fun resolveColor(context: Context): Int {
        return ContextCompat.getColor(context, themeColor)
    }
}
