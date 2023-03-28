package com.app.staffabcd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.app.staffabcd.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var btnLogin: Button
    lateinit var tvSignup:TextView
    lateinit var activity: LoginActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        btnLogin = binding.btnLogin
        tvSignup=binding.tvSignup
        activity=this

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
        val email = binding.etEmail.text?.trim().toString()
        val password = binding.etPassword.text.toString()

        if (!Utils().isValidEmail(email)) {
            binding.etEmail.error = getString(R.string.email_error)
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = getString(R.string.pass_empty)
            return
        }

        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }


}