package com.app.staffabcd.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.app.staffabcd.R;
import com.app.staffabcd.VolleyMultipartRequest;
import com.app.staffabcd.databinding.FragmentDocumentBinding;
import com.app.staffabcd.helper.ApiConfig;
import com.app.staffabcd.helper.Constant;
import com.app.staffabcd.helper.ProgressDisplay;
import com.app.staffabcd.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DocumentFragment extends Fragment {
    FragmentDocumentBinding binding;
    Session session;
    String tapped = "";
    private RequestQueue rQueue;
    ScrollView scrollView;
    RelativeLayout pendingLayout;
    RelativeLayout SuccessLayout;

    String[] dates = {"Select Salary Date", "1", "2",
            "3", "4",
            "5", "6", "7", "8", "9", "10", "11",
            "12", "13", "14", "15", "16", "17",
            "18", "19", "20", "21", "22", "23",
            "24", "25", "26", "27", "28", "29", "30", "31"};
    String selectedOption;
    static int PICK_FILE_REQUEST = 1;

    Uri aadhar, resume, photo, eduCirtificate;
    ProgressDisplay progressDisplay;
    public DocumentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDocumentBinding.inflate(inflater, container, false);
        session = new Session(getActivity());
        requestStoragePermission();
        Spinner mySpinner = binding.datesSpinner;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, dates);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(adapter);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedOption = dates[position];
                if (selectedOption.equals("Select Salary Date")) {
                    Toast.makeText(requireContext(), "Please Select Valid Salary date", Toast.LENGTH_SHORT).show();
                    selectedOption = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });


//        binding.etSalaryDate.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Do something after text is changed
//                String salaryDate = s.toString().trim();
//                if (!salaryDate.isEmpty()) {
//                    int dateNumber = Integer.parseInt(salaryDate);
//                    if (dateNumber < 1 || dateNumber > 31) {
//                        binding.etSalaryDate.setError("Please enter a number between 1 to 31");
//                    } else {
//                        binding.etSalaryDate.setError(null);
//                    }
//                } else {
//                    binding.etSalaryDate.setError("Please enter Salary Date");
//                }
//            }
//        });
        if (!(session.getData(Constant.STAFF_DISPLAY_ID).isEmpty())) {
            binding.successLayout.setVisibility(View.VISIBLE);
            binding.pendingLayout.setVisibility(View.GONE);
            binding.scrollView.setVisibility(View.GONE);
        } else if (session.getData(Constant.AADHAR_CARD).isEmpty()) {
            binding.successLayout.setVisibility(View.GONE);
            binding.pendingLayout.setVisibility(View.GONE);
            binding.scrollView.setVisibility(View.VISIBLE);
        } else if (!session.getData(Constant.AADHAR_CARD).isEmpty() && session.getData(Constant.STAFF_DISPLAY_ID).isEmpty()) {
            binding.successLayout.setVisibility(View.GONE);
            binding.pendingLayout.setVisibility(View.VISIBLE);
            binding.scrollView.setVisibility(View.GONE);
        }

        initCall();
        binding.btnUpdate.setOnClickListener(v -> {
            if (validateFields()) {
                processWithUri(aadhar, resume, eduCirtificate, photo);
            }


        });

        if (session.getData(Constant.DOCUMENT_UPLOAD).equals("1")) {
            notAllowUploadDoc();
        }

        binding.rlAadhar.setOnClickListener(v -> {
            tapped = "aadhar";
            selectPdfFile();
        });
        binding.rlResume.setOnClickListener(v -> {
            tapped = "resume";
            selectPdfFile();
        });
        binding.rlPhoto.setOnClickListener(v -> {
            tapped = "photo";
            selectPhoto();
        });
        binding.rlEduCirtifi.setOnClickListener(v -> {
            tapped = "cirtificate";
            selectPdfFile();
        });

        binding.etDateOfBirth.setOnClickListener(v -> {
            // Set the initial date to 01/01/2000
            Calendar c = Calendar.getInstance();
            c.set(2000, 0, 1);
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and show it
            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        // Handle the date selection
                        String dateString = String.format(Locale.US, "%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);

                        binding.etDateOfBirth.setText(dateString);
                        // Update your UI with the selected date here
                    }, year, month, day);
            datePickerDialog.show();
        });


