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
    lateinit var binding: FragmentProfileBinding
    lateinit var session: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        session = Session(requireActivity())

        binding.btnUpload.setOnClickListener {
            if (validateFields()) {
                updateStaffDetails()
            }
        }
        initUi()
        staffProfileDetails()

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
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
            this[Constant.FIRST_NAME] = binding!!.etFirstName.text.toString()
            this[Constant.LAST_NAME] = binding!!.etLastName.text.toString()
            this[Constant.EMAIL] = binding!!.etEmail.text.toString()
            this[Constant.MOBILE] = binding!!.etMobile.text.toString()
            this[Constant.PASSWORD] = binding!!.etPassword.text.toString()

        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(
                            requireContext(),
                            jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        val data = jsonObject.getJSONArray("data").getJSONObject(0)
                        session.setData(Constant.STAFF_ID, data.getString(Constant.ID))
                        session.setData(Constant.EMAIL, data.getString(Constant.EMAIL))
                        session.setData(Constant.FIRST_NAME, data.getString(Constant.FIRST_NAME))
                        session.setData(Constant.LAST_NAME, data.getString(Constant.LAST_NAME))

                        session.setData(Constant.PASSWORD, data.getString(Constant.PASSWORD))
                        session.setData(Constant.MOBILE, data.getString(Constant.MOBILE))
                        //  session.setData(Constant.ADDRESS, data.getString(Constant.ADDRESS))
                        session.setData(
                            Constant.BANK_ACCOUNT_NUMBER,
                            data.getString(Constant.BANK_ACCOUNT_NUMBER)
                        )
                        session.setData(Constant.IFSC_CODE, data.getString(Constant.IFSC_CODE))
                        session.setData(Constant.BANK_NAME, data.getString(Constant.BANK_NAME))
                        session.setData(Constant.BRANCH, data.getString(Constant.BRANCH))
                        session.setData(Constant.AADHAR_CARD, data.getString(Constant.AADHAR_CARD))
                        session.setData(Constant.RESUME, data.getString(Constant.RESUME))
                        session.setData(Constant.PHOTO, data.getString(Constant.PHOTO))
                        session.setData(
                            Constant.EDUCATION_CERTIFICATE,
                            data.getString(Constant.EDUCATION_CERTIFICATE)
                        )
                        session.setData(Constant.SALARY_DATE, data.getString(Constant.SALARY_DATE))
                        initUi()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.UPDATE_STAFF, params, true)

    }

    private fun staffProfileDetails() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        val userData: JSONObject =
                            jsonObject.getJSONArray(Constant.DATA).getJSONObject(0)
                        session.setData(Constant.ID, userData.getString(Constant.ID))
                        session.setData(
                            Constant.FIRST_NAME,
                            userData.getString(Constant.FIRST_NAME)
                        )
                        session.setData(Constant.LAST_NAME, userData.getString(Constant.LAST_NAME))
                        session.setData(Constant.EMAIL, userData.getString(Constant.EMAIL))
                        session.setData(Constant.PASSWORD, userData.getString(Constant.PASSWORD))
                        session.setData(Constant.MOBILE, userData.getString(Constant.MOBILE))
                        session.setData(
                            Constant.BANK_ACCOUNT_NUMBER,
                            userData.getString(Constant.BANK_ACCOUNT_NUMBER)
                        )
                        session.setData(Constant.IFSC_CODE, userData.getString(Constant.IFSC_CODE))
                        session.setData(Constant.BANK_NAME, userData.getString(Constant.BANK_NAME))
                        session.setData(Constant.BRANCH, userData.getString(Constant.BRANCH))
                        session.setData(
                            Constant.AADHAR_CARD,
                            userData.getString(Constant.AADHAR_CARD)
                        )
                        session.setData(Constant.RESUME, userData.getString(Constant.RESUME))
                        session.setData(Constant.PHOTO, userData.getString(Constant.PHOTO))
                        session.setData(
                            Constant.EDUCATION_CERTIFICATE,
                            userData.getString(Constant.EDUCATION_CERTIFICATE)
                        )
                        session.setData(Constant.JOIN_DATE, userData.getString(Constant.JOIN_DATE))
                        session.setData(
                            Constant.SALARY_DATE,
                            userData.getString(Constant.SALARY_DATE)
                        )
                        session.setData(Constant.BRANCH_ID, userData.getString(Constant.BRANCH_ID))
                        session.setData(Constant.ROLE, userData.getString(Constant.ROLE))
                        session.setData(Constant.BALANCE, userData.getString(Constant.BALANCE))
                        session.setData(Constant.STATUS, userData.getString(Constant.STATUS))
                        session.setData(
                            Constant.STAFF_DISPLAY_ID,
                            userData.getString(Constant.STAFF_ID)
                        )
                        session.setData(Constant.FAMILY1, userData.getString(Constant.FAMILY1))
                        session.setData(Constant.FAMILY2, userData.getString(Constant.FAMILY2))
                        initUi()


                        // extract other values as needed
                    } else {
                        Toast.makeText(
                            requireContext(), jsonObject.getString(Constant.MESSAGE).toString(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.MY_DETAILS, params, true)

    }

    private fun initUi() {
      binding.etFirstName.setText(session.getData(Constant.FIRST_NAME).toString())
        binding.etLastName.setText(session.getData(Constant.LAST_NAME).toString())
        binding.etEmail.setText(session.getData(Constant.EMAIL).toString())
        binding.etPassword.setText(session.getData(Constant.PASSWORD).toString())
        binding.etMobile.setText(session.getData(Constant.MOBILE).toString())

    }

}