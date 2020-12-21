package com.mobilehealthsports.vaccinepass.ui.user_select

import android.content.SharedPreferences
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.MainRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.set
import java.time.LocalDate

class SelectUserViewModel(val sharedPreferences: SharedPreferences) : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    private val _navigationRequest = ServiceRequest<NavigationRequest>()
    val navigationRequest = _navigationRequest

    var userList: MutableList<User> = ArrayList()

    inner class ItemClickListener {
        fun onItemClicked(user: User) {
            sharedPreferences[PreferenceHelper.LAST_USER_ID_PREF] = user.uid
            navigationRequest.request(MainRequest)
        }
    }

    init {
        userList.add(0, User(1, "John", "Doe", "0 pos", LocalDate.of(1999, 5, 25), 75.5f, 178f, 0, null))
        userList.add(1, User(2, "Bear", "Grylls", "A neg", LocalDate.of(1975, 10, 5), 84.3f, 184f, 0, null))
    }

    companion object {
        const val SHARED_PREF_KEY = "PIN"
        const val DEFAULT_COLOR = R.color.black
        const val ERROR_COLOR = R.color.error_red
        const val ERROR_DELAY = 3000L
    }
}