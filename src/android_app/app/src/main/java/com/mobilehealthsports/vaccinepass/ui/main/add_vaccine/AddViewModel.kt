package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.ui.main.user.VaccineState
import kotlinx.coroutines.Job
import java.time.LocalDate
import java.time.temporal.ChronoUnit


data class ScheduleItem(val nextEvent: LocalDate) {
    val daysTo: String
    get()  {
        return String.format("in %s days", ChronoUnit.DAYS.between(LocalDate.now(), nextEvent).toString())
    }
}

class AddViewModel : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    private var currentErrorCoroutine: Job? = null

    lateinit var sharedPreferences: SharedPreferences

    private var _vaccineName = MutableLiveData("Hepatitis C")
    var vaccineName: LiveData<String> = _vaccineName

    private var _vaccineDate = MutableLiveData(LocalDate.now())
    var vaccineDate: LiveData<LocalDate> = _vaccineDate

    private var _vaccineState = MutableLiveData(VaccineState.NOT_SCHEDULED)
    var vaccineState: LiveData<VaccineState> = _vaccineState

    var scheduleItems: MutableList<ScheduleItem> = ArrayList()

    fun addScheduleItem(item: ScheduleItem) {
        scheduleItems.add(item)
    }

    fun setVaccineDate(date: LocalDate) {
        _vaccineDate.value = date
    }

    companion object {
        const val SHARED_PREF_KEY = "ADDVACCINE"
        const val DEFAULT_COLOR = R.color.black
        const val ERROR_COLOR = R.color.error_red
        const val ERROR_DELAY = 3000L
    }
}