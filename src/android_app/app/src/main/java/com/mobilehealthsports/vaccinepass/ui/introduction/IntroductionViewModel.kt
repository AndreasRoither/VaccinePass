package com.mobilehealthsports.vaccinepass.ui.introduction

import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.NonNullMutableLiveData

class IntroductionViewModel : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()

    val okClick = NonNullMutableLiveData(false)

    fun onClick() {
        okClick.value = true
    }
}