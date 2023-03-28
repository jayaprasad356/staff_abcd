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
        binding!!.rlAadhar.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_CODE
                )
            } else {
                tapped = "aadhar"
                launchGallery()
            }

        }
        binding!!.rlResume.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_CODE
                )
            } else {
                tapped = "resume"
                launchGallery()
            }

        }
        binding!!.rlPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_CODE
                )
            } else {
                tapped = "photo"
                launchGallery()
            }

        }
        binding!!.rlEduCirtifi.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_CODE
                )
            } else {
                tapped = "cirtificate"
                launchGallery()
            }

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

        if (binding!!.etAddress.text.isNullOrEmpty()) {
            binding!!.etAddress.error = "Please enter your address"
            isValid = false
        }

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
        if (aadhar == null) {
            Toast.makeText(this, "Please upload Aadhar", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (resume == null) {
            Toast.makeText(this, "Please upload Resume", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (photo == null) {
            Toast.makeText(this, "Please upload Photo", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (eduCirtificate == null) {
            Toast.makeText(this, "Please upload Educational Cirtificate", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }


    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra("test", "documentType")
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            if (uri != null) {
                createImageData(uri)
                if (imageData != null) {
                    if (tapped.equals("aadhar")) {
                        aadhar = imageData
                        binding!!.ivAadhar.visibility = View.VISIBLE
                    }
                    if (tapped.equals("resume")) {
                        resume = imageData
                        binding!!.ivResume.visibility = View.VISIBLE
                    }
                    if (tapped.equals("photo")) {
                        photo = imageData
                        binding!!.ivPhoto.visibility = View.VISIBLE
                    }
                    if (tapped.equals("cirtificate")) {
                        eduCirtificate = imageData
                        binding!!.ivCirtificate.visibility = View.VISIBLE
                    }
                    Toast.makeText(
                        this,
                        "Selected image with document type $tapped",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    //    @Throws(IOException::class)
//    private fun createImageData(uri: Uri) {
//        val inputStream = contentResolver.openInputStream(uri)
//        inputStream?.buffered()?.use {
//            imageData = it.readBytes()
//        }
//
//    }
    private fun createImageData(uri: Uri) {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            imageData = File(filePath)
            cursor.close()
        } else {
            imageData = null
        }
    }

}