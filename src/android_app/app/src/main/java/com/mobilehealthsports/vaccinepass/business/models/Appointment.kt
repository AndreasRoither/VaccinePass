package com.mobilehealthsports.vaccinepass.business.models

import java.time.LocalDate

data class Appointment (
    val uid: Long,
    val title: String?,
    val place: String?,
    val appointment_date: LocalDate?,
    val reminder: Boolean,
    val reminder_date: LocalDate?
    )