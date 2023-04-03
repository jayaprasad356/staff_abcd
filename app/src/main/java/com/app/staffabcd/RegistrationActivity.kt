package com.app.staffabcd

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.app.staffabcd.databinding.ActivityRegistrationBinding
import java.io.File

class RegistrationActivity : AppCompatActivity() {
    var binding: ActivityRegistrationBinding? = null
    lateinit var btnRegister: Button
    private val IMAGE_PICK_CODE = 999
    private var imageData: File? = null
    private var aadhar: File? = null
    private var resume: File? = null
    private var photo: File? = null
    private var eduCirtificate: File? = null

    private val PERMISSION_CODE = 100
    var tapped: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        btnRegister = binding!!.btnRegister
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        }



        btnRegister.setOnClickListener {
            if (validateFields()) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }



        return setContentView(binding!!.root)
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




}