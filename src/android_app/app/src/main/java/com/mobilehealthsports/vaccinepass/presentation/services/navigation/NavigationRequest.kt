package com.mobilehealthsports.vaccinepass.presentation.services.navigation

import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel

sealed class NavigationRequest

// TODO: Add navigation requests to other screens
object HomeRequest : NavigationRequest()
class PinRequest(val state: PinViewModel.PinState, val pinLength: Int) : NavigationRequest()