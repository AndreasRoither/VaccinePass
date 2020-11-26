package com.mobilehealthsports.vaccinepass.ui.main

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.Job
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class UserViewModel : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    private var currentErrorCoroutine: Job? = null

    var vaccineLength : Int = 10

    private var _user = MutableLiveData(User("John", "Doe", "0 pos", 75, 180, LocalDate.of(1996,10,22)))
    var user: LiveData<User> = _user

    var vaccines = MutableList(vaccineLength) { Vaccine("Hepatitis C", LocalDate.of(2020,11,15), VaccineState.ACTIVE)}


    /*private var _userFirstName = MutableLiveData("John")
    var userFirstName: LiveData<String> = _userFirstName

    private var _userLastName = MutableLiveData("Doe")
    var userLastName: LiveData<String> = _userLastName

    private var _userBloodType = MutableLiveData("0 pos")
    var userBloodType : LiveData<String> = _userBloodType

    private var _userWeight = MutableLiveData(75)
    var userWeight: LiveData<Int> = _userWeight

    private var _userHeight = MutableLiveData(180)
    var userHeight: LiveData<Int> = _userHeight*/

    lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREF_KEY = "VACCINE"
        const val DEFAULT_COLOR = R.color.black
        const val ERROR_COLOR = R.color.error_red
        const val ERROR_DELAY = 3000L
    }
}

data class User(val firstName: String, val lastName: String, val bloodType: String, val weight: Int, val height: Int, val birthDate: LocalDate)


enum class VaccineState {
    ACTIVE,
    SCHEDULED,
    NOT_SCHEDULED,
    NOT_VACCINATED
}

data class Vaccine(val name: String, val date: LocalDate, val state: VaccineState)