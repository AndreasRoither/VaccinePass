package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.budiyev.android.codescanner.*
import com.google.gson.Gson
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.Vaccination
import com.mobilehealthsports.vaccinepass.business.models.Vaccine
import com.mobilehealthsports.vaccinepass.business.repository.VaccinationRepository
import com.mobilehealthsports.vaccinepass.business.repository.VaccineRepository
import com.mobilehealthsports.vaccinepass.ui.main.utils.AddVaccineResult
import com.mobilehealthsports.vaccinepass.ui.main.utils.VolleySingleton
import com.mobilehealthsports.vaccinepass.util.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class ScanQrCodeActivity : BaseActivity() {

    private val URL = "http://192.168.0.110:3000/addVaccine"
    private lateinit var codeScanner: CodeScanner
    private val vaccineRespositry: VaccineRepository by inject()
    private val vaccinationRepository: VaccinationRepository by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr_code)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        //TODO add dummy vaccine
        //insertDummyVaccine()

        codeScanner = CodeScanner(this, scannerView)

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

            val vaccineSignature = gson.fromJson(it.text, VaccineSignature::class.java)
            val receivedVaccineInfo = gson.fromJson(vaccineSignature.data, ReceivedVaccineInfo::class.java)

            val jsonObject = JSONObject(it.text)
            val request = JsonObjectRequest(Request.Method.POST, URL, jsonObject, { response ->
                // Process the json
                try {
                    val success = gson.fromJson(response.toString(), AddVaccineResult::class.java)
                    if (success.success) {
                        val id = getVaccineUid(receivedVaccineInfo.productName)
                        val vaccination = createVaccination(id, receivedVaccineInfo, vaccineSignature.signature)
                        addVaccinationToDatabase(vaccination)

                        Toast.makeText(applicationContext, "Added vaccination successful", Toast.LENGTH_SHORT).show()
                        finish()
                    }
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

            VolleySingleton.getInstance(this).addToRequestQueue(request)
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    private fun addVaccinationToDatabase(vaccination: Vaccination) {
        lifecycleScope.launch(Dispatchers.IO) {
            vaccinationRepository.insertVaccination(vaccination)
            val vaccinations = vaccinationRepository.getAllActiveVaccinations()
            Timber.d("vaccines: %s", vaccinations!!.size)
        }
    }

    private fun createVaccination(id: Long, receivedVaccineInfo: ReceivedVaccineInfo, signature: String): Vaccination {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val vaccinationDate = LocalDate.parse(receivedVaccineInfo.vaccinationDate, formatter)
        val expiresIn = calculateExpirationDate(vaccinationDate, receivedVaccineInfo.expiresIn)

        return Vaccination(
            0,
            id,
            true,
            expiresIn,
            receivedVaccineInfo.userId,
            vaccinationDate,
            receivedVaccineInfo.expiresIn,
            receivedVaccineInfo.doctorId,
            receivedVaccineInfo.doctorName,
            signature
        )
    }

    private fun calculateExpirationDate(vaccinationDate: LocalDate, expiresIn: String): LocalDate {
        val months: Long = when (expiresIn) {
            "never" -> 1200
            "0.5 year" -> 6
            "1 year" -> 12
            "2 years" -> 24
            "5 years" -> 60
            else -> 0
        }

        return vaccinationDate.plusMonths(months)
    }

    private fun insertDummyVaccine() {
        lifecycleScope.launch(Dispatchers.IO) {
            vaccineRespositry.insertVaccine(
                Vaccine(
                    Random.nextLong(2, 999999999),
                    "DTaP-IPV-Vakzine SS",
                    "AJ Vaccines A/S",
                    "Di, Tet, Pert, Polio  (Grundimmunisierung) ",
                    "Kinder ab 2 Monaten",
                    "",
                    "Aluminiumhydroxid ",
                    "Nein",
                    "",
                    false
                )
            )
        }
    }

    private fun getVaccineUid(productName: String): Long {
        var vaccine = Vaccine()
        runBlocking {
            lifecycleScope.launch(Dispatchers.IO) {
                vaccine = vaccineRespositry.getVaccineByName(productName)!!
            }
        }
        return vaccine.uid
    }
}