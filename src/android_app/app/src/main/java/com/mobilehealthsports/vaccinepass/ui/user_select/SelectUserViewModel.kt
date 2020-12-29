package com.mobilehealthsports.vaccinepass.ui.user_select

import android.content.SharedPreferences
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.business.repository.UserRepository
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.MainRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.UserCreationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper
import com.mobilehealthsports.vaccinepass.util.PreferenceHelper.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SelectUserViewModel(val sharedPreferences: SharedPreferences, private val userRepository: UserRepository) : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    private val _navigationRequest = ServiceRequest<NavigationRequest>()
    val navigationRequest = _navigationRequest

    var userList: MutableList<User> = ArrayList()

    fun startUserCreation() {
        navigationRequest.request(UserCreationRequest())
    }

    inner class ItemClickListener {
        fun onItemClicked(user: User) {
            sharedPreferences[PreferenceHelper.LAST_USER_ID_PREF] = user.uid
            navigationRequest.request(MainRequest)
        }
    }

    init {
        GlobalScope.launch(Dispatchers.IO) {
            userRepository.getAllUsers().let {
                userList.addAll(it)
            }
        }
    }
}