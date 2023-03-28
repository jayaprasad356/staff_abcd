package com.app.staffabcd.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.app.staffabcd.R
import com.app.staffabcd.Utils
import com.app.staffabcd.databinding.FragmentBankDetailBinding


class BankDetailFragment : Fragment() {

lateinit var binding:FragmentBankDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentBankDetailBinding.inflate(inflater, container, false)

        binding.btnUpdate.setOnClickListener {
            if (validateFields()){
                Toast.makeText(activity,"BankDetails updated done",Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
        }

        return binding.root
    }

    private fun validateFields(): Boolean {
        var isValid = true

        if (binding!!.etBank.text.isNullOrEmpty()) {
            binding!!.etBank.error = "Please enter your bank account number"
            isValid = false
        }

        if (binding!!.etIfsc.text.isNullOrEmpty()) {
            binding!!.etIfsc.error = "Please enter your IFSC code"
            isValid = false
        }
        if (binding!!.etBankName.text.isNullOrEmpty()) {
            binding!!.etBankName.error = "Please enter your Bank Name"
            isValid = false
        }
        if (binding!!.etBranch.text.isNullOrEmpty()) {
            binding!!.etBranch.error = "Please enter your Branch Name"
            isValid = false
        }


        return isValid
    }

}