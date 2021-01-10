package com.mobilehealthsports.vaccinepass.ui.main.vaccine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mobilehealthsports.vaccinepass.business.models.User
import com.mobilehealthsports.vaccinepass.business.models.Vaccination
import com.mobilehealthsports.vaccinepass.business.models.Vaccine
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepository
import com.mobilehealthsports.vaccinepass.business.repository.VaccineRepository
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.ui.main.adapter.ItemViewAdapter
import com.mobilehealthsports.vaccinepass.ui.main.user.*
import com.mobilehealthsports.vaccinepass.util.livedata.NonNullMutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class VaccineViewModel(
    private val vaccinationRepository: VaccinationRepository,
    private val vaccineRepository: VaccineRepository
) : BaseViewModel() {
    val navigationRequest = ServiceRequest<NavigationRequest>()

    private val _user: MutableLiveData<User?> = MutableLiveData(null)
    var user: LiveData<User?> = _user

    private var activeListItems: MutableList<ListItem> = ArrayList()
    private var scheduledListItems: MutableList<ListItem> = ArrayList()
    private var notScheduledListItems: MutableList<ListItem> = ArrayList()
    private var notVaccinatedListItems: MutableList<ListItem> = ArrayList()
    var listItems: MutableList<ListItem> = ArrayList()
    val selectedId = MutableLiveData(-1L)
    val noVaccinationsString = NonNullMutableLiveData("")
    val itemAdapter = ItemViewAdapter {}

    fun setUser(user: User?) {
        user?.let {
            _user.value = user
            viewModelScope.launch(Dispatchers.IO) {
                loadVaccinations(user)
            }
        }
    }

    fun setVaccinationType(state: VaccineState) {
        listItems.clear()

        when(state) {
            VaccineState.ALL -> {
                listItems.addAll(scheduledListItems)
                listItems.addAll(activeListItems)
                listItems.addAll(notVaccinatedListItems)
                listItems.addAll(notScheduledListItems)
            }
            VaccineState.ACTIVE -> listItems.addAll(activeListItems)
            VaccineState.SCHEDULED -> listItems.addAll(scheduledListItems)
            VaccineState.NOT_SCHEDULED -> listItems.addAll(notScheduledListItems)
            VaccineState.NOT_VACCINATED -> listItems.addAll(notVaccinatedListItems)
        }

        if (listItems.isEmpty()) {
            listItems.add(HeaderItem(noVaccinationsString.value))
        }

        itemAdapter.apply {
            items.clear()
            items.addAll(listItems)
            notifyDataSetChanged()
        }
    }

    private suspend fun loadVaccinations(currentUser: User) {
        val vaccinations: List<Vaccination>? = vaccinationRepository.getAllActiveVaccinations()
        vaccinations?.let { list ->
            list.filter {
                it.userId.toLong() == currentUser.uid
            }

            val itemsList = mutableListOf<VaccineItem>()

            list.forEach { item ->
                val vaccine: Vaccine? = vaccineRepository.getVaccine(item.f_uid)
                vaccine?.let { vac ->
                    val vacItem = VaccineItem(
                        item.uid,
                        vac.name,
                        item.vaccinationDate!!,
                        VaccineState.ACTIVE,
                        this::onItemClick
                    )
                    itemsList.add(vacItem)
                }
            }
            mapListItems(itemsList)
        }
    }

    private fun mapListItems(vaccineItems: MutableList<VaccineItem>) {
        val listMap: MutableMap<VaccineState, MutableList<VaccineItem>> = toMap(vaccineItems)
        listItems.clear()

        for (state: VaccineState in listMap.keys) {
            val header = HeaderItem(state.text)
            val list: MutableList<VaccineItem> = listMap.getOrDefault(state, mutableListOf())

            when (state) {
                VaccineState.ACTIVE -> {
                    activeListItems.add(header)
                    activeListItems.addAll(list)
                }
                VaccineState.SCHEDULED -> {
                    scheduledListItems.add(header)
                    scheduledListItems.addAll(list)

                }
                VaccineState.NOT_SCHEDULED -> {
                    notScheduledListItems.add(header)
                    notScheduledListItems.addAll(list)
                }
                VaccineState.NOT_VACCINATED -> {
                    notVaccinatedListItems.add(header)
                    notVaccinatedListItems.addAll(list)
                }
                else -> { }
            }
        }

        listItems.addAll(scheduledListItems)
        listItems.addAll(activeListItems)
        listItems.addAll(notVaccinatedListItems)
        listItems.addAll(notScheduledListItems)

        viewModelScope.launch(Dispatchers.Main) {
            itemAdapter.apply {
                items.clear()
                items.addAll(listItems)
                notifyDataSetChanged()
            }
        }
    }

    private fun toMap(vacs: MutableList<VaccineItem>): MutableMap<VaccineState, MutableList<VaccineItem>> {
        val map: MutableMap<VaccineState, MutableList<VaccineItem>> =
            EnumMap(VaccineState::class.java)

        for (vaccineItem: VaccineItem in vacs) {
            val value: MutableList<VaccineItem> = map[vaccineItem.state] ?: ArrayList()
            map.putIfAbsent(vaccineItem.state, value)
            value.add(vaccineItem)
        }
        return map
    }

    private fun onItemClick(id: Long) {
        selectedId.value = id
    }
}