package com.app.staffabcd.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.app.staffabcd.VolleyMultipartRequest;
import com.app.staffabcd.databinding.FragmentDocumentsBinding;
import com.app.staffabcd.helper.ApiConfig;
import com.app.staffabcd.helper.Constant;
import com.app.staffabcd.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DocumentFragment extends Fragment {
    FragmentDocumentsBinding binding;
    Session session;
    String tapped = "";
    private RequestQueue rQueue;
    private ArrayList<HashMap<String, String>> arraylist;



    static int PICK_FILE_REQUEST = 1;
    byte[] aaadharBytes = null;
    byte[] resumeBytes = null;

    byte[] photoBytes = null;

    byte[] certificateBytes = null;


    Uri aadhar, resume, photo, eduCirtificate;

    public DocumentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDocumentsBinding.inflate(inflater, container, false);
        session = new Session(getActivity());
        requestStoragePermission();


        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processWithUri(aadhar,resume,eduCirtificate);
              //  updateDocument(getActivity());

            }
        });


        // }


        binding.rlAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapped = "aadhar";
                selectPdfFile();
            }
        });
        binding.rlResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapped = "resume";
                selectPdfFile();
            }
        });
        binding.rlPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapped = "photo";
                selectPdfFile();
            }
        });
        binding.rlEduCirtifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapped = "cirtificate";
                selectPdfFile();
            }
        });


        binding.etBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to open the file picker
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

