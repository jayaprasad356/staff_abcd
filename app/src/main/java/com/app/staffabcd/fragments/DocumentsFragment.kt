package com.app.staffabcd.fragments

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.app.staffabcd.VolleyMultipartRequest
import com.app.staffabcd.databinding.FragmentDocumentsBinding
import com.app.staffabcd.helper.Constant
import com.app.staffabcd.helper.Session
import org.apache.commons.io.IOUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.*


class DocumentsFragment : Fragment() {

    lateinit var binding: FragmentDocumentsBinding

    var aadharFileBytes: ByteArray? = null
    var resumeFileBytes: ByteArray? = null
    var photoFileBytes: ByteArray? = null
    var certificateFileBytes: ByteArray? = null
    lateinit var session: Session

     val REQUEST_CODE_SELECT_IMAGE_FILE = 1001

    lateinit var aadhar: Uri
    lateinit var resume: Uri
    lateinit var photo: Uri
    lateinit var eduCirtificate: Uri
    var tapped: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDocumentsBinding.inflate(inflater, container, false)
        requestStoragePermission()
        session= Session(requireActivity())

        binding.btnUpdate.setOnClickListener {
           // if (validateFields()) {
               // saveProfileAccount(aadhar,resume,photo,eduCirtificate)
           // }
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
            selectImageFile()
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
    private fun selectImageFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooseFileIntent = Intent.createChooser(intent, "Choose an image file")
        startActivityForResult(chooseFileIntent, REQUEST_CODE_SELECT_IMAGE_FILE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SELECT_PDF_FILE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                if (tapped.equals("aadhar")) {
                    aadhar = uri
                    binding.ivAadhar.visibility = View.VISIBLE
                    binding.rlAadhar.visibility= View.GONE
                }
                if (tapped.equals("resume")) {
                   // val filePath: String? = getRealPathFromUri(requireContext(), uri)
                    resume = uri
                    binding.ivResume.visibility = View.VISIBLE
                    binding.rlResume.visibility= View.GONE
                }
                if (tapped.equals("cirtificate")) {
                    val filePath: String? = getRealPathFromUri(requireContext(), uri)
                    eduCirtificate = uri
                    binding.ivCirtificate.visibility = View.VISIBLE
                    binding.rlEduCirtifi.visibility= View.GONE
                }
                // Do something with the selected PDF file
            }
        }else if (requestCode == REQUEST_CODE_SELECT_IMAGE_FILE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                if (tapped.equals("photo")) {
                    photo = uri
                    binding.ivPhoto.visibility = View.VISIBLE
                    binding.rlPhoto.visibility= View.GONE
                }
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
        val salaryDate = binding!!.etSalaryDate.text.toString().trim()

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
        } else if (binding!!.etSalaryDate.text.isNullOrEmpty()) {
            binding!!.etSalaryDate.error = "Please enter Salary Date"
            isValid = false
        }
        if (salaryDate.isEmpty()) {
            binding!!.etSalaryDate.error = "Please enter Salary Date"
            isValid = false
        } else {
            val dateNumber = salaryDate.toIntOrNull()
            if (dateNumber == null || dateNumber !in 1..31) {
                binding!!.etSalaryDate.error = "Please enter a number between 1 to 31"
                isValid = false
            }
        }


        return isValid
    }
