package com.mobilehealthsports.vaccinepass.ui.main.add_vaccine

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.budiyev.android.codescanner.*
import com.google.gson.Gson
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.Vaccine
import com.mobilehealthsports.vaccinepass.business.repository.VaccineRepository
import com.mobilehealthsports.vaccinepass.ui.main.utils.AddVaccineResult
import com.mobilehealthsports.vaccinepass.ui.main.utils.VolleySingleton
import com.mobilehealthsports.vaccinepass.util.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.io.Console
import java.util.*

class ScanQrCodeActivity : BaseActivity() {

    private val URL = "http://192.168.0.110:3000/addVaccine"
    private lateinit var codeScanner: CodeScanner
    val vaccineRespositry: VaccineRepository by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr_code)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        lifecycleScope.launch(Dispatchers.IO) {
            val id = vaccineRespositry.insertVaccine(
                Vaccine(
                    0,
                    "Test",
                    "Test",
                    "asdf",
                    "Ab 14 Jahre Markus",
                    "test",
                    "asdf",
                    "asdfsff",
                    "sdfsdf",
                    true
                )
            ) ?: -1
            val vaccine = vaccineRespositry.getVaccine(id)

            vaccine?.let{

            }
        }

        GlobalScope.launch(Dispatchers.IO) {
        }

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
            //vaccineSignature.signature = Base64.getEncoder().encodeToString(oriString.toByteArray())
            val jsonObject = JSONObject(it.text)
            val request = JsonObjectRequest(
                Request.Method.POST, URL, jsonObject,
                { response ->
                    // Process the json
                    try {
                        val success =
                            gson.fromJson(response.toString(), AddVaccineResult::class.java)
                        if (success.success) {

                            // TODO: save vaccine to local database
                            Toast.makeText(
                                applicationContext,
                                "Added vaccination successful",
                                Toast.LENGTH_SHORT
                            ).show()
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
}