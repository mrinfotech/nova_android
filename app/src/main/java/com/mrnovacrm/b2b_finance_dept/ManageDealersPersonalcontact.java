package com.mrnovacrm.b2b_finance_dept;

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
import com.mrnovacrm.b2b_sales_dept.SalesMenuScreenActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DealerRegisrationDTO;
import com.mrnovacrm.model.Login;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageDealersPersonalcontact extends AppCompatActivity implements View.OnClickListener {

    Button nextbtn;
    public static Activity mainfinish;
    private DealerRegisrationDTO dealerregdto;
    EditText personalmobile_txt,personalemail_txt;
    Context mContext;
    private String MOBILEOTPVAL;
    private Dialog otpalertDialog;
    private View otplayout;
    private EditText otpmobilenumber;
    private Button popup_submit;
    private String mobileOTP;
    private String BRANCHID;
    private String PRIMARYID;
    private String COMPANY_VAL="";
    GlobalShare globalShare;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        mContext=this;
        globalShare=(GlobalShare)getApplicationContext();

        setTheme(R.style.AppTheme);
        setTitle("Manage Dealer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.layout_managedealerspersonalcontact);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            BRANCHID = values.get(SharedDB.BRANCHID);
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            COMPANY_VAL = values.get(SharedDB.COMPANY);
        }

        Intent intent = getIntent();
        dealerregdto = (DealerRegisrationDTO) intent.getSerializableExtra("dealerregdto");

        personalmobile_txt=findViewById(R.id.personalmobile_txt);
        personalemail_txt=findViewById(R.id.personalemail_txt);

        nextbtn=findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(ManageDealersPersonalcontact.this);
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
                    dealerregdto.setPersnolemail(PERSONALEMAIL);
                    dealerregdto.setPersnolmobile(PERSONALMOBILE);

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
        otpalertDialog = new Dialog(ManageDealersPersonalcontact.this);
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
                        otpalertDialog.dismiss();
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

        String DEALERNAME=dealerregdto.getDealerName();
        String companyName=dealerregdto.getCompanyName();
        String keypersonname=dealerregdto.getKeypersonname();
        String address=dealerregdto.getAddress();
        String country=dealerregdto.getCountry();
        String state=dealerregdto.getState();
        String town=dealerregdto.getTown();
        String pincode=dealerregdto.getPincode();

        String paNnumber=dealerregdto.getPANnumber();
        String gstiNnumber=dealerregdto.getGSTINnumber();
        String regastrtiontype=dealerregdto.getRegastrtiontype();
        String bankName=dealerregdto.getBankName();
        String accountnumber=dealerregdto.getAccountnumber();
        String ifsCcode=dealerregdto.getIFSCcode();

        String dealerID=dealerregdto.getDealerID();
        String officialemail=dealerregdto.getOfficialemail();
        String officialcontact=dealerregdto.getOfficialcontact();
        String officialwhatsapp=dealerregdto.getOfficialwhatsapp();
        String areaSalesManager=dealerregdto.getAreaSalesManager();
        String areamanagercontactnumber=dealerregdto.getAreamanagercontactnumber();

        String persnolmobile=dealerregdto.getPersnolmobile();
        String persnolemail=dealerregdto.getPersnolemail();
        String licencenumber=dealerregdto.getLicencenumber();



        Log.e("DEALERNAME",DEALERNAME);
        Log.e("companyName",companyName);
        Log.e("keypersonname",keypersonname);
        Log.e("address",address);
        Log.e("country",country);
        Log.e("state",state);
        Log.e("town",town);
        Log.e("pincode",pincode);
        Log.e("paNnumber",paNnumber);
        Log.e("gstiNnumber",gstiNnumber);
        Log.e("regastrtiontype",regastrtiontype);
        Log.e("bankName",bankName);
        Log.e("accountnumber",accountnumber);
        Log.e("ifsCcode",ifsCcode);
        Log.e("dealerID",dealerID);
        Log.e("officialemail",officialemail);
        Log.e("officialcontact",officialcontact);
        Log.e("officialwhatsapp",officialwhatsapp);
        Log.e("areaSalesManager",areaSalesManager);
        Log.e("areamaactnumber",areamanagercontactnumber);
        Log.e("persnolmobile",persnolmobile);
        Log.e("persnolemail",persnolemail);

        Log.e("PRIMARYID",PRIMARYID);
        Log.e("COMPANY_VAL",COMPANY_VAL);
        Log.e("BRANCHID",BRANCHID);

        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Login> mService = mApiService.addDealer(
                DEALERNAME,companyName,keypersonname,address,country,state,town,
                pincode,paNnumber,gstiNnumber,regastrtiontype,bankName,accountnumber,ifsCcode,dealerID,officialemail,
                officialcontact,officialwhatsapp,PRIMARYID,areamanagercontactnumber,persnolmobile,
                persnolemail,PRIMARYID,"DEALER",COMPANY_VAL,BRANCHID,licencenumber);
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

                        if(ManageDealersAccountActivity.mainfinish!=null)
                        {
                            ManageDealersAccountActivity.mainfinish.finish();
                        }

                        if(ManageDealersContactsActivity.mainfinish!=null)
                        {
                            ManageDealersContactsActivity.mainfinish.finish();
                        }

                        if(ManageDealersPersonalcontact.mainfinish!=null)
                        {
                            ManageDealersPersonalcontact.mainfinish.finish();
                        }

                        if(SalesMenuScreenActivity.mainfinish!=null)
                        {
                            SalesMenuScreenActivity.mainfinish.finish();
                        }

                        globalShare.setSalesmenuselectpos("4");
                        Intent intent=new Intent(getApplicationContext(),SalesMenuScreenActivity.class);
                        startActivity(intent);
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