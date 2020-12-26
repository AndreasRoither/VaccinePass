package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine

data class ReceivedVaccineInfo(
    val userId: String,
    val productName: String,
    val vaccinationDate: String,
    val expiresIn: String,
    val doctorId: String,
    val doctorName: String
)
