package com.mobilehealthsports.vaccinepass.ui.main.add_calendar_entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.mobilehealthsports.vaccinepass.business.models.Appointment
import com.mobilehealthsports.vaccinepass.business.models.Reminder
import com.mobilehealthsports.vaccinepass.business.repository.AppointmentRepository
import com.mobilehealthsports.vaccinepass.business.repository.ReminderRepository
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.livedata.NonNullMutableLiveData
import com.mobilehealthsports.vaccinepass.util.livedata.combine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class CalendarEntryViewModel(private val appointmentRepository: AppointmentRepository, private val reminderRepository: ReminderRepository) : BaseViewModel() {

    val errorText = NonNullMutableLiveData("")
    val errorTextValidity = NonNullMutableLiveData("")

    val title = NonNullMutableLiveData("")
    val place = NonNullMutableLiveData("")

    val setReminder = NonNullMutableLiveData(false)
    val cancelAdd = NonNullMutableLiveData(false)
    val addAppointment = NonNullMutableLiveData(false)
    val addReminder: MutableLiveData<Reminder> = MutableLiveData(null)

    val finishBtnEnabled: LiveData<Boolean> = Transformations.map(title.combine(place)) {
        checkTextFields()
    }

    var appointmentDate = NonNullMutableLiveData(LocalDate.now())
    var reminderDate = NonNullMutableLiveData(LocalDate.now())

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
        val date = if (setReminder.value) reminderDate.value else null
        val dateTime = date?.atTime(12, 0)

        viewModelScope.launch(Dispatchers.IO) {
            val appointment = Appointment(0, title.value, place.value, appointmentDate.value, setReminder.value, dateTime)
            appointmentRepository.insertAppointment(appointment)

            dateTime?.let {
                val reminder = Reminder(0, it, title.value, place.value)
                reminderRepository.insertReminder(reminder)?.let { id ->
                    addReminder.postValue(reminderRepository.getReminder(id))
                }
            }
        }
        addAppointment.value = true
    }

    private fun checkTextFields(): Boolean {
        return title.value.isNotBlank() && place.value.isNotBlank()
    }

    private fun sanitizeString(str: String): String {
        return str.replace("\\", "")
            .replace(";", "")
            .replace("%", "")
            .replace("\"", "")
            .replace("\'", "")
            .replace("*", "")
    }
}
