package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.database.AppDatabase
import com.mobilehealthsports.vaccinepass.business.database.extension.toDb
import com.mobilehealthsports.vaccinepass.business.database.extension.toVaccine
import com.mobilehealthsports.vaccinepass.business.models.Vaccine

class VaccineRepositoryImpl(private val database: AppDatabase): VaccineRepository {
    override suspend fun getVaccine(id: Int): Vaccine? {
        return database.vaccineDao().loadAllByIds(intArrayOf(id)).blockingFirst().firstOrNull()?.toVaccine()
    }

    override suspend fun getAllVaccines(): List<Vaccine> {
        return database.vaccineDao().getAll().blockingFirst().map { it.toVaccine() }
    }

    override suspend fun getVaccineByName(name: String): Vaccine? {
        return database.vaccineDao().findByName(name).blockingGet()?.toVaccine()
    }

    override suspend fun insertVaccine(vaccine: Vaccine) {
        database.vaccineDao().insertAll(vaccine.toDb())
    }

    override suspend fun deleteVaccine(vaccine: Vaccine) {
        database.vaccineDao().delete(vaccine.toDb())
    }
}