package com.example.doctor_app

import android.R.attr
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import com.example.doctor_app.model.User


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        register.setOnClickListener{
            login.visibility = if (login.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
            name.visibility = if (name.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
            register_btn.visibility = if (register_btn.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
            sign_in_text.visibility = if (sign_in_text.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
            register.visibility = if (register.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
        }

        sign_in_text.setOnClickListener{
            login.visibility = if (login.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
            name.visibility = if (name.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
            register_btn.visibility = if (register_btn.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
            sign_in_text.visibility = if (sign_in_text.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
            register.visibility = if (register.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
        }

        register_btn.setOnClickListener{
            val url = "localhost:3000/registerDoctor"
            val gson = Gson()

            val params = HashMap<String, String>()
            params["name"] = name.text.toString();
            params["mail"] = mail.text.toString();
            params["password"] = password.text.toString();
            if(params["name"] != "" && params["mail"] != "" && params["password"] != ""){
                val jsonObject = JSONObject(params as Map<*, *>);
                val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    { response ->
                        // Process the json
                        try {
                            val responseObj = gson.fromJson(response.toString(), User::class.java)
                            val name = responseObj.name
                            val mail = responseObj.mail
                            val success = responseObj.success
                            if (success) {
                                // auto login if registration is successfull
                                Toast.makeText(applicationContext,"Registration successful! Auto login!",Toast.LENGTH_SHORT).show()
                                val loggedInUser = User(name, mail, success)
                                //TODO save loggedInUser
                                //TODO start qr code activity with user data
                            }
                        } catch (e: Exception) {
                            println("Exception: $e")
                        }

                    }, {
                        // Error in request
                        println("Volley error: $it")
                    })
                // Volley request policy, only one time request to avoid duplicate transaction
                request.retryPolicy = DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    // 0 means no retry
                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

                VolleySingleton.getInstance(this).addToRequestQueue(request)

            }else{
                Toast.makeText(applicationContext,"Fill all fields!",Toast.LENGTH_SHORT).show()
            }
        }

        login.setOnClickListener {
            val url = "http://localhost:3000/loginDoctor"
            val gson = Gson()
            val params = HashMap<String, String>()
            params["mail"] = mail.text.toString();
            params["password"] = password.text.toString();
            if(params["mail"] != "" && params["password"] != "") {
                val jsonObject = JSONObject(params as Map<*, *>);
                val request = JsonObjectRequest(Request.Method.GET, url, jsonObject,
                    { response ->
                        // Process the json
                        try {
                            val responseObj = gson.fromJson(response.toString(), User::class.java)
                            val name = responseObj.name
                            val mail = responseObj.mail
                            val success = responseObj.success
                            if (success) {
                                Toast.makeText(applicationContext,"Login successful!",Toast.LENGTH_SHORT).show()
                                val loggedInUser = User(name, mail, success)
                                //TODO save loggedInUser
                                //TODO start qr code activity with user data
                            }
                        } catch (e: Exception) {
                            println("Exception: $e")
                        }

                    }, {
                        // Error in request
                        println("Volley error: $it")
                    })
                // Volley request policy, only one time request to avoid duplicate transaction
                request.retryPolicy = DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    // 0 means no retry
                    0, // DefaultRetryPolicy.DEFAULT_MAX_RETRIES = 2
                    1f // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )

                VolleySingleton.getInstance(this).addToRequestQueue(request)

            }else{
                Toast.makeText(applicationContext,"Fill all fields!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}