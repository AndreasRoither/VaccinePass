package com.mobilehealthsports.vaccinepass.ui.main.add_calendar_entry

import androidx.lifecycle.viewModelScope
import com.mobilehealthsports.vaccinepass.business.models.Appointment
import com.mobilehealthsports.vaccinepass.business.repository.AppointmentRepository
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.NonNullMutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarEntryViewModel(val appointmentRepository: AppointmentRepository) : BaseViewModel() {

    val errorText = NonNullMutableLiveData("")
    val errorTextValidity = NonNullMutableLiveData("")

    val title = NonNullMutableLiveData("")
    val place = NonNullMutableLiveData("")

    val setReminder = NonNullMutableLiveData(false)
    val cancelAdd = NonNullMutableLiveData(false)
    val addAppointment = NonNullMutableLiveData(false)

    var appointmentDate = NonNullMutableLiveData(LocalDate.now())

    private val regex = "[*/\\\'%;\"]+".toRegex()

    fun errorTextString(str: String): String {
        return when {
            str.isBlank() -> errorText.value
            regex.containsMatchIn(str) -> errorTextValidity.value
            else -> ""
        }
    }

    fun cancel() {
        cancelAdd.value = true
    }

    fun finish() {
        val appointment: Appointment =
            Appointment(0, title.value, place.value, appointmentDate.value, setReminder.value)
        viewModelScope.launch(Dispatchers.IO) {
            appointmentRepository.insertAppointment(appointment)
        }
        addAppointment.value = true
    }
}