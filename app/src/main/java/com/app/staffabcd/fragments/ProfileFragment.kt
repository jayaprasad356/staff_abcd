package com.app.staffabcd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        session = Session(requireActivity())

        binding.btnUpload.setOnClickListener {
            if (validateFields()) {
                updateStaffDetails()
            }
        }
        initUi()
        staffDetails()
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            staffDetails()

            swipeRefreshLayout.isRefreshing = false
        }
        return binding.root
    }

    private fun validateFields(): Boolean {
        var isValid = true
        val email: String = binding.etEmail.text!!.trim().toString()
        val mobile: String = binding.etMobile.text!!.trim().toString()

        if (binding.etName.text.isNullOrEmpty()) {
            binding.etName.error = "Please enter your Name"
            isValid = false
        }

        if (!Utils().isValidEmail(email)) {
            binding.etEmail.error = getString(R.string.email_error)
            isValid = false
        }

        if (binding.etPassword.text.isNullOrEmpty()) {
            binding.etPassword.error = "Please enter your password"
            isValid = false
        }

        if (!Utils().isValidPhone(mobile)) {
            binding.etMobile.error = "Please enter valid mobile number"
            isValid = false
        }

        return isValid
    }

    private fun updateStaffDetails() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
            this[Constant.NAME] = binding.etName.text.toString()
            this[Constant.EMAIL] = binding.etEmail.text.toString()
            this[Constant.MOBILE] = binding.etMobile.text.toString()
            this[Constant.PASSWORD] = binding.etPassword.text.toString()

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
                        session.setData(Constant.NAME, data.getString(Constant.NAME))
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

    private fun staffDetails() {
        val params: HashMap<String, String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
        }
        ApiConfig.RequestToVolley({ result, response ->
            if (result) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {

//                        val message = jsonObject.getString("message")
//                        val documentUpload = jsonObject.getInt("document_upload")
//                        val salary = jsonObject.getString("salary")
//                        val incentiveEarn = jsonObject.getString("incentive_earn")
//                        val totalEarnings = jsonObject.getInt("total_earnings")
//                        val totalLeads = jsonObject.getString("total_leads")
//                        val totalJoinings = jsonObject.getString("total_joinings")
                        session.setData(Constant.TOTAL_ACTIVE_USERS, jsonObject.getString(Constant.TOTAL_ACTIVE_USERS))
                        session.setData(Constant.TODAY_JOININGS, jsonObject.getString(Constant.TODAY_JOININGS))
                        session.setData(Constant.TODAY_REFERS, jsonObject.getString(Constant.TODAY_REFERS))
                        session.setData(Constant.TODAY_PERFORMANCE, jsonObject.getString(Constant.TODAY_PERFORMANCE))

                        val userData: JSONObject =
                            jsonObject.getJSONArray(Constant.DATA).getJSONObject(0)
                        session.setData(Constant.ID, userData.getString(Constant.ID))
                        session.setData(Constant.NAME, userData.getString(Constant.NAME))
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
                        session.setData(Constant.INCENTIVE_PERCENTAGE, userData.getString(Constant.INCENTIVE_PERCENTAGE))

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
                            Constant.TOTAL_JOININGS,
                            userData.getString(Constant.SUPPORTS)
                        )
                        session.setData(Constant.TOTAL_LEADS, userData.getString(Constant.LEADS))
                        session.setData(Constant.SALARY, userData.getString(Constant.SALARY))
                        session.setData(
                            Constant.INCENTIVE_EARN,
                            userData.getString(Constant.INCENTIVES)
                        )
                        session.setData(Constant.TOTAL_EARNINGS, userData.getString(Constant.EARN))

                        session.setData(
                            Constant.STAFF_DISPLAY_ID,
                            userData.getString(Constant.STAFF_ID)
                        )
                        session.setData(Constant.DOB, userData.getString(Constant.DOB))



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
        }, requireActivity(), Constant.STAFFS_DETAILS, params, true)

    }


    private fun initUi() {
      binding.etName.setText(session.getData(Constant.NAME).toString())
        binding.etEmail.setText(session.getData(Constant.EMAIL).toString())
        binding.etPassword.setText(session.getData(Constant.PASSWORD).toString())
        binding.etMobile.setText(session.getData(Constant.MOBILE).toString())

    }

}