package com.mobilehealthsports.vaccinepass.business.models

import java.time.LocalDate

data class Vaccination(
        val uid: Int,
        val f_uid: Int,
        val active: Boolean,
        val refreshDate: LocalDate?,
        val vaccinationDate: LocalDate?,
) {
    fun refreshDateInPast(): Boolean {
        return when (refreshDate) {
            null -> false
            else -> {
                return refreshDate.isBefore( LocalDate.now() )
            }
        }
    }
}