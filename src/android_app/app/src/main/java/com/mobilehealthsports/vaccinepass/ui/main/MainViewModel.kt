package com.mobilehealthsports.vaccinepass.ui.main

import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.Job

class MainViewModel : BaseViewModel() {

    val messageRequest = ServiceRequest<MessageRequest>()
    private var currentErrorCoroutine: Job? = null

    lateinit var user: User

    companion object {
        const val SHARED_PREF_KEY = "MAIN"
    }

}