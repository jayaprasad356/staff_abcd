package com.app.staffabcd

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.staffabcd.databinding.ActivityLoginBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var btnLogin: Button
    lateinit var tvSignup:TextView
    lateinit var activity: LoginActivity
    lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        btnLogin = binding.btnLogin
        tvSignup=binding.tvSignup
        activity=this
        session= Session(activity)

        if (Constant.DEBUG){
            binding.etMobile.text?.append("7092923100")
            binding.etPassword.text?.append("sanjay@123")
        }

        btnLogin.setOnClickListener {
         validateAndLogin()
        }
        tvSignup.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        return setContentView(binding.root)

    }
    private fun validateAndLogin() {
        val mobile = binding.etMobile.text?.trim().toString()
        val password = binding.etPassword.text.toString()

        if (!Utils().isValidPhone(mobile)) {
            binding.etMobile.error = getString(R.string.mobile_error)
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.pass_empty)
            return
        }
        doLogin(mobile, password)


    }

    private fun doLogin(mobile: String, password: String) {
        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.MOBILE] =  mobile
            this[Constant.PASSWORD] =  password

        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT).show()

                        val data = jsonObject.getJSONArray("data")
                        session.setData(Constant.STAFF_ID,data.getJSONObject(0).getString(Constant.ID))
                        session.setData(Constant.FIRST_NAME,data.getJSONObject(0).getString(Constant.FIRST_NAME))
                        session.setData(Constant.EMAIL,data.getJSONObject(0).getString(Constant.EMAIL))
                        session.setData(Constant.BANK_ACCOUNT_NUMBER,data.getJSONObject(0).getString(Constant.BANK_ACCOUNT_NUMBER))
                        session.setData(Constant.BANK_NAME,data.getJSONObject(0).getString(Constant.BANK_NAME))
                        session.setData(Constant.IFSC_CODE,data.getJSONObject(0).getString(Constant.IFSC_CODE))
                        session.setData(Constant.MOBILE,data.getJSONObject(0).getString(Constant.MOBILE))
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        // extract other values as needed
                    } else {
                        Toast.makeText(activity,jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, Constant.STAFFS_LOGIN, params, true)

    }


}