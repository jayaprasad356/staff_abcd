package com.app.staffabcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.app.staffabcd.adapter.TransactionAdapter
import com.app.staffabcd.databinding.ActivityLoginBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import com.app.staffabcd.model.Transanction
import com.google.gson.Gson
import org.json.JSONArray
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
            binding.etMobile.text?.append("7545787012")
            binding.etPassword.text?.append("Sanjay@123")
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
                    val jsondataObject=jsonObject.getJSONArray("data")
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
//                        session.setData(Constant.NAME,jsondataObject.getString(Constant.NAME))
//                        session.setData(Constant.EMAIL,jsondataObject.getString(Constant.EMAIL))
//                        session.setData(Constant.BANK_ACCOUNT_NUMBER,jsondataObject.getString(Constant.BANK_ACCOUNT_NUMBER))
//                        session.setData(Constant.BANK_NAME,jsondataObject.getString(Constant.BANK_NAME))
//                        session.setData(Constant.IFSC_CODE,jsondataObject.getString(Constant.IFSC_CODE))
//                        session.setData(Constant.MOBILE,jsondataObject.getString(Constant.MOBILE))
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, activity, Constant.STAFFS_LOGIN, params, true)

    }


}