package com.mobilehealthsports.vaccinepass.ui.testing

import androidx.lifecycle.ViewModel
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.*
import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel
import java.time.LocalDate

class TestViewModel : ViewModel() {

    private val _navigationRequest = ServiceRequest<NavigationRequest>()
    val navigationRequest = _navigationRequest

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
        navigationRequest.request(MainRequest(User(0,"John", "Doe", "A neg", LocalDate.of(1999,10,25), 80f, 180f, 0)))
    }

    fun startSelectUserActivity() {
        navigationRequest.request(SelectUserRequest)
    }
}