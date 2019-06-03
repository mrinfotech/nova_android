package com.mrnovacrm.b2b_superadmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BranchDetailsDTO;
import com.mrnovacrm.model.BranchesDTO;
import com.mrnovacrm.model.Login;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAdminActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private HashMap<String, String> values;
    private String PRIMARYID;

    public static EditText date, address;
    Spinner designation_spinner;
    ArrayList<String> designation_spinner_list = new ArrayList<>();
    ArrayList<String> designationspinnerID_list = new ArrayList<>();
    private String DESIGNATIONID = "", DESIGNATION = "";
    EditText first_name, last_name, mobile_edtxt, email, location, address_proof1, address_proof2;
    Button save_btn, add_location, attach_address1, attach_address2;

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

    ArrayList<String> branchNamesList = new ArrayList<>();
    ArrayList<String> branchIdsList = new ArrayList<>();
    String BRANCH_ID="";

    String IDVAL="",NAMEVAL="",FIRSTNAMEVAL="",LASTNAMEVAL="",EMPIDVAL="",BRANCHIDVAL="",MOBILEVAL="",EMAILIDVAL="",
            DOBVAL="",ADDRESSVAL="",ROLENAMEVAL="",BRANCHNAMEVAL="";
    private int SELECTBRANCHPOS=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setTheme(R.style.AppTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Manage Admin");
        setContentView(R.layout.layout_manageadmin);

        Bundle bundle=getIntent().getExtras();
        IDVAL=bundle.getString("id");
        NAMEVAL=bundle.getString("name");
        FIRSTNAMEVAL=bundle.getString("first_name");
        LASTNAMEVAL=bundle.getString("last_name");
        EMPIDVAL=bundle.getString("empid");
        BRANCHIDVAL=bundle.getString("branch");
        MOBILEVAL=bundle.getString("mobile");
        EMAILIDVAL=bundle.getString("email");
        DOBVAL=bundle.getString("dob");
        ADDRESSVAL=bundle.getString("address");
        ROLENAMEVAL=bundle.getString("role_name");
        BRANCHNAMEVAL=bundle.getString("branch_name");

        Log.e("BRANCHIDVAL",BRANCHIDVAL);


        date = findViewById(R.id.date);
        date.setText(DOBVAL);
        address = findViewById(R.id.address);
        address.setText(ADDRESSVAL);

        first_name = findViewById(R.id.first_name);
        first_name.setText(FIRSTNAMEVAL);

        last_name = findViewById(R.id.last_name);
        last_name.setText(LASTNAMEVAL);

        mobile_edtxt = findViewById(R.id.mobile);
        mobile_edtxt.setText(MOBILEVAL);

        save_btn = findViewById(R.id.save_btn);
        save_btn.setOnClickListener(UpdateAdminActivity.this);

        date.setOnTouchListener(UpdateAdminActivity.this);
        email = findViewById(R.id.email);
        email.setText(EMAILIDVAL);

        location = findViewById(R.id.location);
        add_location = findViewById(R.id.add_location);
        add_location.setOnClickListener(this);
        attach_address1 = findViewById(R.id.attach_address1);
        attach_address1.setOnClickListener(this);
        attach_address2 = findViewById(R.id.attach_address2);
        attach_address2.setOnClickListener(this);
        address_proof1 = findViewById(R.id.address_proof1);
        address_proof2 = findViewById(R.id.address_proof2);
        designation_spinner = findViewById(R.id.designation_spinner);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }

        LinearLayout address_proof1linear=findViewById(R.id.address_proof1linear);
        LinearLayout address_proof2linear=findViewById(R.id.address_proof2linear);
        LinearLayout locationlinear=findViewById(R.id.locationlinear);

        View address_proof1linearview=findViewById(R.id.address_proof1linearview);
        View address_proof2linearview=findViewById(R.id.locationlinearview);
        View locationlinearview=findViewById(R.id.locationlinearview);

        address_proof1linear.setVisibility(View.GONE);
        address_proof1linearview.setVisibility(View.GONE);

        address_proof2linear.setVisibility(View.GONE);
        address_proof2linearview.setVisibility(View.GONE);

        locationlinear.setVisibility(View.GONE);
        locationlinearview.setVisibility(View.GONE);

        hintBranch();
        loadBranchesData();
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

    public void hintBranch() {
        branchNamesList.clear();
        branchNamesList.add("Select Branch");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, branchNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        designation_spinner.setAdapter(spinnerClass);
        designation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    BRANCH_ID = "";
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void loadBranchesData() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<BranchesDTO> mService = null;
        mService = mApiService.getBraches();
        mService.enqueue(new Callback<BranchesDTO>() {
            @Override
            public void onResponse(Call<BranchesDTO> call, retrofit2.Response<BranchesDTO> response) {
                Log.e("response", "" + response);
                BranchesDTO branchesDTO = response.body();
                dialog.dismiss();
                try {
                    String status = branchesDTO.getStatus();
                    if (status.equals("1")) {
                        List<BranchDetailsDTO> branchDetailsDTOS = branchesDTO.getBranchDetailsDTOS();
                        if (branchDetailsDTOS != null) {
                            if (branchDetailsDTOS.size() > 0) {

                                branchNamesList.clear();
                                branchNamesList.add("Select Branch");
                                branchIdsList.clear();
                                ArrayList<HashMap<String,String>> hashMapList=new ArrayList<HashMap<String,String>>();
                                for (int i = 0; i < branchDetailsDTOS.size(); i++) {
                                    HashMap<String,String> hashmap=new HashMap<>();
                                    String id = branchDetailsDTOS.get(i).getId();
                                    String name = branchDetailsDTOS.get(i).getName();
                                    hashmap.put("id",id);
                                    hashmap.put("name",name);

                                    if(BRANCHIDVAL.equals(id))
                                    {
                                        SELECTBRANCHPOS=i+1;
                                    }

                                    branchNamesList.add(name);
                                    branchIdsList.add(id);

                                    hashMapList.add(hashmap);
                                }
                                showBrachesDate();
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<BranchesDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showBrachesDate()
    {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, branchNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        designation_spinner.setAdapter(spinnerClass);
        designation_spinner.setSelection(SELECTBRANCHPOS);
        designation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    BRANCH_ID = "";
                } else {
                    BRANCH_ID=branchIdsList.get(position-1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
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
                        || BRANCH_ID == null || "".equalsIgnoreCase(BRANCH_ID)|| BRANCH_ID.equals("")
                        //   || LOCATIONVAL == null|| "".equalsIgnoreCase(LOCATIONVAL)|| LOCATIONVAL.equals("")
                        || EMAILID == null || "".equalsIgnoreCase(EMAILID)|| EMAILID.equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    submitDetailsWithRetorfit(FIRSTNAME,LASTNAME,MOBILENUMBER,DATEVAL,ADDRESS,EMAILID);
                }
                break;
            case R.id.add_location:
            //    callPlaceAutocompleteActivityIntent();
                break;
            case R.id.attach_address1:
               // selectImage1();
                break;
            case R.id.attach_address2:
               // selectImage2();
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
        Call<Login> mService = mApiService.updateAdmin(
                FRISTNAME,LASTNAME,"ADMIN",MOBILENUMBER,DOB,ADDRESS,PRIMARYID,EMAILID,"0.0","0.0",
                BRANCH_ID,IDVAL);
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
                        if(AdminListActivity.mainfinish!=null)
                        {
                            AdminListActivity.mainfinish.finish();
                        }
                        finish();
                        Intent intent = new Intent(getApplicationContext(), AdminListActivity.class);
                        intent.putExtra("SHORTFROM","ADMIN");
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.date) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getSupportFragmentManager(), "Date Picker");
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
}
