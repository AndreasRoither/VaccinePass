package com.mobilehealthsports.vaccinepass.business.models

import androidx.room.PrimaryKey
import java.util.*

data class Vaccine(
    @PrimaryKey val uid: Int,
    val name: String,
    val company: String?,
    val indication: String?,
    val targetGroup: String?,
    val note: String?,
    val adjuvans: String?,
    val thiomersal: String?,
    val refreshRecommendation: String?,
    val refreshDate: Date?,
) {
    fun checkRefreshDate(): Boolean {
        return when (refreshDate) {
            null -> false
            else -> {
                return refreshDate.before(Date())
            }
        }
    }
}