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
import com.app.staffabcd.databinding.FragmentWithdrawalBinding


class ProfileFragment : Fragment() {
lateinit var binding:FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.btnUpload.setOnClickListener {
            if (validateFields()){
                Toast.makeText(activity,"updated success",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }
    private fun validateFields(): Boolean {
        var isValid = true
        var email: String = binding!!.etEmail.text!!.trim().toString()
        var mobile: String = binding!!.etMobile.text!!.trim().toString()

        if (binding!!.etName.text.isNullOrEmpty()) {
            binding!!.etName.error = "Please enter your name"
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
}