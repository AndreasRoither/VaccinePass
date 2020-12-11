package com.example.doctor_app.certificate

import EditTextDatePicker
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.doctor_app.R
import com.example.doctor_app.databinding.ActivityVaccinationUserDataBinding
import com.example.doctor_app.model.User
import com.example.doctor_app.model.VaccineInfo
import com.example.doctor_app.qrcode.QrCode
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class VaccinationUserData : AppCompatActivity() {

    private lateinit var binding: ActivityVaccinationUserDataBinding
    private val viewModel = VaccineUserVM()
    private lateinit var doctor: User

    private val items = listOf("never", "0.5 year", "1 year", "2 years", "5 years")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gson = Gson()
        doctor = gson.fromJson(intent.getStringExtra("user"), User::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_vaccination_user_data)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        initView()
    }

    private fun initView() {
        EditTextDatePicker(this, binding.vaccineDate.id)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        binding.expires.setAdapter(adapter)
        binding.vaccineDate.setText(dateFormat.format(Date()))
//        binding.vaccineDate.setText("aasdfasdfdfhf")
        binding.generateQr.setOnClickListener(this::onButtonClick)
    }

    private fun onButtonClick(v: View) {
        val vaccineInfo = VaccineInfo(
            binding.userId.text.toString(),
            binding.vaccine.text.toString(),
            binding.vaccineDate.text.toString(),
            binding.expires.text.toString(),
            doctor.mail,
            doctor.name
        )

        val gson = Gson()

        val intent = Intent(this, QrCode::class.java)
        intent.putExtra(VACCINE_INFO_KEY, gson.toJson(vaccineInfo).toString())
        startActivity(intent)
    }

    companion object {
        const val VACCINE_INFO_KEY = "vaccineInfo";
    }
}