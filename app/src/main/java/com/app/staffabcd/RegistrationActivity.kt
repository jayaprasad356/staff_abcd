package com.app.staffabcd

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.staffabcd.databinding.ActivityRegistrationBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class RegistrationActivity : AppCompatActivity() {
    var binding: ActivityRegistrationBinding? = null
    lateinit var btnRegister: Button
    lateinit var session:Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        btnRegister = binding!!.btnRegister
        session=Session(this);




        btnRegister.setOnClickListener {
            if (validateFields()) {
                doRegister()

            }
        }



        return setContentView(binding!!.root)
    }

    private fun doRegister() {
        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.FIRST_NAME] =  binding!!.etFirstName.text.toString()
            this[Constant.LAST_NAME] =  binding!!.etLastName.text.toString()
            this[Constant.EMAIL] =  binding!!.etEmail.text.toString()
            this[Constant.MOBILE] =  binding!!.etMobile.text.toString()
            this[Constant.PASSWORD] =  binding!!.etPassword.text.toString()

        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(this,jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT).show()

                        val data = jsonObject.getJSONArray("data")
                        session.setData(Constant.NAME,data.getJSONObject(0).getString(Constant.NAME))
                        session.setData(Constant.EMAIL,data.getJSONObject(0).getString(Constant.EMAIL))
                        session.setData(Constant.PASSWORD,data.getJSONObject(0).getString(Constant.PASSWORD))
                        session.setData(Constant.MOBILE,data.getJSONObject(0).getString(Constant.MOBILE))
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        // extract other values as needed
                    } else {
                        Toast.makeText(this,jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, this, Constant.STAFFS_SIGNUP, params, true)

    }

    private fun validateFields(): Boolean {
        var isValid = true
        var email: String = binding!!.etEmail.text!!.trim().toString()
        var mobile: String = binding!!.etMobile.text!!.trim().toString()

        if (binding!!.etFirstName.text.isNullOrEmpty()) {
            binding!!.etFirstName.error = "Please enter your First Name"
            isValid = false
        }
        if (binding!!.etLastName.text.isNullOrEmpty()) {
            binding!!.etLastName.error = "Please enter your Last Name"
            isValid = false
        }
        if (!Utils().isValidEmail(email)) {
            binding!!.etEmail.error = getString(R.string.email_error)
            isValid = false
        }

        if (binding!!.etPassword.text.isNullOrEmpty()) {
            binding!!.etPassword.error = "Please enter your password"
            isValid = false
        }

        if (!Utils().isValidPhone(mobile)) {
            binding!!.etMobile.error = "Please enter valid mobile number"
            isValid = false
        }


        return isValid
    }




}