package com.mobilehealthsports.vaccinepass.ui.main.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.SelectUserRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import java.time.LocalDate

data class SettingsItem(val icon: Int, val header: String, val text: String)

class SettingsViewModel : BaseViewModel() {

    val messageRequest = ServiceRequest<MessageRequest>()

    private var _user = MutableLiveData(User(0, "Test", "Test", "0 neg", LocalDate.of(2020, 4, 5), 75f, 180f, 1, null))
    var user: LiveData<User> = _user

    var listItems: MutableList<SettingsItem> = ArrayList()

    private val _navigationRequest = ServiceRequest<NavigationRequest>()
    val navigationRequest = _navigationRequest

    inner class CardClickListener {
        fun onCardClicked() {
            navigationRequest.request(SelectUserRequest)
        }
    }

    init {
        listItems.add(SettingsItem(R.drawable.ic_shield, "Account", "Security, reset"))
        listItems.add(SettingsItem(R.drawable.ic_bell, "Notifications", "Messages, Tones"))
        listItems.add(SettingsItem(R.drawable.ic_palette, "Theme", "Accents, colors"))
        listItems.add(SettingsItem(R.drawable.ic_fingerprint, "Biometrics", "TouchID, PIN"))
        listItems.add(SettingsItem(R.drawable.ic_help, "Help", "FAQ, privacy policy, links"))
    }

    fun setUser(user: User?) {
        user?.let {
            _user.value = user
        }
    }
}