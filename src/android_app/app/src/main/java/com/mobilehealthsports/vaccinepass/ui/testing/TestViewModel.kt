package com.mobilehealthsports.vaccinepass.ui.testing

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.*
import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel
import com.mobilehealthsports.vaccinepass.ui.start.StartActivity
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.set

class TestViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val _navigationRequest = ServiceRequest<NavigationRequest>()
    val navigationRequest = _navigationRequest

    fun startInitActivity() {
        navigationRequest.request(InitActivityRequest(false))
        sharedPreferences[StartActivity.LAST_USER_ID_PREF] = -1L
    }

    fun startPinActivityInitial() {
        navigationRequest.request(PinRequest(PinViewModel.PinState.INITIAL, 4))
    }

    fun startPinActivityCheck() {
        navigationRequest.request(PinRequest(PinViewModel.PinState.CHECK, 4))
    }

    fun startCalendarFragment() {
        navigationRequest.request(
            FragmentTestRequest("CalendarFragment")
        )
    }

    fun startMainActivity() {
        navigationRequest.request(MainRequest)
    }

    fun startSelectUserActivity() {
        navigationRequest.request(SelectUserRequest)
    }

    fun startUserCreationFragment() {
        navigationRequest.request(UserCreationRequest())
    }

    fun addActiveVaccination() {
        navigationRequest.request(
            FragmentTestRequest("AddVaccination")
        )
    }
}
