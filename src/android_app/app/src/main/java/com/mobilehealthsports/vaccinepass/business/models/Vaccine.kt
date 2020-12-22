package com.mobilehealthsports.vaccinepass.business.models

import androidx.room.PrimaryKey
import java.util.*

data class Vaccine(
    val uid: Long = 0,
    val name: String = "",
    val company: String? = "",
    val indication: String? = "",
    val targetGroup: String? = "",
    val note: String? = "",
    val adjuvans: String? = "",
    val thiomersal: String? = "",
    val refreshRecommendation: String? = "",
    val active: Boolean = false
) {
}