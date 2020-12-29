package com.mobilehealthsports.vaccinepass.ui.testing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.business.models.Vaccination
import com.mobilehealthsports.vaccinepass.business.models.Vaccine
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepository
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.*
import com.mobilehealthsports.vaccinepass.ui.main.add_vaccine.ReceivedVaccineInfo
import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
