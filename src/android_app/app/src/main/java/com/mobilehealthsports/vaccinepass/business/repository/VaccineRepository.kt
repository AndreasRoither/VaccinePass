package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.models.Vaccine

interface VaccineRepository {
    suspend fun getVaccine(id: Long): Vaccine?
    suspend fun getAllVaccines(): List<Vaccine>
    suspend fun getVaccineByName(name: String): Vaccine?
    suspend fun insertVaccine(vaccine: Vaccine): Long?
    suspend fun deleteVaccine(vaccine: Vaccine)
}