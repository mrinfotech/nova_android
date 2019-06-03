package com.mrnovacrm.b2b_finance_dept;

//public class ManageDeliveryBoysFragment {
//}


//import android.Manifest;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import com.b2b.R;
//import com.b2b.activity.FilePath;
//import com.b2b.b2b_superadmin.BrachesListActivity;
//import com.b2b.constants.TransparentProgressDialog;
//import com.b2b.db.SharedDB;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.ui.PlaceAutocomplete;
//import com.google.android.gms.maps.model.LatLng;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import static android.app.Activity.RESULT_CANCELED;
//import static android.app.Activity.RESULT_OK;
//import static android.content.ContentValues.TAG;
///**
// * Created by prasad on 3/20/2018.
// */
//
//public class ManageDeliveryBoysFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {
//    public static EditText date, address;
//    Spinner designation_spinner;
//    ArrayList<String> designation_spinner_list = new ArrayList<>();
//    ArrayList<String> designationspinnerID_list = new ArrayList<>();
//    private String DESIGNATIONID = "", DESIGNATION = "";
//    EditText first_name, last_name, mobile_edtxt, email, location, address_proof1, address_proof2;
//    Button save_btn, add_location, attach_address1, attach_address2;
//    private HashMap<String, String> values;
//    private String PRIMARYID = "";
//    private static final int LocationRequestCode = 1;
//    private String CURRENT_ADDRESS, LOCALITY = null;
//    double LAT, LNG;
//    private int STORAGE_PERMISSION_CODE = 23;
//    private static final int FileSelectId = 3;
//    private static final int FileSelectId1 = 4;
//    private String selectedFilePath = "";
//    private String selectedFilePath1 = "";
//    //TransparentProgressDialog dialog;
//    private String SERVER_URL = SharedDB.URL + "employee/add";
//    private ArrayList<String> selectimages=new ArrayList<>();
//    Context mContext;
//    private TransparentProgressDialog dialog;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.activity_employee_registration, container, false);
//        mContext=getActivity();
//        date = rootView.findViewById(R.id.date);
//        address = rootView.findViewById(R.id.address);
//        first_name = rootView.findViewById(R.id.first_name);
//        last_name = rootView.findViewById(R.id.last_name);
//        mobile_edtxt = rootView.findViewById(R.id.mobile);
//        save_btn = rootView.findViewById(R.id.save_btn);
//        save_btn.setOnClickListener(this);
//        date.setOnTouchListener(ManageDeliveryBoysFragment.this);
//        email = rootView.findViewById(R.id.email);
//        location = rootView.findViewById(R.id.location);
//        add_location = rootView.findViewById(R.id.add_location);
//        add_location.setOnClickListener(this);
//        attach_address1 = rootView.findViewById(R.id.attach_address1);
//        attach_address1.setOnClickListener(this);
//        attach_address2 = rootView.findViewById(R.id.attach_address2);
//        attach_address2.setOnClickListener(this);
//        address_proof1 = rootView.findViewById(R.id.address_proof1);
//        address_proof2 = rootView.findViewById(R.id.address_proof2);
//        designation_spinner = rootView.findViewById(R.id.designation_spinner);
//        designation_spinner.setVisibility(View.GONE);
//
//        if (SharedDB.isLoggedIn(getActivity())) {
//            values = SharedDB.getUserDetails(getActivity());
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//        }
//        setHasOptionsMenu(true);
//        return rootView;
//    }
//
//    @Override
//    public void onClick(View view) {
//
//        switch (view.getId()) {
//            case R.id.save_btn:
//
//                final String FIRSTNAME = first_name.getText().toString().trim();
//                final String LASTNAME = last_name.getText().toString().trim();
//                final String MOBILENUMBER = mobile_edtxt.getText().toString().trim();
//                final String DATEVAL = date.getText().toString().trim();
//                final String ADDRESS = address.getText().toString().trim();
//                final String LOCATIONVAL = location.getText().toString().trim();
//                final String EMAILID = email.getText().toString().trim();
//
//                if (FIRSTNAME == null || "".equalsIgnoreCase(FIRSTNAME)|| FIRSTNAME.equals("")
//                        || LASTNAME == null || "".equalsIgnoreCase(LASTNAME)|| LASTNAME.equals("")
//                        || MOBILENUMBER == null || "".equalsIgnoreCase(MOBILENUMBER) || MOBILENUMBER.equals("")
//                        || DATEVAL == null || "".equalsIgnoreCase(DATEVAL) || DATEVAL.equals("")
//                        || ADDRESS == null || "".equalsIgnoreCase(ADDRESS)|| ADDRESS.equals("")
//                        //   || LOCATIONVAL == null|| "".equalsIgnoreCase(LOCATIONVAL)|| LOCATIONVAL.equals("")
//                        || EMAILID == null || "".equalsIgnoreCase(EMAILID)|| EMAILID.equals("")
//                        || selectedFilePath == null || "".equalsIgnoreCase(selectedFilePath)|| selectedFilePath.equals("")
//                        || selectedFilePath1 == null || "".equalsIgnoreCase(selectedFilePath1)|| selectedFilePath1.equals("")
//                        || DESIGNATIONID == null || "".equalsIgnoreCase(DESIGNATIONID)|| DESIGNATIONID.equals("")
//                        ) {
//                    Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
//                } else {
//                    dialog = new TransparentProgressDialog(mContext);
//                    dialog.show();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                uploadFile(FIRSTNAME, LASTNAME,
//                                        DESIGNATIONID,MOBILENUMBER.toString(), DATEVAL,
//                                        ADDRESS,EMAILID, LAT, LNG);
//                            } catch (OutOfMemoryError e) {
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getActivity(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
//                                        if( dialog!=null)
//                                        {
//                                            dialog.dismiss();
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    }).start();
//                    //}
//                }
//                break;
//            case R.id.add_location:
//                callPlaceAutocompleteActivityIntent();
//                break;
//            case R.id.attach_address1:
//                selectImage1();
//                break;
//            case R.id.attach_address2:
//                selectImage2();
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void selectImage1() {
//        if (isReadStorageAllowed()) {
//            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            pickIntent.setType("*/*");
//            startActivityForResult(pickIntent, 3);
//            return;
//        }
//        requestStoragePermission();
//        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickIntent.setType("*/*");
//        startActivityForResult(pickIntent, 3);
//    }
//
//    public void selectImage2() {
//        if (isReadStorageAllowed()) {
//            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            pickIntent.setType("*/*");
//            startActivityForResult(pickIntent, 4);
//            return;
//        }
//        requestStoragePermission();
//        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickIntent.setType("*/*");
//        startActivityForResult(pickIntent, 4);
//    }
//
//    private boolean isReadStorageAllowed() {
//        //Getting the permission status
//        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
//        //If permission is granted returning true
//        if (result == PackageManager.PERMISSION_GRANTED)
//            return true;
//        //If permission is not granted returning false
//        return false;
//    }
//
//    private void requestStoragePermission() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            //If the user has denied the permission previously your code will come to this block
//            //Here you can explain why you need this permission
//            //Explain here why you need this permission
//        }
//
//        //And finally ask for the permission
//        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        //Checking the request code of our request
//
//        if (requestCode == STORAGE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            } else {
//            }
//        }
//    }
//
//    private void callPlaceAutocompleteActivityIntent() {
//        try {
//            Intent intent =
//                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                            .build(getActivity());
//            startActivityForResult(intent, LocationRequestCode);
//            //PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
//        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
//            // TODO: Handle the error.
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case FileSelectId:
//                if (resultCode == RESULT_OK) {
//                    if (data == null) {
//                        //no data present
//                        return;
//                    }
//                    try {
//                        Uri selectedFileUri = data.getData();
//                        selectedFilePath = FilePath.getPath(getActivity(), selectedFileUri);
//                        String filename = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);
//                        address_proof1.setText(filename);
//                        Log.e(TAG, "Selected File Path:" + selectedFilePath);
//                    } catch (Exception e) {
//
//                    }
//                }
//                break;
//            case FileSelectId1:
//                if (resultCode == RESULT_OK) {
//                    if (data == null) {
//                        //no data present
//                        return;
//                    }
//                    try {
//                        Uri selectedFileUri = data.getData();
//                        selectedFilePath1 = FilePath.getPath(getActivity(), selectedFileUri);
//                        String filename = selectedFilePath1.substring(selectedFilePath1.lastIndexOf("/") + 1);
//                        address_proof2.setText(filename);
//                        Log.e(TAG, "Selected File Path:" + selectedFilePath1);
//                    } catch (Exception e) {
//
//                    }
//                }
//                break;
//            case LocationRequestCode:
//                if (resultCode == RESULT_OK) {
//                    Place place = PlaceAutocomplete.getPlace(getActivity(), data);
//                    LatLng latLng = place.getLatLng();
//                    LAT = latLng.latitude;
//                    LNG = latLng.longitude;
//                    Geocoder geocoder;
//                    List<Address> addresses = null;
//                    geocoder = new Geocoder(getActivity(), Locale.getDefault());
//                    try {
//                        addresses = geocoder.getFromLocation(LAT, LNG, 1);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    if (addresses != null && addresses.size() > 0) {
//                        CURRENT_ADDRESS = addresses.get(0).getAddressLine(0);
//                        LOCALITY = addresses.get(0).getLocality();
//
//                        address.setText(CURRENT_ADDRESS);
//                        location.setText(LOCALITY);
//                    }
//
//                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                    Status status = PlaceAutocomplete.getStatus(getActivity(), data);
//                    Log.e("TAG", status.getStatusMessage());
//                } else if (resultCode == RESULT_CANCELED) {
//                }
//                break;
//        }
//    }
//
//    public int uploadFile(final String firstname, String lastname, String role, String mobile,
//                          String dob, String addresstxt, final String emailtxt, double latitude, double langitude) {
//
//        int serverResponseCode = 0;
//
//        final HttpURLConnection connection;
//        DataOutputStream dataOutputStream;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//
//        File selectedFile = new File(selectedFilePath);
//        String[] parts = selectedFilePath.split("/");
//        final String fileName = parts[parts.length - 1];
//
//        File selectedFile1 = new File(selectedFilePath1);
//        String[] parts1 = selectedFilePath1.split("/");
//        final String fileName1 = parts1[parts1.length - 1];
//
//        if (!selectedFile.isFile() || !selectedFile1.isFile()) {
//            // dialog.dismiss();
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
//                }
//            });
//            return 0;
//        } else {
//            try {
//                FileInputStream fileInputStream = new FileInputStream(selectedFile);
//                URL url = new URL(SERVER_URL);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);//Allow Inputs
//                connection.setDoOutput(true);//Allow Outputs
//                connection.setUseCaches(false);//Don't use a cached Copy
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Connection", "Keep-Alive");
//                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
//                connection.setRequestProperty(
//                        "Content-Type", "multipart/form-data;boundary=" + boundary);
//                connection.setRequestProperty("addressproof1", selectedFilePath);
//                //connection.setRequestProperty("addressproof2", selectedFilePath1);
//                //creating new dataoutputstream
//                dataOutputStream = new DataOutputStream(connection.getOutputStream());
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"first_name\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(firstname);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"last_name\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(lastname);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"role\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(role);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"mobile\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(mobile);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"dob\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(dob);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"address\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(addresstxt);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(PRIMARYID);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"email\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(emailtxt);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"latitude\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(String.valueOf(latitude));
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"langitude\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(String.valueOf(langitude));
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"addressproof1\";filename=\""
//                        + fileName + "\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//
//                //returns no. of bytes present in fileInputStream
//                bytesAvailable = fileInputStream.available();
//                //selecting the buffer size as minimum of available bytes or 1 MB
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                //setting the buffer as byte array of size of bufferSize
//                buffer = new byte[bufferSize];
//
//                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
//                while (bytesRead > 0) {
//                    try {
//                        //write the bytes read from inputstream
//                        dataOutputStream.write(buffer, 0, bufferSize);
//                    } catch (OutOfMemoryError e) {
//                        Toast.makeText(getActivity(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
//                    }
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                }
//
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                try {
//                    serverResponseCode = connection.getResponseCode();
//                } catch (OutOfMemoryError e) {
//                    Toast.makeText(getActivity(), "Memory Insufficient!", Toast.LENGTH_SHORT).show();
//                }
//                String serverResponseMessage = connection.getResponseMessage();
//
//                Log.e(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
//
//                //response code of 200 indicates the server status OK
//                if (serverResponseCode == 200) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            StringBuilder sb = new StringBuilder();
//                            try {
//                                BufferedReader rd = new BufferedReader(new InputStreamReader(connection
//                                        .getInputStream()));
//                                String line;
//                                while ((line = rd.readLine()) != null) {
//                                    sb.append(line);
//                                }
//                                rd.close();
//                            } catch (IOException ioex) {
//                            }
//                            //      Log.e("response", "" + sb.toString());
//
//                            String response = sb.toString();
//
//                            int statusValue = 0;
//                            String messageValue = "";
//                            try {
//                                JSONObject res_object = new JSONObject(response);
//
//                                if (res_object.has("status")) {
//                                    statusValue = res_object.getInt("status");
//                                    Log.e("statusValue", "" + statusValue);
//                                }
//                                if (res_object.has("message")) {
//                                    messageValue = res_object.getString("message");
//                                    Log.e("messageValue", messageValue);
//                                }
//                                if (statusValue == 1) {
//                                    Toast.makeText(getActivity(), messageValue, Toast.LENGTH_SHORT).show();
//                                    Fragment fragment = new ManageDeliveryBoysFragment();
//                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                                    fragmentTransaction.replace(R.id.container_body, fragment);
//                                    fragmentTransaction.commit();
//                                } else {
//                                    Toast.makeText(getActivity(), messageValue, Toast.LENGTH_SHORT).show();
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//                //closing the input and output streams
//                fileInputStream.close();
//                dataOutputStream.flush();
//                dataOutputStream.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(), "URL Error!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(), "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            if(dialog!=null)
//            {
//                dialog.dismiss();
//            }
//            return serverResponseCode;
//        }
//    }
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.date) {
//            DialogFragment fromfragment = new DatePickerFragment();
//            fromfragment.show(getFragmentManager(), "Date Picker");
//        }
//        return true;
//    }
//
//
//
//    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            //Use the current date as the default date in the date picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//            String type;
//            return new DatePickerDialog(getActivity(), dateListener, year, month, day);
//        }
//
//        private DatePickerDialog.OnDateSetListener dateListener =
//                new DatePickerDialog.OnDateSetListener() {
//
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        date.setText(dayOfMonth + "-" + monthOfYear + "-" + year);
//                    }
//                };
//
//        @Override
//        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//        }
//    }
//
//    @Override
//    public void onCreateOptionsMenu(
//            Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_superadmin, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.list:
//                Intent intent = new Intent(getActivity(), BrachesListActivity.class);
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//}


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.FilePath;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
/**
 * Created by prasad on 3/20/2018.
 */

public class ManageDeliveryBoysFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {
    public static EditText date, address;
    Spinner designation_spinner;
    ArrayList<String> designation_spinner_list = new ArrayList<>();
    ArrayList<String> designationspinnerID_list = new ArrayList<>();
    private String DESIGNATIONID = "", DESIGNATION = "";
    EditText first_name, last_name, mobile_edtxt, email, location, address_proof1, address_proof2;
    Button save_btn, add_location, attach_address1, attach_address2;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String BRANCHID = "";
    private static final int LocationRequestCode = 1;
    private String CURRENT_ADDRESS, LOCALITY = null;
    double LAT, LNG;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final int FileSelectId = 3;
    private static final int FileSelectId1 = 4;
    private String selectedFilePath = "";
    private String selectedFilePath1 = "";
    //TransparentProgressDialog dialog;
    private String SERVER_URL = SharedDB.URL + "employee/add";
    private ArrayList<String> selectimages=new ArrayList<>();
    Context mContext;
    private TransparentProgressDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_employee_registration, container, false);
        mContext=getActivity();
        date = rootView.findViewById(R.id.date);
        address = rootView.findViewById(R.id.address);
        first_name = rootView.findViewById(R.id.first_name);
        last_name = rootView.findViewById(R.id.last_name);
        mobile_edtxt = rootView.findViewById(R.id.mobile);
        save_btn = rootView.findViewById(R.id.save_btn);
        save_btn.setOnClickListener(this);
        date.setOnTouchListener(ManageDeliveryBoysFragment.this);
        email = rootView.findViewById(R.id.email);
        location = rootView.findViewById(R.id.location);
        add_location = rootView.findViewById(R.id.add_location);
        add_location.setOnClickListener(this);
        attach_address1 = rootView.findViewById(R.id.attach_address1);
        attach_address1.setOnClickListener(this);
        attach_address2 = rootView.findViewById(R.id.attach_address2);
        attach_address2.setOnClickListener(this);
        address_proof1 = rootView.findViewById(R.id.address_proof1);
        address_proof2 = rootView.findViewById(R.id.address_proof2);
        designation_spinner = rootView.findViewById(R.id.designation_spinner);
        designation_spinner.setVisibility(View.GONE);

        if (SharedDB.isLoggedIn(getActivity())) {
            values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }


        LinearLayout address_proof1linear=rootView.findViewById(R.id.address_proof1linear);
        LinearLayout address_proof2linear=rootView.findViewById(R.id.address_proof2linear);
        LinearLayout locationlinear=rootView.findViewById(R.id.locationlinear);

        View address_proof1linearview=rootView.findViewById(R.id.address_proof1linearview);
        View address_proof2linearview=rootView.findViewById(R.id.locationlinearview);
        View locationlinearview=rootView.findViewById(R.id.locationlinearview);

        address_proof1linear.setVisibility(View.GONE);
        address_proof1linearview.setVisibility(View.GONE);

        address_proof2linear.setVisibility(View.GONE);
        address_proof2linearview.setVisibility(View.GONE);

        locationlinear.setVisibility(View.GONE);
        locationlinearview.setVisibility(View.GONE);

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.save_btn:

                final String FIRSTNAME = first_name.getText().toString().trim();
                final String LASTNAME = last_name.getText().toString().trim();
                final String MOBILENUMBER = mobile_edtxt.getText().toString().trim();
                final String DATEVAL = date.getText().toString().trim();
                final String ADDRESS = address.getText().toString().trim();
                final String LOCATIONVAL = location.getText().toString().trim();
                final String EMAILID = email.getText().toString().trim();

                if (FIRSTNAME == null || "".equalsIgnoreCase(FIRSTNAME)|| FIRSTNAME.equals("")
                        || LASTNAME == null || "".equalsIgnoreCase(LASTNAME)|| LASTNAME.equals("")
                        || MOBILENUMBER == null || "".equalsIgnoreCase(MOBILENUMBER) || MOBILENUMBER.equals("")
                        || DATEVAL == null || "".equalsIgnoreCase(DATEVAL) || DATEVAL.equals("")
                        || ADDRESS == null || "".equalsIgnoreCase(ADDRESS)|| ADDRESS.equals("")
                        //   || LOCATIONVAL == null|| "".equalsIgnoreCase(LOCATIONVAL)|| LOCATIONVAL.equals("")
                        || EMAILID == null || "".equalsIgnoreCase(EMAILID)|| EMAILID.equals("")) {
                    Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    submitDetailsWithRetorfit(FIRSTNAME,LASTNAME,MOBILENUMBER,DATEVAL,ADDRESS,EMAILID);
                }
                break;
            case R.id.add_location:
                //callPlaceAutocompleteActivityIntent();
                break;
            case R.id.attach_address1:
                selectImage1();
                break;
            case R.id.attach_address2:
                selectImage2();
                break;
            default:
                break;
        }
    }
    public void submitDetailsWithRetorfit(final String FRISTNAME,String LASTNAME, final String MOBILENUMBER,
                                          final String DOB, final String ADDRESS,final String EMAILID) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Login> mService = mApiService.addAdmin(
                FRISTNAME,LASTNAME,"DB",MOBILENUMBER,DOB,ADDRESS,PRIMARYID,EMAILID,"0.0",
                "0.0",BRANCHID);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response", "" + response);
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                        Fragment fragment = new ManageDeliveryBoysFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        fragmentTransaction.commit();

                    } else {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void selectImage1() {
        if (isReadStorageAllowed()) {
            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("*/*");
            startActivityForResult(pickIntent, 3);
            return;
        }
        requestStoragePermission();
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("*/*");
        startActivityForResult(pickIntent, 3);
    }

    public void selectImage2() {
        if (isReadStorageAllowed()) {
            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("*/*");
            startActivityForResult(pickIntent, 4);
            return;
        }
        requestStoragePermission();
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("*/*");
        startActivityForResult(pickIntent, 4);
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request

        if (requestCode == STORAGE_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FileSelectId:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        //no data present
                        return;
                    }
                    try {
                        Uri selectedFileUri = data.getData();
                        selectedFilePath = FilePath.getPath(getActivity(), selectedFileUri);
                        String filename = selectedFilePath.substring(selectedFilePath.lastIndexOf("/") + 1);
                        address_proof1.setText(filename);
                        Log.e(TAG, "Selected File Path:" + selectedFilePath);
                    } catch (Exception e) {

                    }
                }
                break;
            case FileSelectId1:
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        //no data present
                        return;
                    }
                    try {
                        Uri selectedFileUri = data.getData();
                        selectedFilePath1 = FilePath.getPath(getActivity(), selectedFileUri);
                        String filename = selectedFilePath1.substring(selectedFilePath1.lastIndexOf("/") + 1);
                        address_proof2.setText(filename);
                        Log.e(TAG, "Selected File Path:" + selectedFilePath1);
                    } catch (Exception e) {

                    }
                }
                break;
            case LocationRequestCode:

                break;
        }
    }

    public int uploadFile(final String firstname, String lastname, String role, String mobile,
                          String dob, String addresstxt, final String emailtxt, double latitude, double langitude) {

        int serverResponseCode = 0;

        final HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File selectedFile = new File(selectedFilePath);
        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        File selectedFile1 = new File(selectedFilePath1);
        String[] parts1 = selectedFilePath1.split("/");
        final String fileName1 = parts1[parts1.length - 1];

        if (!selectedFile.isFile() || !selectedFile1.isFile()) {
            // dialog.dismiss();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("addressproof1", selectedFilePath);
                //connection.setRequestProperty("addressproof2", selectedFilePath1);
                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"first_name\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(firstname);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"last_name\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(lastname);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"role\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(role);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"mobile\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(mobile);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"dob\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(dob);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"address\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(addresstxt);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(PRIMARYID);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"email\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(emailtxt);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"latitude\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(String.valueOf(latitude));
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"langitude\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(String.valueOf(langitude));
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"addressproof1\";filename=\""
                        + fileName + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    try {
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(getActivity(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                try {
                    serverResponseCode = connection.getResponseCode();
                } catch (OutOfMemoryError e) {
                    Toast.makeText(getActivity(), "Memory Insufficient!", Toast.LENGTH_SHORT).show();
                }
                String serverResponseMessage = connection.getResponseMessage();

                Log.e(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            StringBuilder sb = new StringBuilder();
                            try {
                                BufferedReader rd = new BufferedReader(new InputStreamReader(connection
                                        .getInputStream()));
                                String line;
                                while ((line = rd.readLine()) != null) {
                                    sb.append(line);
                                }
                                rd.close();
                            } catch (IOException ioex) {
                            }
                            //      Log.e("response", "" + sb.toString());

                            String response = sb.toString();

                            int statusValue = 0;
                            String messageValue = "";
                            try {
                                JSONObject res_object = new JSONObject(response);

                                if (res_object.has("status")) {
                                    statusValue = res_object.getInt("status");
                                    Log.e("statusValue", "" + statusValue);
                                }
                                if (res_object.has("message")) {
                                    messageValue = res_object.getString("message");
                                    Log.e("messageValue", messageValue);
                                }
                                if (statusValue == 1) {
                                    Toast.makeText(getActivity(), messageValue, Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new ManageDeliveryBoysFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container_body, fragment);
                                    fragmentTransaction.commit();
                                } else {
                                    Toast.makeText(getActivity(), messageValue, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "URL Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if(dialog!=null)
            {
                dialog.dismiss();
            }
            return serverResponseCode;
        }
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.date) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getFragmentManager(), "Date Picker");
        }
        return true;
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            String type;
            if (getArguments() != null) {
                type = getArguments().getString("DateType");
                if (type.equals("fromDate")) {
                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);
                }
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        date.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        }
    }

    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_superadmin, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list:
                Intent intent = new Intent(getActivity(), FinanceEmployeesListActivity.class);
                intent.putExtra("SHORTFROM","DB");
                intent.putExtra("TITLE","Delivery Boys List");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}