package com.mrnovacrm.b2b_superadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DemoGraphicsDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.RecordsDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ManageBranchesFragment extends Fragment implements View.OnClickListener {
    EditText branch_name, mobile_edtxt, email, location, address,gst;
    Spinner country_spinner, state_spinner, dist_spinner;
    Button add_location, save_btn;
    ArrayList<String> countryNamesList = new ArrayList<>();
    ArrayList<String> countryIdsList = new ArrayList<>();

    ArrayList<String> stateNamesList = new ArrayList<>();
    ArrayList<String> stateIdsList = new ArrayList<>();

    ArrayList<String> distNamesList = new ArrayList<>();
    ArrayList<String> distIdsList = new ArrayList<>();

    String COUNTRY_ID = "";
    String STATE_ID = "";
    String DISTRICT_ID = "";
    Context mContext;
    private TransparentProgressDialog dialog;
    private String PRIMARYID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_managebranch, container, false);

        mContext = getActivity();
        branch_name = rootView.findViewById(R.id.branch_name);
        mobile_edtxt = rootView.findViewById(R.id.mobile);
        email = rootView.findViewById(R.id.email);

        setHasOptionsMenu(true);

        country_spinner = rootView.findViewById(R.id.country_spinner);
        state_spinner = rootView.findViewById(R.id.state_spinner);
        dist_spinner = rootView.findViewById(R.id.dist_spinner);

        location = rootView.findViewById(R.id.location);
        address = rootView.findViewById(R.id.address);
        gst = rootView.findViewById(R.id.gst);

        add_location = rootView.findViewById(R.id.add_location);
        add_location.setOnClickListener(ManageBranchesFragment.this);
        save_btn = rootView.findViewById(R.id.save_btn);
        save_btn.setOnClickListener(ManageBranchesFragment.this);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            PRIMARYID = "1";
        }

        hintCountries();
        hintStates();
        hintDistrict();
        loadGraphicsData("country", "id");
        return rootView;
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
                Intent intent = new Intent(getActivity(), BrachesListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                final String BRANCHNAME = branch_name.getText().toString().trim();
                final String MOBILENUMBER = mobile_edtxt.getText().toString().trim();
                final String ADDRESS = address.getText().toString().trim();
                final String LOCATIONVAL = location.getText().toString().trim();
                final String EMAILID = email.getText().toString().trim();
                final String GST = gst.getText().toString().trim();

                if (BRANCHNAME == null || "".equalsIgnoreCase(BRANCHNAME) || BRANCHNAME.equals("")
                        || MOBILENUMBER == null || "".equalsIgnoreCase(MOBILENUMBER) || MOBILENUMBER.equals("")
                        || ADDRESS == null || "".equalsIgnoreCase(ADDRESS) || ADDRESS.equals("")
                        || COUNTRY_ID == null || "".equalsIgnoreCase(COUNTRY_ID) || COUNTRY_ID.equals("")
                        || STATE_ID == null || "".equalsIgnoreCase(STATE_ID) || STATE_ID.equals("")
                        || DISTRICT_ID == null || "".equalsIgnoreCase(DISTRICT_ID) || DISTRICT_ID.equals("")
                        ) {
                    Toast.makeText(getActivity(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
//                    dialog = new TransparentProgressDialog(mContext);
//                    dialog.show();
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                uploadFile(BRANCHNAME, MOBILENUMBER,
//                                        EMAILID,COUNTRY_ID, STATE_ID,
//                                        DISTRICT_ID,LOCATIONVAL, ADDRESS);
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

                    submitDetailsWithRetorfit(BRANCHNAME, MOBILENUMBER, EMAILID, COUNTRY_ID, STATE_ID, DISTRICT_ID, LOCATIONVAL, ADDRESS,GST);
                }
                break;
            case R.id.add_location:
                break;
        }
    }

    public int uploadFile(final String BRANCHNAME, final String MOBILENUMBER,
                          final String EMAILID, final String COUNTRY_ID, final String STATE_ID,
                          final String DISTRICT_ID, final String LOCATIONVAL, final String ADDRESS) {

        int serverResponseCode = 0;

        final HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;


        try {
            URL url = new URL(SharedDB.URL + "branches/manage");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);//Allow Inputs
            connection.setDoOutput(true);//Allow Outputs
            connection.setUseCaches(false);//Don't use a cached Copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=" + boundary);
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(PRIMARYID);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"branch\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(BRANCHNAME);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"mobile\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(MOBILENUMBER);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"email\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(EMAILID);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"country\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(COUNTRY_ID);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"state\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(STATE_ID);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"district\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(DISTRICT_ID);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"latitude\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes("0.0");
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"longitude\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes("0.0");
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);


            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"gst\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes("");
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"address\"" + lineEnd);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(ADDRESS);
            dataOutputStream.writeBytes(lineEnd);
            dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

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


            dataOutputStream.writeBytes(lineEnd);
            //reads bytes from FileInputStream(from 0th index of buffer to buffersize)

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
                                Fragment fragment = new ManageBranchesFragment();
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
        if (dialog != null) {
            dialog.dismiss();
        }
        return serverResponseCode;

    }


    public void hintCountries() {
        countryNamesList.clear();
        countryNamesList.add("Select Country");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, countryNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        country_spinner.setAdapter(spinnerClass);
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    COUNTRY_ID = "";
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void hintStates() {
        stateNamesList.clear();
        stateNamesList.add("Select State");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, stateNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        state_spinner.setAdapter(spinnerClass);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    STATE_ID = "";
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void hintDistrict() {
        distNamesList.clear();
        distNamesList.add("Select District");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, distNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        dist_spinner.setAdapter(spinnerClass);
        dist_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    DISTRICT_ID = "";
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void submitDetailsWithRetorfit(final String BRANCHNAME, final String MOBILENUMBER,
                                          final String EMAILID, final String COUNTRY_ID, final String STATE_ID,
                                          final String DISTRICT_ID, final String LOCATIONVAL, final String ADDRESS,
                                          final String GST) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Login> mService = mApiService.addBranches(PRIMARYID, EMAILID, BRANCHNAME, MOBILENUMBER, ADDRESS, DISTRICT_ID, STATE_ID,
                COUNTRY_ID, GST, "0.0", "0.0");
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

                        Fragment fragment = new ManageBranchesFragment();
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

    public void loadGraphicsData(final String type, final String id) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<DemoGraphicsDTO> mService = null;
        if (type.equals("country")) {
            mService = mApiService.getCountries();
        } else if (type.equals("state")) {
            mService = mApiService.getStates(id);
        }else if (type.equals("dist")) {
            mService = mApiService.getDistricts(id);
        }
        mService.enqueue(new Callback<DemoGraphicsDTO>() {
            @Override
            public void onResponse(Call<DemoGraphicsDTO> call, Response<DemoGraphicsDTO> response) {
                Log.e("response", "" + response);
                DemoGraphicsDTO mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        List<RecordsDTO> demograRecordsDTOS = mLoginObject.getDemograRecordsDTOS();
                        if (demograRecordsDTOS != null) {
                            if (demograRecordsDTOS.size() > 0) {
                                if (type.equals("country")) {
                                    countryNamesList.clear();
                                    countryNamesList.add("Select Country");
                                    countryIdsList.clear();
                                } else if (type.equals("state")) {
                                    stateNamesList.clear();
                                    stateNamesList.add("Select State");
                                    stateIdsList.clear();
                                }else if (type.equals("dist")) {
                                    distNamesList.clear();
                                    distNamesList.add("Select District");
                                    distIdsList.clear();
                                }

                                for (int i = 0; i < demograRecordsDTOS.size(); i++) {
                                    String id = demograRecordsDTOS.get(i).getId();
                                    String name = demograRecordsDTOS.get(i).getName();
                                    if (type.equals("country")) {
                                        countryNamesList.add(name);
                                        countryIdsList.add(id);
                                    }
                                    if (type.equals("state")) {
                                        stateNamesList.add(name);
                                        stateIdsList.add(id);
                                    }
                                    if (type.equals("dist")) {
                                        distNamesList.add(name);
                                        distIdsList.add(id);
                                    }
                                }
                                if (type.equals("country")) {
                                    showCountriesData();
                                }else if(type.equals("state"))
                                {
                                    showStatesData();
                                }else if(type.equals("dist"))
                                {
                                    showDistData();
                                }
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<DemoGraphicsDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showCountriesData() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, countryNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        country_spinner.setAdapter(spinnerClass);
        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    COUNTRY_ID = "";
                    hintStates();
                    hintDistrict();
                } else {
                    COUNTRY_ID = countryIdsList.get(position - 1);
                    loadGraphicsData("state", COUNTRY_ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void showStatesData() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, stateNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        state_spinner.setAdapter(spinnerClass);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    STATE_ID = "";
                    hintDistrict();
                } else {
                    STATE_ID =stateIdsList.get(position - 1);
                    loadGraphicsData("dist", STATE_ID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void showDistData() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(getActivity(),
                R.layout.layout_spinneritems, distNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        dist_spinner.setAdapter(spinnerClass);
        dist_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    DISTRICT_ID = "";

                } else {
                    DISTRICT_ID =distIdsList.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

}