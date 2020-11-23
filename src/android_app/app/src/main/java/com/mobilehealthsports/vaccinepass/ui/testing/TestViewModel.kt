package com.mobilehealthsports.vaccinepass.ui.testing

import androidx.lifecycle.ViewModel
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.PinRequest
import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel

class TestViewModel : ViewModel() {

    private val _navigationRequest = ServiceRequest<NavigationRequest>()
    val navigationRequest = _navigationRequest

    fun startPinActivityInitial() {
        navigationRequest.request(PinRequest(PinViewModel.PinState.INITIAL, 4))
    }

    fun startPinActivityCheck() {
        navigationRequest.request(PinRequest(PinViewModel.PinState.CHECK, 4))
    }
}