// Start the file picker activity
                startActivityForResult(intent, PICK_FILE_REQUEST);

            }
        });


        return binding.getRoot();
    }

    public void selectPdfFile() {
        // Create an Intent to open the file picker
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        // Start the file picker activity
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (tapped.equals("aadhar")) {
                aadhar = uri;
                binding.ivAadhar.setVisibility(View.VISIBLE);
                binding.rlAadhar.setVisibility(View.GONE);
            } else if (tapped.equals("resume")) {
                resume = uri;
                binding.ivResume.setVisibility(View.VISIBLE);
                binding.rlResume.setVisibility(View.GONE);
            } else if (tapped.equals("photo")) {
                photo = uri;
                binding.ivPhoto.setVisibility(View.VISIBLE);
                binding.rlPhoto.setVisibility(View.GONE);

            } else if (tapped.equals("cirtificate")) {
                eduCirtificate = uri;
                binding.ivCirtificate.setVisibility(View.VISIBLE);
                binding.rlEduCirtifi.setVisibility(View.GONE);


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

//    private void saveProfileAccount(Uri aadharuri, Uri resumeUri, Uri photoUri, Uri eduCirtificateUri) {
//
//        try {
//            InputStream aadharInputStream = getActivity().getContentResolver().openInputStream(aadharuri);
//            InputStream resumeInputStream = getActivity().getContentResolver().openInputStream(resumeUri);
//            InputStream photoInputStream = getActivity().getContentResolver().openInputStream(photoUri);
//            InputStream eduCirtificateInputStream = getActivity().getContentResolver().openInputStream(eduCirtificateUri);
//
//
//            aaadharBytes = IOUtils.toByteArray(aadharInputStream);
//            resumeBytes = IOUtils.toByteArray(resumeInputStream);
//            photoBytes = IOUtils.toByteArray(photoInputStream);
//            certificateBytes = IOUtils.toByteArray(eduCirtificateInputStream);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ContentResolver cR = getActivity().getContentResolver();
//        String aadharMimeType = cR.getType(aadharuri);
//        String resumeMimeType = cR.getType(resumeUri);
//        String photoMimeType = cR.getType(photoUri);
//        String eduCirtificateMimeType = cR.getType(eduCirtificateUri);
//
//
//        File aadharFile = new File(aadharuri.getPath());
//        File resumeFile = new File(resumeUri.getPath());
//        File photoFile = new File(photoUri.getPath());
//        File eduCirtificateFile = new File(eduCirtificateUri.getPath());
//
//
//        String aadharName = aadharFile.getName();
//        String resumeName = resumeFile.getName();
//        String photoName = photoFile.getName();
//        String eduCirtificateName = eduCirtificateFile.getName();
//
//
//        String url = "https://demoabcd.graymatterworks.com/api/staffs_document.php";
//        //String url = Constant.UPLOADATTACHMENT + session.getData(Constant.STUDENT_ID);
//
//        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                String resultResponse = new String(response.data);
//                Log.d("RESPONSE", resultResponse);
//                Toast.makeText(getActivity(), "" + resultResponse, Toast.LENGTH_SHORT).show();
//                try {
//                    JSONObject result = new JSONObject(resultResponse);
//                    String status = result.getString("status");
//                    String message = result.getString("message");
//
////                    if (status.equals(Constant.REQUEST_SUCCESS)) {
////                        // tell everybody you have succed upload image and post strings
////                        Log.i("Messsage", message);
////                    } else {
////                        Log.i("Unexpected", message);
////                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkResponse networkResponse = error.networkResponse;
//                String errorMessage = "Unknown error";
//                if (networkResponse == null) {
//                    if (error.getClass().equals(TimeoutError.class)) {
//                        errorMessage = "Request timeout";
//                    } else if (error.getClass().equals(NoConnectionError.class)) {
//                        errorMessage = "Failed to connect server";
//                    }
//                } else {
//                    String result = new String(networkResponse.data);
//                    try {
//                        JSONObject response = new JSONObject(result);
//                        String status = response.getString("status");
//                        String message = response.getString("message");
//
//                        Log.e("Error Status", status);
//                        Log.e("Error Message", message);
//
//                        if (networkResponse.statusCode == 404) {
//                            errorMessage = "Resource not found";
//                        } else if (networkResponse.statusCode == 401) {
//                            errorMessage = message + " Please login again";
//                        } else if (networkResponse.statusCode == 400) {
//                            errorMessage = message + " Check your inputs";
//                        } else if (networkResponse.statusCode == 500) {
//                            errorMessage = message + " Something is getting wrong";
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.i("Error", errorMessage);
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put(Constant.STAFF_ID, session.getData(Constant.STAFF_ID));
//                params.put("salary_date", "21");
//
//                return params;
//            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                params.put(Constant.AADHAR_CARD, new DataPart(aadharName, aaadharBytes, aadharMimeType));
//                params.put(Constant.RESUME, new DataPart(resumeName, resumeBytes, resumeMimeType));
//                params.put(Constant.PHOTO, new DataPart(photoName, photoBytes, photoMimeType));
//                params.put(Constant.EDUCATION_CERTIFICATE, new DataPart(eduCirtificateName, certificateBytes, eduCirtificateMimeType));
//
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
//    }

    //    private void saveProfileAccount(Uri aadharUri, Uri resumeUri, Uri photoUri, Uri eduCertificateUri) throws IOException {
//
//            // Get input streams for each file
//            InputStream aadharInputStream = getActivity().getContentResolver().openInputStream(aadharUri);
//            InputStream resumeInputStream = getActivity().getContentResolver().openInputStream(resumeUri);
//            InputStream photoInputStream = getActivity().getContentResolver().openInputStream(photoUri);
//            InputStream eduCertificateInputStream = getActivity().getContentResolver().openInputStream(eduCertificateUri);
//
//            // Read bytes from each input stream
//            byte[] aadharBytes = IOUtils.toByteArray(aadharInputStream);
//            byte[] resumeBytes = IOUtils.toByteArray(resumeInputStream);
//            byte[] photoBytes = IOUtils.toByteArray(photoInputStream);
//            byte[] certificateBytes = IOUtils.toByteArray(eduCertificateInputStream);
//
//            ContentResolver cR = getActivity().getContentResolver();
//            String aadharPath = DocumentFile.fromSingleUri(getActivity(), aadharUri).getUri().getPath();
//            String aadharMimeType = cR.getType(aadharUri);
//
//            String resumePath = DocumentFile.fromSingleUri(getActivity(), resumeUri).getUri().getPath();
//
//            String resumeMimeType = cR.getType(resumeUri);
//
//            String photoPath = DocumentFile.fromSingleUri(getActivity(), photoUri).getUri().getPath();
//            String photoMimeType = cR.getType(photoUri);
//
//            String eduCertificatePath = DocumentFile.fromSingleUri(getActivity(), eduCertificateUri).getUri().getPath();
//            String eduCertificateMimeType = cR.getType(eduCertificateUri);
//
//            // Create File objects from the file paths
//            File aadharFile = new File(aadharPath);
//            File resumeFile = new File(resumePath);
//            File photoFile = new File(photoPath);
//            File eduCertificateFile = new File(eduCertificatePath);
//
//            // Get the file names from the File objects
//            String aadharName = aadharFile.getName();
//            String resumeName = resumeFile.getName();
//            String photoName = photoFile.getName();
//            String eduCertificateName = eduCertificateFile.getName();
//
//            // Set up the multipart request
//            String url = "https://demoabcd.graymatterworks.com/api/staffs_document.php";
//            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
//                @Override
//                public void onResponse(NetworkResponse response) {
//                    String resultResponse = new String(response.data);
//                    Log.d("RESPONSE", resultResponse);
//                    Toast.makeText(getActivity(), "" + resultResponse, Toast.LENGTH_SHORT).show();
//                    try {
//                        JSONObject result = new JSONObject(resultResponse);
//                        String status = result.getString("status");
//                        String message = result.getString("message");
//
////                  if (status.equals(Constant.REQUEST_SUCCESS)) {
////                      // tell everybody you have succed upload image and post strings
////                      Log.i("Messsage", message);
////                  } else {
////                      Log.i("Unexpected", message);
////                  }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    // Handle error response
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put(Constant.STAFF_ID, session.getData(Constant.STAFF_ID));
//                    params.put("salary_date", "21");
//
//                    return params;
//                }
//
//
//                @Override
//                protected Map<String, > getByteData() {
//                    Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
//                    params.put(Constant.AADHAR_CARD, aadharName, aadharFile, aadharMimeType);
//                    params.put(Constant.RESUME, new VolleyMultipartRequest.FilePart(resumeName, resumeFile, resumeMimeType));
//                    params.put(Constant.PHOTO, new VolleyMultipartRequest.FilePart(photoName, photoFile, photoMimeType));
//                    params.put(Constant.EDUCATION_CERTIFICATE, new VolleyMultipartRequest.FilePart(eduCertificateName, eduCertificateFile, eduCertificateMimeType));
//                    return params;
//                }
//
//
//
//            };
//
//            VolleySingleton.getInstance(getActivity()).addToRequestQueue(multipartRequest);
//
//    }
//    private String getRealPathFromURI(Uri contentUri) {
//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
    public String getRealPathFromURI(Uri uri) {
        String path = null;

        // If the scheme is "file", just return the path directly
        if ("file".equals(uri.getScheme())) {
            path = uri.getPath();
        }
        // If the scheme is "content", perform a query to get the path
        else if ("content".equals(uri.getScheme())) {
            String[] projection = {MediaStore.Files.FileColumns.DATA};
            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
                path = cursor.getString(columnIndex);
                cursor.close();
            }
        }

        return path;
    }



    private void uploadPDF(final String aadharName, Uri aadharUriFile, final String resumeName, Uri resumeUriFile,final String eduCertificateName, Uri eduCertificateUriFile) {

        InputStream aadharIStream = null;
        InputStream resumeIStream = null;
        InputStream eduCertificateIStream = null;
        try {

            aadharIStream = getActivity().getContentResolver().openInputStream(aadharUriFile);
            final byte[] aadharInputData = getBytes(aadharIStream);
            
            eduCertificateIStream = getActivity().getContentResolver().openInputStream(eduCertificateUriFile);
            final byte[] eduCertificateInputData = getBytes(eduCertificateIStream);
            
            resumeIStream = getActivity().getContentResolver().openInputStream(resumeUriFile);
            final byte[] resumeInputData = getBytes(resumeIStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://demoabcd.graymatterworks.com/api/staffs_document.php",
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Log.d("ressssssoo",new String(response.data));
                            rQueue.getCache().clear();
                            try {
                                JSONObject jsonObject = new JSONObject(new String(response.data));
                                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                jsonObject.toString().replace("\\\\","");

                                if (jsonObject.getString("status").equals("true")) {
                                    Log.d("come::: >>>  ","yessssss");
                                    arraylist = new ArrayList<HashMap<String, String>>();
                                    JSONArray dataArray = jsonObject.getJSONArray("data");


                                    for (int i = 0; i < dataArray.length(); i++) {
                                        JSONObject dataobj = dataArray.getJSONObject(i);
                                       // url = dataobj.optString("pathToFile");
                                        Toast.makeText(getActivity(), dataobj.optString("pathToFile"), Toast.LENGTH_SHORT).show();
                                    }


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
                    params.put("salary_date", "21");
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();


                    params.put(Constant.AADHAR_CARD, new DataPart(aadharName ,aadharInputData));
                    params.put(Constant.RESUME, new DataPart(resumeName, aadharInputData));
                    params.put(Constant.EDUCATION_CERTIFICATE, new DataPart(eduCertificateName,eduCertificateInputData));
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
    public void processWithUri(Uri aadharUri,Uri resumeUri,Uri eduCertificateUri) {
        // Get the Uri of the selected file
        String aadharUriString = aadharUri.toString();
        String resumeUriString = resumeUri.toString();
        String eduCertificateUriString = eduCertificateUri.toString();
        
        
        File aadharFile = new File(aadharUriString);
        File resumeFile = new File(resumeUriString);
        File eduCertificateFile = new File(eduCertificateUriString);
        
        
        
        String path = aadharFile.getAbsolutePath();
        String aadharDisplayName = null;
        String resumeDisplayName = null;
        String eduCertificateDisplayName = null;

        if (aadharUriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor =getActivity().getContentResolver().query(aadharUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    aadharDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("nameeeee>>>>  ",aadharDisplayName);


                }
            } finally {
                cursor.close();
            }
        } else if (aadharUriString.startsWith("file://")) {
            aadharDisplayName = aadharFile.getName();
            Log.d("nameeeee>>>>  ",aadharDisplayName);
        }
        if (resumeUriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor =getActivity().getContentResolver().query(resumeUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    resumeDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("nameeeee>>>>  ",resumeDisplayName);

                }
            } finally {
                cursor.close();
            }
        } else if (resumeUriString.startsWith("file://")) {
            resumeDisplayName = resumeFile.getName();
            Log.d("nameeeee>>>>  ",resumeDisplayName);
        }
        if (eduCertificateUriString.startsWith("content://")) {
            Cursor cursor = null;
            try {
                cursor =getActivity().getContentResolver().query(eduCertificateUri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    eduCertificateDisplayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    Log.d("nameeeee>>>>  ",eduCertificateDisplayName);

                }
            } finally {
                cursor.close();
            }
        } else if (eduCertificateUriString.startsWith("file://")) {
            eduCertificateDisplayName = eduCertificateFile.getName();
            Log.d("nameeeee>>>>  ",eduCertificateDisplayName);
        }

        uploadPDF(aadharDisplayName,aadharUri,resumeDisplayName,resumeUri,eduCertificateDisplayName,eduCertificateUri);
        
        
    }

}

