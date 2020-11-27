package com.example.doctor_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        login_view_btn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            // start your next activity
            startActivity(intent)
        }

        qr_code_view_btn.setOnClickListener{
            //TODO,
            //val intent = Intent(this, LoginActivity.kt::class.java)
            // start your next activity
            //startActivity(intent)
        }


    }
}