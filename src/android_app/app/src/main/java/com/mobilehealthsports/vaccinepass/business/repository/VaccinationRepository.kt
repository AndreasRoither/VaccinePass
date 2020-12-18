package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.models.Vaccination

interface VaccinationRepository {
    suspend fun getVaccination(id: Long): Vaccination?
    suspend fun getAllVaccinations(): List<Vaccination>
    suspend fun getAllActiveVaccinations(): List<Vaccination>?
    suspend fun insertVaccination(vaccination: Vaccination): Long?
    suspend fun deleteVaccination(vaccination: Vaccination)
}