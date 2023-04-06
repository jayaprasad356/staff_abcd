package com.app.staffabcd

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle

import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.staffabcd.databinding.ActivityRegistrationBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class RegistrationActivity : AppCompatActivity() {
    var binding: ActivityRegistrationBinding? = null
    private lateinit var btnRegister: Button
    private lateinit var session:Session

    private val joinDateListener = DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, dayOfMonth)
        val selectedDate = selectedCalendar.time

        val today = Date() // Get the current date
        if (selectedDate.before(today) && !isSameDay(selectedDate, today)) {
            binding?.etJoinDate?.text?.clear()
            Toast.makeText(this, "Please select a valid date", Toast.LENGTH_SHORT).show()
        } else {
            binding?.etJoinDate?.setText(String.format("%tF", selectedDate))
        }
    }


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

        binding?.etJoinDate?.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                joinDateListener,
                year,
                month,
                dayOfMonth
            )
            datePickerDialog.show()
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
            this[Constant.FIRST_NAME] =  binding!!.etFirstName.text.toString()
            this[Constant.LAST_NAME] =  binding!!.etLastName.text.toString()
            this[Constant.EMAIL] =  binding!!.etEmail.text.toString()
            this[Constant.MOBILE] =  binding!!.etMobile.text.toString()
            this[Constant.PASSWORD] =  binding!!.etPassword.text.toString()
            this[Constant.JOIN_DATE] =  binding!!.etJoinDate.text.toString()

        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(this,jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT).show()

                        val data = jsonObject.getJSONArray("data")
                        session.setData(Constant.FIRST_NAME,data.getJSONObject(0).getString(Constant.FIRST_NAME))
                        session.setData(Constant.LAST_NAME,data.getJSONObject(0).getString(Constant.LAST_NAME))
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
    }    // Check if two dates are the same day
    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
    }
}