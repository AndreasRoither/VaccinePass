package com.mobilehealthsports.vaccinepass.util

import android.content.SharedPreferences
import com.mobilehealthsports.vaccinepass.business.repository.AppointmentRepository
import com.mobilehealthsports.vaccinepass.business.repository.ReminderRepository
import com.mobilehealthsports.vaccinepass.business.repository.UserRepository
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepository
import com.mobilehealthsports.vaccinepass.ui.start.StartActivity
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.set

object ResetHelper {
    suspend fun resetApp(sharedPreferences: SharedPreferences, appointmentRepository: AppointmentRepository, reminderRepository: ReminderRepository, userRepository: UserRepository, vaccinationRepository: VaccinationRepository) {
        appointmentRepository.deleteAllAppointments()
        reminderRepository.deleteAllReminder()
        userRepository.deleteAllUser()
        vaccinationRepository.deleteAllVaccinations()
        sharedPreferences[StartActivity.LAST_USER_ID_PREF] = -1L
    }
}
