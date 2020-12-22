package com.mobilehealthsports.vaccinepass.ui.vaccination

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.mobilehealthsports.vaccinepass.business.models.Vaccination
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepository
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VaccinationViewModel(private val vaccinationRepository: VaccinationRepository) : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    val navigationRequest = ServiceRequest<NavigationRequest>()

    val vaccinationId: MutableLiveData<Long?> = MutableLiveData(null)
    val vaccination: MutableLiveData<Vaccination?> = MutableLiveData(null)

    private var observer: Observer<Long?>

    init {
        observer = Observer { id ->
            id?.let {
                queryVaccination(it)
            }
        }
        vaccinationId.observeForever(observer)
    }

    private fun queryVaccination(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            vaccination.postValue(vaccinationRepository.getVaccination(id))
        }
    }

    override fun onCleared() {
        vaccinationId.removeObserver(observer)
        super.onCleared()
    }
}