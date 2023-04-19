package com.app.staffabcd.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.app.staffabcd.databinding.FragmentBankDetailBinding
import com.app.staffabcd.helper.ApiConfig
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import org.json.JSONException
import org.json.JSONObject


class BankDetailFragment : Fragment() {

lateinit var binding:FragmentBankDetailBinding
lateinit var session:Session
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBankDetailBinding.inflate(inflater, container, false)
        session= Session(requireActivity())
initCall()
        binding.btnUpdate.setOnClickListener {
            if (validateFields()){
                updateStaffDetails()

            }
        }

        return binding.root
    }

    private fun initCall() {
        binding.etBankAccountNumber.setText(session.getData(Constant.BANK_ACCOUNT_NUMBER))
        binding.etIfsc.setText(session.getData(Constant.IFSC_CODE))
        binding.etBankName.setText(session.getData(Constant.BANK_NAME))
        binding.etBranch.setText(session.getData(Constant.BRANCH))

    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (binding.etBankAccountNumber.text.isNullOrEmpty()) {
            binding.etBankAccountNumber.error = "Please enter your bank account number"
            isValid = false
        }

        if (binding.etIfsc.text.isNullOrEmpty()) {
            binding.etIfsc.error = "Please enter your IFSC code"
            isValid = false
        }
        if (binding.etBankName.text.isNullOrEmpty()) {
            binding.etBankName.error = "Please enter your Bank Name"
            isValid = false
        }
        if (binding.etBranch.text.isNullOrEmpty()) {
            binding.etBranch.error = "Please enter your Branch Name"
            isValid = false
        }


        return isValid
    }
    private fun updateStaffDetails() {
        val params : HashMap<String,String> = hashMapOf()
        params.apply {
            this[Constant.STAFF_ID] =  session.getData(Constant.STAFF_ID)
            this[Constant.IFSC_CODE] =  binding.etIfsc.text.toString()
            this[Constant.BANK_NAME] =  binding.etBankName.text.toString()
            this[Constant.BRANCH] =  binding.etBranch.text.toString()
            this[Constant.BANK_ACCOUNT_NUMBER] =  binding.etBankAccountNumber.text.toString()

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
                       // session.setData(Constant.ADDRESS, data.getString(Constant.ADDRESS))
                        session.setData(Constant.BANK_ACCOUNT_NUMBER, data.getString(Constant.BANK_ACCOUNT_NUMBER))
                        session.setData(Constant.IFSC_CODE, data.getString(Constant.IFSC_CODE))
                        session.setData(Constant.BANK_NAME, data.getString(Constant.BANK_NAME))
                        session.setData(Constant.BRANCH, data.getString(Constant.BRANCH))
                        session.setData(Constant.AADHAR_CARD, data.getString(Constant.AADHAR_CARD))
                        session.setData(Constant.RESUME, data.getString(Constant.RESUME))
                        session.setData(Constant.PHOTO, data.getString(Constant.PHOTO))
                        session.setData(Constant.EDUCATION_CERTIFICATE, data.getString(Constant.EDUCATION_CERTIFICATE))
                        session.setData(Constant.SALARY_DATE, data.getString(Constant.SALARY_DATE))
                        initCall()
                    } else {
                        Toast.makeText(requireContext(),jsonObject.getString(Constant.MESSAGE).toString(),Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }, requireActivity(), Constant.UPDATE_STAFFBANK, params, true)

    }

}