package com.mobilehealthsports.vaccinepass.business.models

import java.time.LocalDateTime

data class Reminder(
    val uid: Long,
    val reminderDate: LocalDateTime,
    val title: String,
    val place: String
)