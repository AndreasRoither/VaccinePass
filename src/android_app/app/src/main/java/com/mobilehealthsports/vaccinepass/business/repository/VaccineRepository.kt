package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.models.Vaccine

interface VaccineRepository {
    suspend fun getVaccine(id: Int): Vaccine?
    suspend fun getAllVaccines(): List<Vaccine>
    suspend fun getVaccineByName(name: String): Vaccine?
    suspend fun insertVaccine(vaccine: Vaccine)
    suspend fun deleteVaccine(vaccine: Vaccine)
}