//    private fun saveProfileAccount(aadharUri: Uri,resumeUri: Uri,photoUri: Uri,cirtificatURi:Uri) {
//        try {
//            val aadharInputStream: InputStream = requireActivity().contentResolver.openInputStream(aadharUri)!!
//            aadharFileBytes = IOUtils.toByteArray(aadharInputStream)
//
//            val resumeInputStream: InputStream = requireActivity().contentResolver.openInputStream(resumeUri)!!
//            resumeFileBytes = IOUtils.toByteArray(resumeInputStream)
//
//            val photoInputStream: InputStream = requireActivity().contentResolver.openInputStream(photoUri)!!
//            photoFileBytes = IOUtils.toByteArray(photoInputStream)
//
//            val cirtificateInputStream: InputStream = requireActivity().contentResolver.openInputStream(cirtificatURi)!!
//            certificateFileBytes = IOUtils.toByteArray(cirtificateInputStream)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        val cR: ContentResolver = requireActivity().contentResolver
//
//
//        // Get file paths
//        val aadharPath = aadharUri.path
//        val resumePath = resumeUri.path
//        val photoPath = photoUri.path
//        val certificatePath = cirtificatURi.path
//
//// Get content types
//        val aadharType = cR.getType(aadharUri)
//        val resumeType = cR.getType(resumeUri)
//        val photoType = cR.getType(photoUri)
//        val certificateType = cR.getType(cirtificatURi)
//
//// Create File objects
//        val aadharFile = File(aadharPath)
//        val resumeFile = File(resumePath)
//        val photoFile = File(photoPath)
//        val certificateFile = File(certificatePath)
//
//
//        val url: String = "https://demoabcd.graymatterworks.com/api/staffs_document.php"
//        val multipartRequest: VolleyMultipartRequest = object : VolleyMultipartRequest(
//            Request.Method.POST, url,
//            Response.Listener<NetworkResponse> { response ->
//                val resultResponse = String(response.data)
//                Log.d("RESPONSE", resultResponse)
//                Toast.makeText(requireContext(), "" + resultResponse, Toast.LENGTH_SHORT).show()
//                try {
//                    val result = JSONObject(resultResponse)
//                    val status = result.getString("status")
//                    val message = result.getString("message")
//                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//
//
//
//                    //                    if (status.equals(Constant.REQUEST_SUCCESS)) {
//                    //                        // tell everybody you have succed upload image and post strings
//                    //                        Log.i("Messsage", message);
//                    //                    } else {
//                    //                        Log.i("Unexpected", message);
//                    //                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            },
//            Response.ErrorListener { error ->
//                val networkResponse = error.networkResponse
//                var errorMessage = "Unknown error"
//                if (networkResponse == null) {
//                    if (error.javaClass == TimeoutError::class.java) {
//                        errorMessage = "Request timeout"
//                    } else if (error.javaClass == NoConnectionError::class.java) {
//                        errorMessage = "Failed to connect server"
//                    }
//                } else {
//                    val result = String(networkResponse.data)
//                    try {
//                        val response = JSONObject(result)
//                        val status = response.getString("status")
//                        val message = response.getString("message")
//                        Log.e("Error Status", status)
//                        Log.e("Error Message", message)
//                        if (networkResponse.statusCode == 404) {
//                            errorMessage = "Resource not found"
//                        } else if (networkResponse.statusCode == 401) {
//                            errorMessage = "$message Please login again"
//                        } else if (networkResponse.statusCode == 400) {
//                            errorMessage = "$message Check your inputs"
//                        } else if (networkResponse.statusCode == 500) {
//                            errorMessage = "$message Something is getting wrong"
//                        }
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//                Log.i("Error", errorMessage)
//                error.printStackTrace()
//            }) {
//            override fun getParams(): MutableMap<String, String> {
//                val params: MutableMap<String, String> = HashMap()
//                params[Constant.STAFF_ID] = session.getData(Constant.STAFF_ID)
//                params["salary_date"] ="21"
//
//                return params
//            }
//
//
////            override fun getByteData(): Map<String, DataPart> {
////                val params: MutableMap<String, DataPart> = HashMap()
////                params[Constant.AADHAR_CARD] = DataPart(aadharFile.name, aadharFile, aadharType)
////
////
////                params[Constant.RESUME] = DataPart(resumeFile.name, resumeFile.toString(), resumeType)
////
////                // Create a File object from photoFile path
////                val photoFileObject = File(photoPath)
////                params[Constant.PHOTO] = DataPart(photoFileObject.name, photoFileObject.toString(), photoType)
////
////
////                params[Constant.EDUCATION_CERTIFICATE] = DataPart(certificateFile.name, certificateFile.toString(), certificateType)
////                return params
////            }
//
//
//
//        }
//        VolleySingleton.getInstance(requireContext()).addToRequestQueue(multipartRequest)
//    }


}