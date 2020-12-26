package com.example.doctor_app.qrcode

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.doctor_app.R
import com.example.doctor_app.certificate.VaccinationUserData
import com.example.doctor_app.databinding.ActivityQrCodeBinding
import com.example.doctor_app.key_management.KeyManager
import com.example.doctor_app.model.VaccineInfo
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class QrCode : AppCompatActivity() {

    private lateinit var binding: ActivityQrCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qr_code)
        binding.lifecycleOwner = this

        val gson = Gson()

        val vaccineInfoString = intent.getStringExtra(VaccinationUserData.VACCINE_INFO_KEY)!!
        val vaccineInfo = gson.fromJson(vaccineInfoString, VaccineInfo::class.java)

        val data = getSignDataString(vaccineInfoString)
        val qrImage = generateQRCode(data)

        updateUI(qrImage, vaccineInfo)
    }

    private fun updateUI(qrImage: Bitmap, vaccineInfo: VaccineInfo) {
        binding.qrCode.setImageBitmap(qrImage)
        binding.vaccine.text = vaccineInfo.productName
        binding.userId.text = vaccineInfo.userId
        binding.date.text = vaccineInfo.vaccinationDate
        binding.doctor.text = vaccineInfo.doctorName
    }

    private fun getSignDataString(vacInfoString: String): String {
        val gson = Gson()
        //val vaccineInfo = gson.fromJson(vacInfoString, VaccineInfo::class.java)

        val keyManager = KeyManager()
        val key = applicationContext.getSharedPreferences("keys", Context.MODE_PRIVATE).getString("secret_key", "")
        val signature = keyManager.signData(vacInfoString, key!!)

        val params = HashMap<String, String>()
        params["data"] = vacInfoString
        params["signature"] = signature

        return gson.toJson(params).toString()
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
}