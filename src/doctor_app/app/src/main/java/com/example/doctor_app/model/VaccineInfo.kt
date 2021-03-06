package com.example.doctor_app.model

data class VaccineInfo(
    val userId: String,
    val productName: String,
    val vaccinationDate: String,
    val expiresIn: String,
    val doctorId: String,
    val doctorName: String
)