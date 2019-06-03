package com.mrnovacrm.b2b_finance_dept;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_superadmin.AdminListActivity;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;

import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFinanceEmployeesActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    public static EditText date;
    EditText address,first_name,last_name,mobile_edtxt,email,location;
    Button save_btn;
    EditText address_proof1, address_proof2;
    Button add_location, attach_address1, attach_address2;
    Spinner designation_spinner;
    private String PRIMARYID;
    Context mContext;
    String SHORTFROM="";
    String TITLE="";
    String IDVAL="",NAMEVAL="",FIRSTNAMEVAL="",LASTNAMEVAL="",EMPIDVAL="",BRANCHIDVAL="",MOBILEVAL="",EMAILIDVAL="",
            DOBVAL="",ADDRESSVAL="",ROLENAMEVAL="",BRANCHNAMEVAL="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setTitle("");
        mContext=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_employee_registration);

        Bundle bundle=getIntent().getExtras();
        SHORTFROM= bundle.getString("SHORTFROM");
        TITLE= bundle.getString("TITLE");
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

        setTitle("Update "+EMPIDVAL);

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
        save_btn.setOnClickListener(UpdateFinanceEmployeesActivity.this);
        date.setOnTouchListener(UpdateFinanceEmployeesActivity.this);

        email = findViewById(R.id.email);
        email.setText(EMAILIDVAL);

        location = findViewById(R.id.location);
        add_location = findViewById(R.id.add_location);
        add_location.setOnClickListener(UpdateFinanceEmployeesActivity.this);
        attach_address1 = findViewById(R.id.attach_address1);
        attach_address1.setOnClickListener(UpdateFinanceEmployeesActivity.this);
        attach_address2 = findViewById(R.id.attach_address2);
        attach_address2.setOnClickListener(UpdateFinanceEmployeesActivity.this);
        address_proof1 = findViewById(R.id.address_proof1);
        address_proof2 = findViewById(R.id.address_proof2);
        designation_spinner = findViewById(R.id.designation_spinner);
        designation_spinner.setVisibility(View.GONE);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
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
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    submitDetailsWithRetorfit(FIRSTNAME,LASTNAME,MOBILENUMBER,DATEVAL,ADDRESS,EMAILID);
                }
                break;
            case R.id.add_location:
              //  callPlaceAutocompleteActivityIntent();
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

          Call<Login> mService = mApiService.updateAdmin(FRISTNAME,LASTNAME,SHORTFROM,MOBILENUMBER
                ,DOB,ADDRESS,PRIMARYID,EMAILID,"0.0","0.0",BRANCHIDVAL,IDVAL);


        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response", "" + response);
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    Log.e("status", "" + status);
                    if (status.equals("1")) {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        if(SHORTFROM.equals("FM"))
                        {
                            if(AdminListActivity.mainfinish!=null)
                            {
                                AdminListActivity.mainfinish.finish();
                            }
                            Intent intent=new Intent(getApplicationContext(),AdminListActivity.class);
                            intent.putExtra("SHORTFROM",SHORTFROM);
                            startActivity(intent);
                        }else{
                            if(FinanceEmployeesListActivity.mainfinish!=null)
                            {
                                FinanceEmployeesListActivity.mainfinish.finish();
                            }
                            Intent intent=new Intent(getApplicationContext(),FinanceEmployeesListActivity.class);
                            intent.putExtra("SHORTFROM",SHORTFROM);
                            intent.putExtra("TITLE",TITLE);
                            startActivity(intent);
                        }
                        finish();
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
