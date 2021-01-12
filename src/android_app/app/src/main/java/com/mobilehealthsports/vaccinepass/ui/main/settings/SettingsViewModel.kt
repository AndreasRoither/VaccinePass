package com.mobilehealthsports.vaccinepass.ui.main.settings

import android.app.AlertDialog
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.business.repository.AppointmentRepository
import com.mobilehealthsports.vaccinepass.business.repository.ReminderRepository
import com.mobilehealthsports.vaccinepass.business.repository.UserRepository
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepository
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.InitActivityRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.SelectUserRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.util.ResetHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate

data class SettingsItem(val icon: Int, val header: String, val text: String)

class SettingsViewModel(
    private var sharedPreferences: SharedPreferences,
    private var appointmentRepository: AppointmentRepository,
    private var reminderRepository: ReminderRepository,
    private var userRepository: UserRepository,
    private var vaccinationRepository: VaccinationRepository
) : BaseViewModel() {

    val messageRequest = ServiceRequest<MessageRequest>()
    private var resetCoroutine: Job? = null

    private var _user = MutableLiveData(User(0, "Test", "Test", "0 neg", LocalDate.of(2020, 4, 5), 75f, 180f, 1, null))
    var user: LiveData<User> = _user

    var listItems: MutableList<SettingsItem> = ArrayList()

    private val _navigationRequest = ServiceRequest<NavigationRequest>()
    val navigationRequest = _navigationRequest

    fun resetApp(view: View) {
        val alertDialogBuilder = AlertDialog.Builder(view.context)
        alertDialogBuilder.setMessage(R.string.confirm_reset)
                .setTitle(R.string.title_reset)
                .setPositiveButton(R.string.proceed) { _, _ ->
                    resetCoroutine = GlobalScope.launch {
                        ResetHelper.resetApp(sharedPreferences, appointmentRepository, reminderRepository, userRepository, vaccinationRepository)
                    }
                    navigationRequest.request(InitActivityRequest(false))
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                }

        alertDialogBuilder.create().show()
    }

    inner class CardClickListener {
        fun onCardClicked() {
            navigationRequest.request(SelectUserRequest)
        }
    }

    init {
        listItems.add(SettingsItem(R.drawable.ic_shield, "Account", "Security"))
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