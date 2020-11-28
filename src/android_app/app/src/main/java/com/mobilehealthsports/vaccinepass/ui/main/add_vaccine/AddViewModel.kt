package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.ui.main.user.Vaccine
import com.mobilehealthsports.vaccinepass.ui.main.user.VaccineState
import kotlinx.coroutines.Job
import java.time.LocalDate

class AddViewModel : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    private var currentErrorCoroutine: Job? = null

    lateinit var sharedPreferences: SharedPreferences

    private var _vaccine = MutableLiveData(Vaccine("Hepatitis C", LocalDate.now(), VaccineState.NOT_SCHEDULED))
    var vaccine: LiveData<Vaccine> = _vaccine

    companion object {
        const val SHARED_PREF_KEY = "VACCINE"
        const val DEFAULT_COLOR = R.color.black
        const val ERROR_COLOR = R.color.error_red
        const val ERROR_DELAY = 3000L
    }
}