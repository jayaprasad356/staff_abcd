package com.app.staffabcd.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.staffabcd.R
import com.app.staffabcd.Utils
import com.app.staffabcd.databinding.FragmentProfileBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import org.json.JSONException
import org.json.JSONObject


class ProfileFragment : Fragment() {
lateinit var binding:FragmentProfileBinding
lateinit var session:Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        session= Session(requireActivity())

        binding.btnUpload.setOnClickListener {
            if (validateFields()){
                updateStaffDetails()
            }
        }

        return binding.root
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
    private fun updateStaffDetails() {
        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] =  session.getData(Constant.STAFF_ID)
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
                        Toast.makeText(requireContext(),jsonObject.getString(Constant.MESSAGE).toString(),Toast.LENGTH_SHORT).show()
                        val data = jsonObject.getJSONArray("data").getJSONObject(0)
                        session.setData(Constant.STAFF_ID, data.getString(Constant.ID))
                        session.setData(Constant.NAME, data.getString(Constant.NAME))
                        session.setData(Constant.EMAIL, data.getString(Constant.EMAIL))
                        session.setData(Constant.PASSWORD, data.getString(Constant.PASSWORD))
                        session.setData(Constant.MOBILE, data.getString(Constant.MOBILE))
                      //  session.setData(Constant.ADDRESS, data.getString(Constant.ADDRESS))
                        session.setData(Constant.BANK_ACCOUNT_NUMBER, data.getString(Constant.BANK_ACCOUNT_NUMBER))
                        session.setData(Constant.IFSC_CODE, data.getString(Constant.IFSC_CODE))
                        session.setData(Constant.BANK_NAME, data.getString(Constant.BANK_NAME))
                        session.setData(Constant.BRANCH, data.getString(Constant.BRANCH))
                        session.setData(Constant.AADHAR_CARD, data.getString(Constant.AADHAR_CARD))
                        session.setData(Constant.RESUME, data.getString(Constant.RESUME))
                        session.setData(Constant.PHOTO, data.getString(Constant.PHOTO))
                        session.setData(Constant.EDUCATION_CERTIFICATE, data.getString(Constant.EDUCATION_CERTIFICATE))
                        session.setData(Constant.SALARY_DATE, data.getString(Constant.SALARY_DATE))
                    } else {
                        Toast.makeText(requireContext(),jsonObject.getString(Constant.MESSAGE).toString(),Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.UPDATE_STAFF, params, true)

    }

}