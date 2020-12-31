package com.mobilehealthsports.vaccinepass.business.models

import java.time.LocalDate

data class Reminder (
    val uid: Long,
    val reminderDate: LocalDate,
    val vaccination_uid: Long
)