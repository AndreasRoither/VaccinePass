package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.database.AppDatabase
import com.mobilehealthsports.vaccinepass.business.database.extension.toDb
import com.mobilehealthsports.vaccinepass.business.database.extension.toVaccination
import com.mobilehealthsports.vaccinepass.business.models.Vaccination

class VaccinationRepositoryImpl(private val database: AppDatabase) : VaccinationRepository {
    override suspend fun getVaccination(id: Int): Vaccination? {
        return database.userVaccinationDao().loadAllByIds(intArrayOf(id)).blockingFirst()
            .firstOrNull()?.toVaccination()

    }

    override suspend fun getAllVaccinations(): List<Vaccination> {
        return database.userVaccinationDao().getAll().blockingFirst().map { it.toVaccination() }
    }

    override suspend fun getAllActiveVaccinations(): List<Vaccination> {
        return database.userVaccinationDao().getAllActive().blockingFirst()
            .map { it.toVaccination() }
    }

    override suspend fun insertVaccination(vaccination: Vaccination) {
        database.userVaccinationDao().insertAll(vaccination.toDb())
    }

    override suspend fun deleteVaccination(vaccination: Vaccination) {
        database.userVaccinationDao().delete(vaccination.toDb())
    }
}