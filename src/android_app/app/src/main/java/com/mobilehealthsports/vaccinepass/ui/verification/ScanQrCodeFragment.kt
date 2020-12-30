package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.budiyev.android.codescanner.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.databinding.FragmentScanQrCodeBinding
import com.mobilehealthsports.vaccinepass.ui.main.utils.AddVaccineResult
import com.mobilehealthsports.vaccinepass.ui.main.utils.VolleySingleton
import com.mobilehealthsports.vaccinepass.ui.verification.VerificationResultViewFragment
import org.json.JSONObject

class ScanQrCodeFragment : Fragment() {
    private val URL = "http://192.168.0.110:3000/addVaccine"
    private lateinit var codeScanner: CodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qr_code, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentScanQrCodeBinding.bind(view)
        val scannerView = binding.scannerView
        codeScanner = CodeScanner(requireContext(), scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not
        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            val gson = Gson()
            val vaccineSignature: VaccineSignature
            val receivedVaccineInfo: ReceivedVaccineInfo
            try {
                vaccineSignature = gson.fromJson(it.text, VaccineSignature::class.java)
                receivedVaccineInfo =
                    gson.fromJson(vaccineSignature.data, ReceivedVaccineInfo::class.java)
            } catch (jsException: JsonSyntaxException) {
                return@DecodeCallback
            }
            val jsonObject = JSONObject(it.text)
            val request = JsonObjectRequest(Request.Method.POST, URL, jsonObject, { response ->
                // Process the json
                try {
                    val success = gson.fromJson(response.toString(), AddVaccineResult::class.java)
                    showResultInActivity(success.success, vaccineSignature.data)
                } catch (e: Exception) {
                    println("Exception: $e")
                }
            }, {
                // Error in request
                println("Volley error: $it")
            })
            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                // 0 means no retry
                0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            )
            VolleySingleton.getInstance(requireContext()).addToRequestQueue(request)
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            requireActivity().runOnUiThread {
                Toast.makeText(
                    requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        // Start scanner
        codeScanner.startPreview()
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }
    companion object {
        const val TAG = "ScanQrCodeFragment"
        const val EXTRA_SUCCESS = "SCANQRCODEVERIFICATIONACTIVITY_SUCCESS"
        const val EXTRA_VACCINATION_INFO = "SCANQRCODEVERIFICATIONACTIVITY_VACCINATION_INFO"

    }
    private fun showResultInActivity(result: Boolean, vaccinationInfo: String) {
        val supportFragmentManager: FragmentManager = requireActivity().supportFragmentManager
        supportFragmentManager.commit {
            val fragment = VerificationResultViewFragment.newInstance(result, vaccinationInfo)
            replace(R.id.fragment_container_view, fragment)
        }
    }
}