package com.mobilehealthsports.vaccinepass.ui.main

import android.content.SharedPreferences
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.Job

class MainViewModel : BaseViewModel() {

    val messageRequest = ServiceRequest<MessageRequest>()
    private var currentErrorCoroutine: Job? = null

    companion object {
        const val SHARED_PREF_KEY = "MAIN"
    }

}