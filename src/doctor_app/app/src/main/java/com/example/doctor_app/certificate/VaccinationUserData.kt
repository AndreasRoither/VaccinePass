package com.example.doctor_app.certificate

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.doctor_app.R
import com.example.doctor_app.databinding.ActivityVaccinationUserDataBinding
import com.example.doctor_app.key_management.KeyManager
import com.example.doctor_app.model.User
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class VaccinationUserData : AppCompatActivity() {

    private lateinit var binding: ActivityVaccinationUserDataBinding
    private val viewModel = VaccineUserVM()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var gson = Gson()
        user = gson.fromJson(intent.getStringExtra("user"), User::class.java)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_vaccination_user_data)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        binding.generateQr.setOnClickListener(this::onButtonClick)

    }

    private fun onButtonClick(v: View) {
        Toast.makeText(this, viewModel.userId , Toast.LENGTH_SHORT).show();
        var keyManager = KeyManager()
        var keyAlias = user.mail + "_secret"
        var data = viewModel.userId
        var doctorMail = user.mail//keyManager.getPublicKey(keyAlias)
        var signature = keyManager.signData(keyAlias, data)

        val params = HashMap<String, String>()
        params["userid"] = data
        params["doctor"] = doctorMail
        params["signature"] = signature

        //val jsonObject = JSONObject(params as Map<*, *>);
        val gson = Gson()

        val qrImage = generateQRCode(gson.toJson(params).toString())
        binding.qrCode.setImageBitmap(qrImage)

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