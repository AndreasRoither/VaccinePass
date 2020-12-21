package com.mobilehealthsports.vaccinepass.ui.main.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.ToastRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

enum class ListItemType {
    HEADER,
    VACCINE
}

abstract class ListItem(open var type: ListItemType)
data class HeaderItem(val text: String, override var type: ListItemType = ListItemType.HEADER) : ListItem(type)
data class VaccineItem(val name: String, val date: LocalDate, val state: VaccineState, override var type: ListItemType = ListItemType.VACCINE) : ListItem(type)

enum class VaccineState(val text: String) {
    ACTIVE("Active Vaccinations"),
    SCHEDULED("Scheduled Vaccinations"),
    NOT_SCHEDULED("Expired Vaccinations"),
    NOT_VACCINATED("Mandatory Vaccinations")
}

class UserViewModel : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()

    val vaccineAdapter = VaccineViewAdapter {
        messageRequest.request(ToastRequest("Clicked"))
    }
    private var vaccineLength: Int = 10
    private var vaccineItems: MutableList<VaccineItem>
    private var _user = MutableLiveData(User(0, "Test", "Test", "0 neg", LocalDate.of(2020, 4, 5), 75f, 180f, 1, null))
    var user: LiveData<User> = _user
    var listItems: MutableList<ListItem> = ArrayList()

    fun setUser(user: User?) {
        user?.let {
            _user.value = user
        }
    }

    init {
        vaccineItems = MutableList(vaccineLength) { VaccineItem("Hepatitis C", LocalDate.of(2020, 11, 15), VaccineState.ACTIVE) }

        val listMap: MutableMap<VaccineState, MutableList<VaccineItem>> = toMap(vaccineItems)

        for (state: VaccineState in listMap.keys) {
            val header = HeaderItem(state.text)
            listItems.add(header)
            for (vac: VaccineItem in listMap[state]!!) {
                listItems.add(vac)
            }
        }

        vaccineAdapter.apply {
            items.addAll(listItems)
            notifyDataSetChanged()
        }
    }

    private fun toMap(vacs: MutableList<VaccineItem>): MutableMap<VaccineState, MutableList<VaccineItem>> {
        val map: MutableMap<VaccineState, MutableList<VaccineItem>> = EnumMap(VaccineState::class.java)

        for (vaccineItem: VaccineItem in vacs) {
            val value: MutableList<VaccineItem> = map[vaccineItem.state] ?: ArrayList()
            map.putIfAbsent(vaccineItem.state, value)
            value.add(vaccineItem)
        }
        return map
    }

    companion object {
        const val SHARED_PREF_KEY = "VACCINE"
        const val DEFAULT_COLOR = R.color.black
        const val ERROR_COLOR = R.color.error_red
        const val ERROR_DELAY = 3000L
    }
}
