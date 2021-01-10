package com.mobilehealthsports.vaccinepass.ui.vaccination

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mobilehealthsports.vaccinepass.business.models.Vaccination
import com.mobilehealthsports.vaccinepass.business.models.Vaccine
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepository
import com.mobilehealthsports.vaccinepass.business.repository.VaccineRepository
import com.mobilehealthsports.vaccinepass.presentation.services.ServiceRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageRequest
import com.mobilehealthsports.vaccinepass.presentation.services.messages.ToastRequest
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationRequest
import com.mobilehealthsports.vaccinepass.presentation.viewmodels.BaseViewModel
import com.mobilehealthsports.vaccinepass.ui.main.add_vaccine.ReceivedVaccineInfo
import com.mobilehealthsports.vaccinepass.util.livedata.NonNullMutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VaccinationViewModel(private val vaccinationRepository: VaccinationRepository, private val vaccineRepository: VaccineRepository) : BaseViewModel() {
    val messageRequest = ServiceRequest<MessageRequest>()
    val navigationRequest = ServiceRequest<NavigationRequest>()

    val vaccinationId: MutableLiveData<Long?> = MutableLiveData(null)
    var vaccination: MutableLiveData<Vaccination?> = MutableLiveData(null)
    val qrJson: MutableLiveData<String> = MutableLiveData("")
    val doctorName: MutableLiveData<String?> = MutableLiveData("")

    val vaccineName: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val vaccineCompany: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val vaccinationDate: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val refreshDate: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val expiresIn: NonNullMutableLiveData<String> = NonNullMutableLiveData("")
    val userId: NonNullMutableLiveData<String> = NonNullMutableLiveData("")

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
            try {
                val vac =  vaccinationRepository.getVaccination(id)
                vac?.let { _vaccination ->
                    vaccination.postValue(_vaccination)
                    val vaccine = vaccineRepository.getVaccine(_vaccination.f_uid)
                    vaccinationDate.postValue(_vaccination.vaccinationDate.toString())
                    refreshDate.postValue(_vaccination.refreshDate.toString())
                    expiresIn.postValue(_vaccination.expiresIn)
                    userId.postValue(_vaccination.userId)
                    doctorName.postValue(_vaccination.doctorName)
                    vaccine?.let { vac ->
                        vaccineName.postValue(vac.name)
                        vaccineCompany.postValue(vac.company!!)
                        qrJson.postValue(getSignDataString(_vaccination, vac))
                    }
                }

            } catch(ex: Exception) {
                messageRequest.request(ToastRequest("Could not load vaccination"))
            }
        }
    }

    private fun getSignDataString(vaccination: Vaccination, vaccine: Vaccine): String {
        val gson = Gson()

        val receivedVaccineInfo = ReceivedVaccineInfo(vaccination.userId, vaccine.name, vaccination.vaccinationDate.toString(),
            vaccination.expiresIn, vaccination.doctorId, vaccination.doctorName)
        val vaccineInfoString = gson.toJson(receivedVaccineInfo).toString();

        val params = HashMap<String, String?>()
        params["data"] = vaccineInfoString
        params["signature"] = vaccination.signature

        return gson.toJson(params).toString()
    }

    override fun onCleared() {
        vaccinationId.removeObserver(observer)
        super.onCleared()
    }
}