package com.mrnovacrm.b2b_finance_dept;

//public class ManageEmployeePersonalcontact {
//}


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_admin.AdminMenuScreenActivity;
import com.mrnovacrm.b2b_superadmin.SuperAdminMenuScrenActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.EmployeeRegistrationDTO;
import com.mrnovacrm.model.Login;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageEmployeePersonalcontact extends AppCompatActivity implements View.OnClickListener {

    Button nextbtn;
    public static Activity mainfinish;
    EditText personalmobile_txt,personalemail_txt;
    Context mContext;
    private String MOBILEOTPVAL;
    private Dialog otpalertDialog;
    private View otplayout;
    private EditText otpmobilenumber;
    private Button popup_submit;
    private String mobileOTP;
    private EmployeeRegistrationDTO empregdto;
    private String fromval;
    private String title;

    private String BRANCHID;
    private String PRIMARYID;
    private String COMPANY_VAL="";
    private String SHORTFORM="";
    GlobalShare globalShare;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        mContext=this;
        globalShare=(GlobalShare)getApplicationContext();

        setTheme(R.style.AppTheme);
        //setTitle("Manage Dealer");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            BRANCHID = values.get(SharedDB.BRANCHID);
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            COMPANY_VAL = values.get(SharedDB.COMPANY);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }

        Bundle bundle=getIntent().getExtras();
        fromval=bundle.getString("fromval");
        title=bundle.getString("title");
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.e("fromval",fromval);

        setContentView(R.layout.layout_managedealerspersonalcontact);

        Intent intent = getIntent();
        empregdto = (EmployeeRegistrationDTO) intent.getSerializableExtra("empregdto");

        personalmobile_txt=findViewById(R.id.personalmobile_txt);
        personalemail_txt=findViewById(R.id.personalemail_txt);

        nextbtn=findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(ManageEmployeePersonalcontact.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.nextbtn:
                String PERSONALMOBILE=personalmobile_txt.getText().toString();
                String PERSONALEMAIL=personalemail_txt.getText().toString();

                if (PERSONALMOBILE == null || "".equalsIgnoreCase(PERSONALMOBILE)|| PERSONALMOBILE.equals("")
                        || PERSONALEMAIL == null || "".equalsIgnoreCase(PERSONALEMAIL)|| PERSONALEMAIL.equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    empregdto.setPersnolmobile(PERSONALMOBILE);
                    empregdto.setPersnolemail(PERSONALEMAIL);
                    loginProcessWithRetrofit(PERSONALMOBILE);
                }
                break;
            default:
                break;
        }
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

    private void loginProcessWithRetrofit(final String mobilenumber){
        final TransparentProgressDialog dialog =new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.authenticate(mobilenumber);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response",""+response);
                    MOBILEOTPVAL = mLoginObject.getOtp();
                      Log.e("MOBILEOTPVAL",MOBILEOTPVAL);
                    showOTPPopup();
                }catch (Exception e)
                {
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

    public void showOTPPopup() {
        //	if(!globalShare.getSelectuserotp().equals("local")) {
        /** Used for Show Disclaimer Pop up screen */
        otpalertDialog = new Dialog(ManageEmployeePersonalcontact.this);
        otpalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpalertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = getLayoutInflater();
        otplayout = inflater.inflate(R.layout.loginotppopup, null);
        otpalertDialog.setContentView(otplayout);
        otpalertDialog.setCancelable(false);
        if (!otpalertDialog.isShowing()) {
            otpalertDialog.show();
        }

        ImageView cancel = (ImageView) otplayout.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                otpalertDialog.dismiss();
            }
        });

        otpmobilenumber = (EditText) otplayout.findViewById(R.id.otpmobilenumber);
        otpmobilenumber.setHintTextColor(getResources().getColor(R.color.darkgrey));

        popup_submit=(Button)otplayout.findViewById(R.id.popup_submit);
        popup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileOTP = otpmobilenumber.getText().toString().trim();
                if (mobileOTP == null || "".equalsIgnoreCase(mobileOTP)
                        || mobileOTP.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    if (mobileOTP.equals(MOBILEOTPVAL)) {

                        submitDetailsWithRetorfit();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        otpmobilenumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            private boolean isConnectedToInternet;
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    isConnectedToInternet = CheckNetWork
                            .isConnectedToInternet(getApplicationContext());
                    hideKeyboard();
                    if (isConnectedToInternet) {
                        mobileOTP = otpmobilenumber.getText().toString();
                        if (mobileOTP == null || "".equalsIgnoreCase(mobileOTP)
                                || mobileOTP.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter OTP", Toast.LENGTH_SHORT).show();
                        } else {
                            if (mobileOTP.equals(MOBILEOTPVAL)) {
                                otpalertDialog.dismiss();
                                submitDetailsWithRetorfit();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Check Your Internet Connection",
                                Toast.LENGTH_LONG).show();
                    }
                }
                return false;
            }
        });
        //}
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void submitDetailsWithRetorfit() {

        String EMPNAME=empregdto.getEmployeename();
        String DOB=empregdto.getDOB();
        String GENDER=empregdto.getGender();
        String fathername=empregdto.getFathername();
        String mothername=empregdto.getMothername();
        String martialstatus=empregdto.getMartialstatus();
        String address=empregdto.getAddress();
        String country=empregdto.getCountry();
        String state=empregdto.getState();
        String town=empregdto.getTown();
        String pincode=empregdto.getPincode();

        String paNnumber=empregdto.getPANnumber();
        String pfNumber=empregdto.getPFNumber();
        String esiNumber=empregdto.getESINumber();
        String bankName=empregdto.getBankName();
        String accountnumber=empregdto.getAccountnumber();
        String ifsCcode=empregdto.getIFSCcode();

        String employeeID=empregdto.getEmployeeID();
        String doj=empregdto.getDOJ();
        String department=empregdto.getDepartment();
        String designation=empregdto.getDesignation();
        String companyemail=empregdto.getCompanyemail();
        String companycontact=empregdto.getCompanycontact();
        String totalExperience=empregdto.getTotalExperience();
        String reportingManager=empregdto.getReportingManager();

        String persnolmobile=empregdto.getPersnolmobile();
        String persnolemail=empregdto.getPersnolemail();


        Log.e("DEALERNAME",EMPNAME);
        Log.e("DOB",DOB);
        Log.e("GENDER",GENDER);
        Log.e("fathername",fathername);
        Log.e("mothername",mothername);
        Log.e("martialstatus",martialstatus);
        Log.e("address",address);
        Log.e("country",country);
        Log.e("state",state);
        Log.e("town",town);
        Log.e("pincode",pincode);
        Log.e("paNnumber",paNnumber);
        Log.e("esiNumber",esiNumber);
        Log.e("bankName",bankName);
        Log.e("accountnumber",accountnumber);
        Log.e("ifsCcode",ifsCcode);

        Log.e("employeeID",employeeID);
        Log.e("doj",doj);
        Log.e("department",department);
        Log.e("designation",designation);
        Log.e("companyemail",companyemail);
        Log.e("companycontact",companycontact);
        Log.e("totalExperience",totalExperience);
        Log.e("reportingManager",reportingManager);
        Log.e("persnolmobile",persnolmobile);
        Log.e("persnolemail",persnolemail);

        Log.e("PRIMARYID",PRIMARYID);
        Log.e("COMPANY_VAL",COMPANY_VAL);
        Log.e("BRANCHID",BRANCHID);
        Log.e("fromval",fromval);


        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Login> mService = mApiService.manageEmployee(
                EMPNAME,DOB,GENDER,fathername,mothername,martialstatus,
                address,country,state,town,
                pincode,paNnumber,pfNumber,esiNumber,bankName,accountnumber,ifsCcode,employeeID,doj,
                department,designation,companyemail,companycontact,totalExperience,reportingManager,persnolmobile,
                persnolemail,PRIMARYID,fromval,COMPANY_VAL,BRANCHID);

        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response", "" + response);
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        otpalertDialog.dismiss();
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        if(ManageEmployeeAccountActivity.mainfinish!=null)
                        {
                            ManageEmployeeAccountActivity.mainfinish.finish();
                        }
                        if(ManageEmployeeCompanyActivity.mainfinish!=null)
                        {
                            ManageEmployeeCompanyActivity.mainfinish.finish();
                        }

                        if(SHORTFORM.equals("ADMIN"))
                        {
                            globalShare.setAdminmenuselectpos("2");
                            if(AdminMenuScreenActivity.mainfinish!=null)
                            {
                                AdminMenuScreenActivity.mainfinish.finish();
                            }
                            Intent intent=new Intent(getApplicationContext(),AdminMenuScreenActivity.class);
                            startActivity(intent);
                        }else  if(SHORTFORM.equals("SA"))
                        {
                            globalShare.setSuperadminmenuselectpos("3");

                            if(SuperAdminMenuScrenActivity.mainfinish!=null)
                            {
                                SuperAdminMenuScrenActivity.mainfinish.finish();
                            }
                            Intent intent=new Intent(getApplicationContext(),SuperAdminMenuScrenActivity.class);
                            startActivity(intent);
                        }else{
                            if(FinanceDeptMenuScreenActivity.mainfinish!=null)
                            {
                                FinanceDeptMenuScreenActivity.mainfinish.finish();
                            }
                            if(fromval.equals("PACKER"))
                            {
                                globalShare.setFinancemenuselectpos("2");
                            }
//                        else if(fromval.equals("DB"))
//                        {
//                            globalShare.setFinancemenuselectpos("3");
//                        }
                            else if(fromval.equals("SE"))
                            {
                                //  globalShare.setFinancemenuselectpos("4");
                                globalShare.setFinancemenuselectpos("3");
                            }
                            Intent intent=new Intent(getApplicationContext(),FinanceDeptMenuScreenActivity.class);
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
}