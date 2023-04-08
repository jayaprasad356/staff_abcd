package com.app.staffabcd

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.staffabcd.activitys.ForgotPasswordActivity
import com.app.staffabcd.databinding.ActivityLoginBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
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
            validateAndLogin()
        }

        btnLogin.setOnClickListener {
         validateAndLogin()
        }
        tvSignup.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
        binding.tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
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

                        val userData: JSONObject = data.getJSONObject(0)
                        session.setData(Constant.STAFF_ID, userData.getString(Constant.ID))
                        session.setData(Constant.FIRST_NAME, userData.getString(Constant.FIRST_NAME))
                        session.setData(Constant.LAST_NAME, userData.getString(Constant.LAST_NAME))
                        session.setData(Constant.EMAIL, userData.getString(Constant.EMAIL))
                        session.setData(Constant.PASSWORD, userData.getString(Constant.PASSWORD))
                        session.setData(Constant.MOBILE, userData.getString(Constant.MOBILE))
                        session.setData(Constant.BANK_ACCOUNT_NUMBER, userData.getString(Constant.BANK_ACCOUNT_NUMBER))
                        session.setData(Constant.IFSC_CODE, userData.getString(Constant.IFSC_CODE))
                        session.setData(Constant.BANK_NAME, userData.getString(Constant.BANK_NAME))
                        session.setData(Constant.BRANCH, userData.getString(Constant.BRANCH))
                        session.setData(Constant.AADHAR_CARD, userData.getString(Constant.AADHAR_CARD))
                        session.setData(Constant.RESUME, userData.getString(Constant.RESUME))
                        session.setData(Constant.PHOTO, userData.getString(Constant.PHOTO))
                        session.setData(Constant.EDUCATION_CERTIFICATE, userData.getString(Constant.EDUCATION_CERTIFICATE))
                        session.setData(Constant.JOIN_DATE, userData.getString(Constant.JOIN_DATE))
                        session.setData(Constant.SALARY_DATE, userData.getString(Constant.SALARY_DATE))
                        session.setData(Constant.BRANCH_ID, userData.getString(Constant.BRANCH_ID))
                        session.setData(Constant.ROLE, userData.getString(Constant.ROLE))
                        session.setData(Constant.BALANCE, userData.getString(Constant.BALANCE))
                        session.setData(Constant.STATUS, userData.getString(Constant.STATUS))
                        session.setData(Constant.STAFF_DISPLAY_ID, userData.getString(Constant.STAFF_ID))

                        session.setBoolean(Constant.IS_LOGIN,true)
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