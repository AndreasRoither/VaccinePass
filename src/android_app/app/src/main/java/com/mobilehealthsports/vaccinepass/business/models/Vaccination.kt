package com.mobilehealthsports.vaccinepass.business.models

import java.time.LocalDate

data class Vaccination(
        val uid: Long,
        val f_uid: Long,
        val active: Boolean,
        val refreshDate: LocalDate?,
        val userId: String,
        val vaccinationDate: LocalDate?,
        val expiresIn: String,
        val doctorId: String,
        val doctorName: String,
        val signature: String,

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