package com.mobilehealthsports.vaccinepass.ui.main.user

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.Job
import java.time.LocalDate
import java.util.EnumMap
import kotlin.collections.ArrayList

enum class ListItemType {
    HEADER,
    VACCINE
}

abstract class ListItem(open var type: ListItemType)
data class HeaderItem(val text: String, override var type: ListItemType = ListItemType.HEADER) : ListItem(type)
data class Vaccine(val name: String, val date: LocalDate, val state: VaccineState, override var type: ListItemType = ListItemType.VACCINE) : ListItem(type)

data class User(val firstName: String, val lastName: String, val bloodType: String, val weight: Int, val height: Int, val birthDate: LocalDate)

enum class VaccineState(val text: String) {
    ACTIVE("Active Vaccinations"),
    SCHEDULED("Scheduled Vaccinations"),
    NOT_SCHEDULED("Expired Vaccinations"),
    NOT_VACCINATED("Mandatory Vaccinations")
}

class UserViewModel : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    private var currentErrorCoroutine: Job? = null

    private var vaccineLength : Int = 10

    private var _user = MutableLiveData(User("John", "Doe", "0 pos", 75, 180, LocalDate.of(1996,10,22)))
    var user: LiveData<User> = _user

    var listItems : MutableList<ListItem> = ArrayList()

    private var vaccines : MutableList<Vaccine>

    init {
        vaccines = MutableList(vaccineLength) { Vaccine("Hepatitis C", LocalDate.of(2020,11,15), VaccineState.ACTIVE)}

        val listMap : MutableMap<VaccineState, MutableList<Vaccine>> = toMap(vaccines)

        for(state: VaccineState in listMap.keys) {
            val header = HeaderItem(state.text)
            listItems.add(header)
            for(vac: Vaccine in listMap[state]!!){
                listItems.add(vac)
            }
        }
    }

    private fun toMap(vacs : MutableList<Vaccine>) : MutableMap<VaccineState, MutableList<Vaccine>> {
        val map: MutableMap<VaccineState, MutableList<Vaccine>> = EnumMap(VaccineState::class.java)

        for(vaccine: Vaccine in vacs) {
            val value: MutableList<Vaccine> = map[vaccine.state] ?: ArrayList()
            map.putIfAbsent(vaccine.state, value)
            value.add(vaccine)
        }
        return map
    }

    lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREF_KEY = "VACCINE"
        const val DEFAULT_COLOR = R.color.black
        const val ERROR_COLOR = R.color.error_red
        const val ERROR_DELAY = 3000L
    }
}
