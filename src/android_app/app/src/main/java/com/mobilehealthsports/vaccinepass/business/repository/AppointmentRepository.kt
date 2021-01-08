package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.models.Appointment


interface AppointmentRepository {
    suspend fun getAppointment(id: Long): Appointment?
    suspend fun getAllAppointments(): List<Appointment>
    suspend fun insertAppointment(appointment: Appointment): Long?
    suspend fun deleteAppointment(appointment: Appointment)
    suspend fun deleteAllAppointments()
}