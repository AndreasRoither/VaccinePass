package com.mobilehealthsports.vaccinepass.business.models

import java.util.*

data class Vaccination(
    val uid: Int,
    val f_uid: Int,
    val active: Boolean,
    val refreshDate: Date?,
    val vaccinationDate: Date?,
) {
    fun refreshDateInPast(): Boolean {
        return when (refreshDate) {
            null -> false
            else -> {
                return refreshDate.before(Date())
            }
        }
    }
}