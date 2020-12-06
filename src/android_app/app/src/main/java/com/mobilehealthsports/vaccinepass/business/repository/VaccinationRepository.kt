package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.models.Vaccination

interface VaccinationRepository {
    suspend fun getVaccination(id: Int): Vaccination?
    suspend fun getAllVaccinations(): List<Vaccination>
    suspend fun getAllActiveVaccinations(): List<Vaccination>?
    suspend fun insertVaccination(vaccination: Vaccination)
    suspend fun deleteVaccination(vaccination: Vaccination)
}