package com.mobilehealthsports.vaccinepass.ui.user_creation

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.business.repository.UserRepository
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.ToastRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.MainRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.set
import com.mobilehealthsports.vaccinepass.util.ThemeColor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class UserCreationViewModel(
    val sharedPreferences: SharedPreferences,
    val userRepository: UserRepository
) : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    val navigationRequest = ServiceRequest<NavigationRequest>()
    var themeCallback: ((Int) -> Unit)? = null
    val permissionRequest = MutableLiveData<String>()

    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")
    val bloodType = MutableLiveData("")
    val weight = MutableLiveData("")
    val height = MutableLiveData("")

    private val _birthDate = MutableLiveData(LocalDate.now())
    val birthDate: LiveData<LocalDate> = _birthDate

    private val _color = MutableLiveData(ThemeColor.PURPLE)
    val color: LiveData<ThemeColor> = _color

    fun setThemeColor(color: ThemeColor) {
        sharedPreferences["selectedThemeColor"] = color.value
        _color.value = color

        when (color) {
            ThemeColor.PURPLE -> themeCallback?.invoke(R.style.VaccinePass_purple)
            ThemeColor.GREEN -> themeCallback?.invoke(R.style.VaccinePass_green)
            ThemeColor.ORANGE -> themeCallback?.invoke(R.style.VaccinePass_orange)
        }
    }

    fun setBirthDate(date: LocalDate?) {
        _birthDate.value = date
    }

    fun setPhoto() {
        permissionRequest.value = "camera"
    }

    fun finish() {

        viewModelScope.launch {
            val user = User(
                0,
                firstName.value,
                lastName.value,
                bloodType.value,
                convertToDateViaInstant(birthDate.value!!),
                weight.value?.toFloat(),
                height.value?.toFloat(),
                color.value!!.value
            )

            val id = userRepository.insertUser(user)

            id?.let {
                val newUser = userRepository.getUser(id)

                newUser?.let {
                    navigationRequest.request(MainRequest(it))
                }

                if (newUser == null) {
                    messageRequest.request(ToastRequest("New user null"))
                }
            }

            if (id == null) {
                messageRequest.request(ToastRequest("Id null. Could not insert user"))
            }
        }
    }

    fun convertToDateViaInstant(dateToConvert: LocalDate): Date? {
        return Date.from(
            dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()
        )
    }
}