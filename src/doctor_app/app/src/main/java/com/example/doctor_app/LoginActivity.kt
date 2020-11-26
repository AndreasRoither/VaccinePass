package com.example.doctor_app

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

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
            val url = "localhost:3000/register"

            val params = HashMap<String, String>()
            params["name"] = name.text.toString();
            params["mail"] = mail.text.toString();
            params["password"] = password.text.toString();
            // TODO check if everything not null
            val jsonObject = JSONObject(params as Map<*, *>);
            val request = sendRequest(url, jsonObject)
            VolleySingleton.getInstance(this).addToRequestQueue(request)
        }

        login.setOnClickListener {
            val url = "localhost:3000/login"

            val params = HashMap<String, String>()
            params["mail"] = mail.text.toString();
            params["password"] = password.text.toString();
            // TODO check if everything not null
            val jsonObject = JSONObject(params as Map<*, *>);
            val request = sendRequest(url, jsonObject)
            VolleySingleton.getInstance(this).addToRequestQueue(request)
        }
    }
}

fun toggleVisibilities(){

}

fun sendRequest(url: String, json: JSONObject) : JsonObjectRequest {
    val request = JsonObjectRequest(Request.Method.POST,url,json,
        { response ->
            // Process the json
            try {
                println("Response: $response")
                //TODO get response object
            }catch (e:Exception){
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

    // Add the volley post request to the request queue
    return request;

}