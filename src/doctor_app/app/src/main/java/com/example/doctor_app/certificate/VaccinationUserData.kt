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
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException

class VaccinationUserData : AppCompatActivity() {

    private lateinit var binding: ActivityVaccinationUserDataBinding
    private val viewModel = VaccineUserVM()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_vaccination_user_data)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this


        binding.generateQr.setOnClickListener(this::onButtonClick)

    }

    private fun onButtonClick(v: View) {
        Toast.makeText(this, viewModel.userId , Toast.LENGTH_SHORT).show();

        val qrImage = generateQRCode(viewModel.userId)
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