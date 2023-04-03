package com.app.staffabcd.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.app.staffabcd.databinding.FragmentDocumentsBinding
import java.io.File


class DocumentsFragment : Fragment() {

    lateinit var binding: FragmentDocumentsBinding


    private var aadhar: File? = null
    private var resume: File? = null
    private var photo: File? = null
    private var eduCirtificate: File? = null
    var tapped: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        requestStoragePermission()

        binding.btnUpdate.setOnClickListener {
            if (validateFields()) {
                Toast.makeText(requireContext(), "Documents Updated Success", Toast.LENGTH_SHORT).show()
            }
        }

        binding.rlAadhar.setOnClickListener {
            tapped = "aadhar"
            selectPdfFile()
        }
        binding.rlResume.setOnClickListener {
            tapped = "resume"
            selectPdfFile()
        }
        binding.rlPhoto.setOnClickListener {
            tapped = "photo"
            selectPdfFile()
        }
        binding.rlEduCirtifi.setOnClickListener {
            tapped = "cirtificate"
            selectPdfFile()
        }


        return binding.root
    }

    private fun selectPdfFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        val chooseFileIntent = Intent.createChooser(intent, "Choose a PDF file")
        startActivityForResult(chooseFileIntent, REQUEST_CODE_SELECT_PDF_FILE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_PDF_FILE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                if (tapped.equals("aadhar")) {
                    val filePath: String? = getRealPathFromUri(requireContext(), uri)
                    aadhar = filePath?.let { File(it) }
                    binding.ivAadhar.visibility = View.VISIBLE
                    binding.rlAadhar.visibility= View.GONE
                }
                if (tapped.equals("resume")) {
                    val filePath: String? = getRealPathFromUri(requireContext(), uri)
                    resume = filePath?.let { File(it) }
                    binding.ivResume.visibility = View.VISIBLE
                    binding.rlResume.visibility= View.GONE
                }
                if (tapped.equals("photo")) {
                    val filePath: String? = getRealPathFromUri(requireContext(), uri)
                    photo = filePath?.let { File(it) }
                    binding.ivPhoto.visibility = View.VISIBLE
                    binding.rlPhoto.visibility= View.GONE
                }
                if (tapped.equals("cirtificate")) {
                    val filePath: String? = getRealPathFromUri(requireContext(), uri)
                    eduCirtificate = filePath?.let { File(it) }
                    binding.ivCirtificate.visibility = View.VISIBLE
                    binding.rlEduCirtifi.visibility= View.GONE
                }
                // Do something with the selected PDF file
            }
        }
    }

    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.let {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                val filePath = it.getString(columnIndex)
                it.close()
                return filePath
            } else {
                it.close()
            }
        }
        return null
    }




    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        // Permission is granted
                    } else {
                        // Permission denied
                    }
                }
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            // Permission is granted by default in Android 10 and lower
        }
    }
    companion object {
        private const val REQUEST_CODE_SELECT_PDF_FILE = 1
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
        if (binding!!.etMobileFamily.text.isNullOrEmpty()) {
            binding!!.etMobileFamily.error = "Please enter Mobile number of family member"
            isValid = false
        }
        if (binding!!.etMobileFriend.text.isNullOrEmpty()) {
            binding!!.etMobileFriend.error = "Please enter Mobile number of Friend"
            isValid = false
        }
        if (aadhar == null) {
            Toast.makeText(requireContext(), "Please upload Aadhar", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (resume == null) {
            Toast.makeText(requireContext(), "Please upload Resume", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (photo == null) {
            Toast.makeText(requireContext(), "Please upload Photo", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (eduCirtificate == null) {
            Toast.makeText(requireContext(), "Please upload Educational Cirtificate", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }
}