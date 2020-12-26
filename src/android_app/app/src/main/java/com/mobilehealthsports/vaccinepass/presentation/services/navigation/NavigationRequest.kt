package com.mobilehealthsports.vaccinepass.presentation.services.navigation

import com.mobilehealthsports.vaccinepass.ui.pin.PinViewModel

sealed class NavigationRequest

object HomeRequest : NavigationRequest()
class PinRequest(val state: PinViewModel.PinState, val pinLength: Int) : NavigationRequest()
class FragmentTestRequest(val fragment: String) : NavigationRequest()
object MainRequest : NavigationRequest()
object SelectUserRequest : NavigationRequest()
class UserCreationRequest(val fragment: String) : NavigationRequest()