//        binding.etBank.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Create an Intent to open the file picker
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("*/*");
//
//// Start the file picker activity
//                startActivityForResult(intent, PICK_FILE_REQUEST);
//
//            }
//        });


        return binding.getRoot();
    }

    private void notAllowUploadDoc() {
        binding.btnUpdate.setEnabled(false);
        binding.ivResume.setVisibility(View.VISIBLE);
        binding.rlResume.setVisibility(View.GONE);
        binding.ivAadhar.setVisibility(View.VISIBLE);
        binding.rlAadhar.setVisibility(View.GONE);
        binding.ivCirtificate.setVisibility(View.VISIBLE);
        binding.rlEduCirtifi.setVisibility(View.GONE);
        binding.ivPhoto.setVisibility(View.VISIBLE);
        binding.rlPhoto.setVisibility(View.GONE);
        binding.etBank.setEnabled(false);
        binding.etBankName.setEnabled(false);
        binding.etBranch.setEnabled(false);
        binding.etIfsc.setEnabled(false);
        binding.datesSpinner.setEnabled(false);
        binding.etMobileFamilyTwo.setEnabled(false);
        binding.etMobileFamily.setEnabled(false);
        binding.etDateOfBirth.setEnabled(false);

    }

    private void initCall() {
        binding.etBank.setText(session.getData(Constant.BANK_ACCOUNT_NUMBER));
        binding.etBankName.setText(session.getData(Constant.BANK_NAME));
        binding.etIfsc.setText(session.getData(Constant.IFSC_CODE));
        selectedOption = session.getData(Constant.SALARY_DATE);
        binding.etBranch.setText(session.getData(Constant.BRANCH));
        binding.etMobileFamily.setText(session.getData(Constant.FAMILY1));
        binding.etMobileFamilyTwo.setText(session.getData(Constant.FAMILY2));
        binding.etDateOfBirth.setText(session.getData(Constant.DOB));


    }

    private void selectPhoto() {
        // Create an Intent to open the file picker
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);

    }

    public void selectPdfFile() {
        // Create an Intent to open the file picker
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf"); // Only allow PDF files
        // Start the file picker activity
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            switch (tapped) {
                case "aadhar":
                    aadhar = uri;
                    binding.ivAadhar.setVisibility(View.VISIBLE);
                    binding.rlAadhar.setVisibility(View.VISIBLE);
                    break;
                case "resume":
                    resume = uri;
                    binding.ivResume.setVisibility(View.VISIBLE);
                    binding.rlResume.setVisibility(View.VISIBLE);
                    break;
                case "photo":
                    photo = uri;
                    binding.ivPhoto.setVisibility(View.VISIBLE);
                    binding.rlPhoto.setVisibility(View.VISIBLE);

                    break;
                case "cirtificate":
                    eduCirtificate = uri;
                    binding.ivCirtificate.setVisibility(View.VISIBLE);
                    binding.rlEduCirtifi.setVisibility(View.VISIBLE);


                    break;
            }

            //  saveProfileAccount(uri);
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ActivityResultLauncher<String> requestPermissionLauncher =
                    registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                            isGranted -> {
                                if (isGranted) {
                                    // Permission is granted
                                } else {
                                    // Permission denied
                                }
                            });
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission is granted by default in Android 10 and lower
        }
    }


    private void uploadPDF(final String aadharName, Uri aadharUriFile, final String resumeName,
                           Uri resumeUriFile, final String eduCertificateName, Uri eduCertificateUriFile, final String photoName, Uri photoUriFile) {
        InputStream aadharIStream = null;
        InputStream resumeIStream = null;
        InputStream eduCertificateIStream = null;
        InputStream photoIStream = null;
        try {

            aadharIStream = getActivity().getContentResolver().openInputStream(aadharUriFile);
            final byte[] aadharInputData = getBytes(aadharIStream);

            eduCertificateIStream = getActivity().getContentResolver().openInputStream(eduCertificateUriFile);
            final byte[] eduCertificateInputData = getBytes(eduCertificateIStream);

            resumeIStream = getActivity().getContentResolver().openInputStream(resumeUriFile);
            final byte[] resumeInputData = getBytes(resumeIStream);

            photoIStream = getActivity().getContentResolver().openInputStream(photoUriFile);
            final byte[] photoInputData = getBytes(photoIStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.STAFF_DOCUMENT,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo", new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                jsonObject.toString().replace("\\\\", "");

                                if (jsonObject.getBoolean("success")) {
                                    session.setData(Constant.SALARY_DATE, selectedOption);
                                    progressDisplay.hideProgress();
                                    updateStaffBankDetails();


                                }
                            } catch (JSONException e) {
                                Toast.makeText(getActivity(), "failure catch", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    // params.put("tags", "ccccc");  add string parameters
                    params.put(Constant.STAFF_ID, session.getData(Constant.STAFF_ID));
                    params.put(Constant.SALARY_DATE, selectedOption);
                    params.put(Constant.DOB, binding.etDateOfBirth.getText().toString());

                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();


                    params.put(Constant.AADHAR_CARD, new DataPart(aadharName, aadharInputData));
                    params.put(Constant.RESUME, new DataPart(resumeName, resumeInputData));
                    params.put(Constant.EDUCATION_CERTIFICATE, new DataPart(eduCertificateName, eduCertificateInputData));
                    params.put(Constant.PHOTO, new DataPart(photoName, photoInputData));
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(getActivity());
            rQueue.add(volleyMultipartRequest);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @SuppressLint("Range")
    public void processWithUri(Uri aadharUri, Uri resumeUri, Uri eduCertificateUri, Uri photoUri) {
        // Get the Uri of the selected file
        progressDisplay  = new ProgressDisplay(getActivity());
        progressDisplay.showProgress();
        String aadharUriString = aadharUri.toString();
        String resumeUriString = resumeUri.toString();
        String eduCertificateUriString = eduCertificateUri.toString();
        String photoUriString = photoUri.toString();


        File aadharFile = new File(aadharUriString);
        File resumeFile = new File(resumeUriString);
        File eduCertificateFile = new File(eduCertificateUriString);
        File photoFile = new File(photoUriString);


        String path = aadharFile.getAbsolutePath();
        String aadharDisplayName = null;
        String resumeDisplayName = null;
        String eduCertificateDisplayName = null;
        String photoDisplayName = null;

        if (aadharUriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(aadharUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    aadharDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("nameeeee>>>>  ", aadharDisplayName);


                }
            } finally {
                cursor.close();
            }
        } else if (aadharUriString.startsWith("file://")) {
            aadharDisplayName = aadharFile.getName();
            Log.d("nameeeee>>>>  ", aadharDisplayName);
        }
        if (resumeUriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(resumeUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    resumeDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("nameeeee>>>>  ", resumeDisplayName);

                }
            } finally {
                cursor.close();
            }
        } else if (resumeUriString.startsWith("file://")) {
            resumeDisplayName = resumeFile.getName();
            Log.d("nameeeee>>>>  ", resumeDisplayName);
        }
        if (eduCertificateUriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(eduCertificateUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    eduCertificateDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("nameeeee>>>>  ", eduCertificateDisplayName);

                }
            } finally {
                cursor.close();
            }
        } else if (eduCertificateUriString.startsWith("file://")) {
            eduCertificateDisplayName = eduCertificateFile.getName();
            Log.d("nameeeee>>>>  ", eduCertificateDisplayName);
        }
        if (photoUriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor = getActivity().getContentResolver().query(photoUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    photoDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("nameeeee>>>>  ", photoDisplayName);

                }
            } finally {
                cursor.close();
            }
        } else if (photoUriString.startsWith("file://")) {
            photoDisplayName = photoFile.getName();
            Log.d("nameeeee>>>>  ", photoDisplayName);
        }

        uploadPDF(aadharDisplayName, aadharUri, resumeDisplayName, resumeUri, eduCertificateDisplayName, eduCertificateUri, photoDisplayName, photoUri);


    }

    private boolean validateFields() {
        boolean isValid = true;
        String salaryDate = selectedOption;

        if (binding.etBank.getText().toString().isEmpty()) {
            binding.etBank.setError("Please enter your bank account number");
            isValid = false;
        }

        if (binding.etIfsc.getText().toString().isEmpty()) {
            binding.etIfsc.setError("Please enter your IFSC code");
            isValid = false;
        }
        if (binding.etBankName.getText().toString().isEmpty()) {
            binding.etBankName.setError("Please enter your Bank Name");
            isValid = false;
        }
        if (binding.etBranch.getText().toString().isEmpty()) {
            binding.etBranch.setError("Please enter your Branch Name");
            isValid = false;
        }
        if (binding.etMobileFamily.getText().toString().isEmpty()) {
            binding.etMobileFamily.setError("Please enter Mobile number of family member");
            isValid = false;
        }
        if (binding.etMobileFamilyTwo.getText().toString().isEmpty()) {
            binding.etMobileFamilyTwo.setError("Please enter Mobile number of Friend");
            isValid = false;
        }
        if (binding.etDateOfBirth.getText().toString().isEmpty()) {
            binding.etDateOfBirth.setError("Please Select Date of Birth");
            isValid = false;
        }
        if (aadhar == null) {
            Toast.makeText(requireContext(), "Please upload Aadhar", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (resume == null) {
            Toast.makeText(requireContext(), "Please upload Resume", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (photo == null) {
            Toast.makeText(requireContext(), "Please upload Photo", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (eduCirtificate == null) {
            Toast.makeText(requireContext(), "Please upload Educational Cirtificate", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (selectedOption.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter Salary Date", Toast.LENGTH_SHORT).show();
            isValid = false;
        }


        return isValid;
    }

    private void updateStaffBankDetails() {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constant.STAFF_ID, session.getData(Constant.STAFF_ID));
        params.put(Constant.IFSC_CODE, binding.etIfsc.getText().toString());
        params.put(Constant.BANK_NAME, binding.etBankName.getText().toString());
        params.put(Constant.BRANCH, binding.etBranch.getText().toString());
        params.put(Constant.BANK_ACCOUNT_NUMBER, binding.etBank.getText().toString());
        params.put(Constant.FAMILY1, binding.etMobileFamily.getText().toString());
        params.put(Constant.FAMILY2, binding.etMobileFamilyTwo.getText().toString());
        ApiConfig.RequestToVolley((result, response) -> {
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                      //  Toast.makeText(requireContext(), jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        JSONArray data = jsonObject.getJSONArray("data");
                        JSONObject object = data.getJSONObject(0);
                        session.setData(Constant.STAFF_ID, object.getString(Constant.ID));
                        session.setData(Constant.EMAIL, object.getString(Constant.EMAIL));
                        session.setData(Constant.PASSWORD, object.getString(Constant.PASSWORD));
                        session.setData(Constant.MOBILE, object.getString(Constant.MOBILE));
                        // session.setData(Constant.ADDRESS, object.getString(Constant.ADDRESS));
                        session.setData(Constant.BANK_ACCOUNT_NUMBER, object.getString(Constant.BANK_ACCOUNT_NUMBER));
                        session.setData(Constant.IFSC_CODE, object.getString(Constant.IFSC_CODE));
                        session.setData(Constant.BANK_NAME, object.getString(Constant.BANK_NAME));
                        session.setData(Constant.BRANCH, object.getString(Constant.BRANCH));
                        session.setData(Constant.AADHAR_CARD, object.getString(Constant.AADHAR_CARD));
                        session.setData(Constant.RESUME, object.getString(Constant.RESUME));
                        session.setData(Constant.PHOTO, object.getString(Constant.PHOTO));
                        session.setData(Constant.EDUCATION_CERTIFICATE, object.getString(Constant.EDUCATION_CERTIFICATE));
                        session.setData(Constant.SALARY_DATE, object.getString(Constant.SALARY_DATE));
                        binding.btnUpdate.setEnabled(false);
                        initCall();
                        navigateToHomeFragment();

                    } else {
                        Toast.makeText(requireContext(), jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, requireActivity(), Constant.UPDATE_STAFFBANK, params, true);


    }

    private void navigateToHomeFragment() {
        HomeFragment withdrawalFragment = new HomeFragment();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLyt, withdrawalFragment);
        transaction.commit();
    }

}

