package com.mobilehealthsports.vaccinepass.business.models

import java.time.LocalDate
import java.time.LocalDateTime

data class Appointment(
    val uid: Long,
    val title: String?,
    val place: String?,
    val appointment_date: LocalDate?,
    val reminder: Boolean,
    val reminder_date: LocalDateTime?
)