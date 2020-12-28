package com.mobilehealthsports.vaccinepass.ui.vaccination

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentVaccinationBinding
import com.mobilehealthsports.vaccinepass.presentation.services.messages.MessageService
import com.mobilehealthsports.vaccinepass.presentation.services.navigation.NavigationService
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.core.parameter.parametersOf


class VaccinationFragment : Fragment() {
    private var vaccinationId: Long? = null

    private var disposables = CompositeDisposable()
    private val messageService: MessageService by inject { parametersOf(this) }
    private val navigationService: NavigationService by inject { parametersOf(this) }
    private val viewModel: VaccinationViewModel by stateViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            vaccinationId = it.getLong(ARG_PARAM_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vaccination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationService.subscribeToRequests(viewModel.navigationRequest)
        messageService.subscribeToRequests(viewModel.messageRequest)
        disposables.addAll(messageService)

        val binding = FragmentVaccinationBinding.bind(view)
        binding.viewModel = viewModel
        binding.lifecycleOwner = requireActivity()

        viewModel.qrJson.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                binding.qrCode.setImageBitmap(generateQRCode(it))
            }
        })

        viewModel.vaccinationId.value = vaccinationId
    }

    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d("VaccinationUserData", "generateQRCode: ${e.message}")
        }
        return bitmap
    }

    companion object {
        private const val ARG_PARAM_ID = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param vaccinationId id of the vaccination.
         * @return A new instance of fragment VaccinationFragment.
         */
        @JvmStatic
        fun newInstance(vaccinationId: Long) =
            VaccinationFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_PARAM_ID, vaccinationId)
                }
            }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}