package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.database.AppDatabase
import com.mobilehealthsports.vaccinepass.business.database.extension.toAppointment
import com.mobilehealthsports.vaccinepass.business.database.extension.toDb
import com.mobilehealthsports.vaccinepass.business.models.Appointment

class AppointmentRepositoryImpl(private val database: AppDatabase) : AppointmentRepository {
    override suspend fun getAppointment(id: Long): Appointment? {
        return database.appointmentDao().loadAllByIds(longArrayOf(id)).blockingFirst()
            .firstOrNull()?.toAppointment()
    }

    override suspend fun getAllAppointments(): List<Appointment> {
        return database.appointmentDao().getAll().blockingFirst().map { it.toAppointment() }
    }

    override suspend fun insertAppointment(appointment: Appointment): Long? {
        return database.appointmentDao().insertAll(appointment.toDb()).firstOrNull()
    }

    override suspend fun deleteAppointment(appointment: Appointment) {
        database.appointmentDao().delete(appointment.toDb())
    }

    override suspend fun deleteAllAppointments() {
        database.appointmentDao().deleteAll()
    }
}