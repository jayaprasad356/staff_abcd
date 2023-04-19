package com.app.staffabcd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.staffabcd.databinding.ActivityRegistrationBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class RegistrationActivity : AppCompatActivity() {
    var binding: ActivityRegistrationBinding? = null
    private lateinit var btnRegister: Button
    private lateinit var session:Session



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        btnRegister = binding!!.btnRegister
        session=Session(this)


        btnRegister.setOnClickListener {
            if (validateFields()) {
                doRegister()

            }
        }


        binding!!.backimg.setOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }



        return setContentView(binding!!.root)
    }

    private fun doRegister() {
        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.NAME] =  binding!!.etName.text.toString()
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
                        session.setData(Constant.STAFF_ID,data.getJSONObject(0).getString(Constant.ID))
                        session.setData(Constant.NAME,data.getJSONObject(0).getString(Constant.NAME))
                        session.setData(Constant.EMAIL,data.getJSONObject(0).getString(Constant.EMAIL))
                        session.setData(Constant.PASSWORD,data.getJSONObject(0).getString(Constant.PASSWORD))
                        session.setData(Constant.MOBILE,data.getJSONObject(0).getString(Constant.MOBILE))
                        session.setBoolean(Constant.IS_LOGIN,true)
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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
        val email: String = binding!!.etEmail.text!!.trim().toString()
        val mobile: String = binding!!.etMobile.text!!.trim().toString()

        if (binding!!.etName.text.isNullOrEmpty()) {
            binding!!.etName.error = "Please enter your Name"
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

        if (mobile.length != 10) {
            binding!!.etMobile.error = "Please enter valid mobile number"
            isValid = false
        }


        return isValid
    }
}