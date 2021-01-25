package com.mobilehealthsports.vaccinepass.ui.verification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.repository.UserRepository
import com.mobilehealthsports.vaccinepass.databinding.FragmentVerificationFailureOldBinding
import com.mobilehealthsports.vaccinepass.databinding.FragmentVerificationSuccessOldBinding
import com.mobilehealthsports.vaccinepass.ui.main.add_vaccine.ReceivedVaccineInfo
import com.mobilehealthsports.vaccinepass.ui.main.add_vaccine.ScanQrCodeFragment
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject

class VerificationResultViewFragment : Fragment() {

    lateinit var bindingSuccess: FragmentVerificationSuccessOldBinding
    lateinit var bindingFailure: FragmentVerificationFailureOldBinding

    private val userRepository: UserRepository by inject()
    private var verified: Boolean = false
    private lateinit var vaccinationInfo: ReceivedVaccineInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = Gson()
        var vaccinationString = ""

        arguments?.let {
            verified = it.getBoolean(ScanQrCodeFragment.EXTRA_SUCCESS, false)
            vaccinationString = it.getString(ScanQrCodeFragment.EXTRA_VACCINATION_INFO).toString()
        }
        vaccinationInfo = gson.fromJson(vaccinationString, ReceivedVaccineInfo::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (verified) {
            inflater.inflate(R.layout.fragment_verification_success_old, container, false)

        } else {
            inflater.inflate(R.layout.fragment_verification_failure_old, container, false)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (verified) {
            bindingSuccess = FragmentVerificationSuccessOldBinding.bind(view)
            showSuccessView(vaccinationInfo)
        } else {
            bindingFailure = FragmentVerificationFailureOldBinding.bind(view)
            showErrorView(vaccinationInfo)
        }
    }

    private fun showSuccessView(vaccinationInfo: ReceivedVaccineInfo) {
        bindingSuccess.doctorName.text = vaccinationInfo.doctorName
        bindingSuccess.vaccinationDate.text = vaccinationInfo.vaccinationDate
        bindingSuccess.vaccine.text = vaccinationInfo.productName

        var userName = ""
        runBlocking {
            userRepository.getUser(vaccinationInfo.userId.toLong())?.let {
                //userRepository.getUser(1)?.let { //just for testing purposes
                userName = it.firstName + " " + it.lastName
            }
        }
        bindingSuccess.toolbar.title = userName
    }

    private fun showErrorView(vaccinationInfo: ReceivedVaccineInfo) {
        var userName = ""
        runBlocking {
            userRepository.getUser(vaccinationInfo.userId.toLong())?.let {
                //userRepository.getUser(1)?.let { //just for testing purposes
                userName = it.firstName + " " + it.lastName
            }
        }
        bindingFailure.toolbar.title = userName
    }

    companion object {
        @JvmStatic
        fun newInstance(result: Boolean, vaccinationInfo: String) =
            VerificationResultViewFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ScanQrCodeFragment.EXTRA_SUCCESS, result)
                    putString(ScanQrCodeFragment.EXTRA_VACCINATION_INFO, vaccinationInfo)
                }
            }
    }
}