package com.example.doctor_app.business.repository

import com.example.doctor_app.business.models.Vaccine

interface VaccineRepository {
    suspend fun getVaccine(id: Long): Vaccine?
    suspend fun getAllVaccines(): List<Vaccine>
    suspend fun getVaccineByName(name: String): Vaccine?
    suspend fun insertVaccine(vaccine: Vaccine): Long?
    suspend fun deleteVaccine(vaccine: Vaccine)
}