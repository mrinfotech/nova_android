package com.mrnovacrm.b2b_superadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageBranchActivity extends AppCompatActivity implements View.OnClickListener {

    String id = "";
    String name = "";
    String email = "";
    String mobile = "";
    String address = "";
    String gst = "";
    String state_nm = "";
    String district_nm = "";
    String country_nm = "";
    String state_idval = "";
    String district_idval = "";
    String country_idval = "";
    ArrayList<String> countryNamesList = new ArrayList<>();
    ArrayList<String> countryIdsList = new ArrayList<>();
    ArrayList<String> stateNamesList = new ArrayList<>();
    ArrayList<String> stateIdsList = new ArrayList<>();
    ArrayList<String> distNamesList = new ArrayList<>();
    ArrayList<String> distIdsList = new ArrayList<>();
    String COUNTRY_ID = "";
    String STATE_ID = "";
    String DISTRICT_ID = "";
    EditText branch_name, mobile_edtxt, branch_email, branch_location, branch_address, branch_gst;
    Spinner country_spinner, state_spinner, dist_spinner;
    Button add_location, save_btn;
    Context mContext;
    int COUNTRYSELECTEDPOS = 0;
    int STATESELECTEDPOS = 0;
    int DISTSELECTEDPOS = 0;
    private String PRIMARYID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setTheme(R.style.AppTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Manage Branch");
        setContentView(R.layout.layout_managebranch);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        name = bundle.getString("name");
        email = bundle.getString("email");
        mobile = bundle.getString("mobile");
        address = bundle.getString("address");
        gst = bundle.getString("gst");
        state_nm = bundle.getString("state_nm");
        district_nm = bundle.getString("district_nm");
        country_nm = bundle.getString("country_nm");

        district_idval = bundle.getString("district_id");
        state_idval = bundle.getString("state_id");
        country_idval = bundle.getString("country_id");

        branch_name = findViewById(R.id.branch_name);
        branch_name.setText(name);

        mobile_edtxt = findViewById(R.id.mobile);
        mobile_edtxt.setText(mobile);

        branch_email = findViewById(R.id.email);
        branch_email.setText(email);

        country_spinner = findViewById(R.id.country_spinner);
        state_spinner = findViewById(R.id.state_spinner);
        dist_spinner = findViewById(R.id.dist_spinner);

        branch_location = findViewById(R.id.location);

        branch_address = findViewById(R.id.address);
        branch_address.setText(address);

        branch_gst = findViewById(R.id.gst);
        branch_gst.setText(gst);

        add_location = findViewById(R.id.add_location);
        add_location.setOnClickListener(ManageBranchActivity.this);
        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(ManageBranchActivity.this);

        Log.e("country_idval", country_idval);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            PRIMARYID = "1";
        }

        hintCountries();
        hintStates();
        hintDistrict();
        loadGraphicsData("country", "id");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void hintCountries() {
        countryNamesList.clear();
        countryNamesList.add("Select Country");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                final String BRANCHNAME = branch_name.getText().toString().trim();
                final String MOBILENUMBER = mobile_edtxt.getText().toString().trim();
                final String ADDRESS = branch_address.getText().toString().trim();
                final String LOCATIONVAL = branch_location.getText().toString().trim();
                final String EMAILID = branch_email.getText().toString().trim();
                final String GST = branch_gst.getText().toString().trim();

                if (BRANCHNAME == null || "".equalsIgnoreCase(BRANCHNAME) || BRANCHNAME.equals("")
                        || MOBILENUMBER == null || "".equalsIgnoreCase(MOBILENUMBER) || MOBILENUMBER.equals("")
                        || ADDRESS == null || "".equalsIgnoreCase(ADDRESS) || ADDRESS.equals("")
                        || COUNTRY_ID == null || "".equalsIgnoreCase(COUNTRY_ID) || COUNTRY_ID.equals("")
                        || STATE_ID == null || "".equalsIgnoreCase(STATE_ID) || STATE_ID.equals("")
                        || DISTRICT_ID == null || "".equalsIgnoreCase(DISTRICT_ID) || DISTRICT_ID.equals("")
                        ) {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    submitDetailsWithRetorfit(BRANCHNAME, MOBILENUMBER, EMAILID, COUNTRY_ID, STATE_ID, DISTRICT_ID, LOCATIONVAL, ADDRESS, GST);
                }
                break;
            case R.id.add_location:
                break;
        }
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
        } else if (type.equals("dist")) {
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
                                } else if (type.equals("dist")) {
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
                                        if (country_idval.equals(id)) {
                                            COUNTRYSELECTEDPOS = i + 1;
                                        }
                                    }
                                    if (type.equals("state")) {
                                        stateNamesList.add(name);
                                        stateIdsList.add(id);
                                        if (state_idval.equals(id)) {
                                            STATESELECTEDPOS = i + 1;
                                        }
                                    }
                                    if (type.equals("dist")) {
                                        distNamesList.add(name);
                                        distIdsList.add(id);
                                        if (district_idval.equals(id)) {
                                            DISTSELECTEDPOS = i + 1;
                                        }
                                    }
                                }
                                if (type.equals("country")) {
                                    showCountriesData();
                                } else if (type.equals("state")) {
                                    showStatesData();
                                } else if (type.equals("dist")) {
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
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showCountriesData() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, countryNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        country_spinner.setAdapter(spinnerClass);
        country_spinner.setSelection(COUNTRYSELECTEDPOS);
        COUNTRYSELECTEDPOS = 0;
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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, stateNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        state_spinner.setAdapter(spinnerClass);
        state_spinner.setSelection(STATESELECTEDPOS);
        STATESELECTEDPOS = 0;
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    STATE_ID = "";
                    hintDistrict();

                } else {
                    STATE_ID = stateIdsList.get(position - 1);
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
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, distNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        dist_spinner.setAdapter(spinnerClass);
        dist_spinner.setSelection(DISTSELECTEDPOS);
        DISTSELECTEDPOS = 0;
        dist_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    DISTRICT_ID = "";
                } else {
                    DISTRICT_ID = distIdsList.get(position - 1);
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

        Call<Login> mService = mApiService.manageBranches(PRIMARYID, EMAILID, BRANCHNAME, MOBILENUMBER, ADDRESS, DISTRICT_ID, STATE_ID,
                COUNTRY_ID, GST, "0.0", "0.0", id);
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
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if (BrachesListActivity.mainfinish != null) {
                            BrachesListActivity.mainfinish.finish();
                        }
                        finish();
                        Intent intent = new Intent(getApplicationContext(), BrachesListActivity.class);
                        startActivity(intent);
                    } else {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}