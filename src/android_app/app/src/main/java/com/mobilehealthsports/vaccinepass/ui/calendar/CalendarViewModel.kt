package com.mobilehealthsports.vaccinepass.ui.calendar

import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel

class CalendarViewModel : BaseViewModel() {

    val messageRequest = ServiceRequest<MessageRequest>()
    val navigationRequest = ServiceRequest<NavigationRequest>()

    
}