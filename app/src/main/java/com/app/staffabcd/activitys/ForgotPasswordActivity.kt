package com.app.staffabcd.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.staffabcd.R
import com.app.staffabcd.RegistrationActivity
import com.app.staffabcd.Utils
import com.app.staffabcd.databinding.ActivityForgotPasswordBinding
import com.app.staffabcd.databinding.ActivityLoginBinding

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)

        binding.btnConfirm.setOnClickListener {
            validateAndLogin()
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
        val intent = Intent(this, OtpActivity::class.java)
        startActivity(intent)
        finish()
    }

}