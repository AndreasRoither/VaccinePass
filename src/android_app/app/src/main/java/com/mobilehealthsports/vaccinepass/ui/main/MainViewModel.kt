package com.mobilehealthsports.vaccinepass.ui.main

import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel

class MainViewModel : BaseViewModel() {
    val user: MutableLiveData<User?> = MutableLiveData(null)
}