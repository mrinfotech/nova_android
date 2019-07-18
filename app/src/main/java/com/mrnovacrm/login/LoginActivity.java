package com.mrnovacrm.login;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_admin.AdminMenuScreenActivity;
import com.mrnovacrm.b2b_dealer.DealerMenuScreenActivity;
import com.mrnovacrm.b2b_dealer.DealerScreenActivity;
import com.mrnovacrm.b2b_delivery_dept.DeliveryMenuScreenActivity;
import com.mrnovacrm.b2b_dispatch_dept.DispatchMenuScreenActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceDeptMenuScreenActivity;
import com.mrnovacrm.b2b_superadmin.SuperAdminMenuScrenActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.ContactsModelDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.RolesModelDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 27-02-2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button loginbtn;
    TextView forgotpwdtxt;
    EditText mobilenumbertxt, passwordtxt;
    private String userName = "";
    int securePinLength = 0;
    private boolean isConnectedToInternet;
    String secure_Pin = "";
    Context context;
    private Dialog alertDialog;
    private View layout;
    private TextView submittxt;
    private EditText mobilenumberedittxt;
    private ImageView closeicon;
    private String forgotPwdMobileNumber = "";
    private String password = "";
    private GlobalShare globalShare;
    public static Activity mainfinish;
    RelativeLayout headrel;
    // TextView registertxt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        mainfinish = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        globalShare = (GlobalShare) getApplicationContext();

        loginbtn = findViewById(R.id.loginbtn);
        forgotpwdtxt = findViewById(R.id.forgotpwdtxt);

        mobilenumbertxt = findViewById(R.id.mobilenumbertxt);
        passwordtxt = findViewById(R.id.passwordtxt);
        headrel = findViewById(R.id.headrel);

        loginbtn.setOnClickListener(LoginActivity.this);
        forgotpwdtxt.setOnClickListener(LoginActivity.this);
        headrel.setOnClickListener(LoginActivity.this);

        passwordtxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        showValidation();
                    }
                } catch (Exception e) {
                }
                return false;
            }
        });
        hideKeyboard();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginbtn:
                showValidation();
                break;
            case R.id.forgotpwdtxt:
                showForgotPasswordPopup();
                break;
            case R.id.closeicon:
                alertDialog.dismiss();
                break;
            case R.id.submittxt:
                forgotPwdMobileNumber = mobilenumberedittxt.getText().toString().trim();
                if (forgotPwdMobileNumber == null || "".equalsIgnoreCase(forgotPwdMobileNumber)
                        || forgotPwdMobileNumber.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.mobilevalidmsg,
                            Toast.LENGTH_LONG).show();
                } else {
                    isConnectedToInternet = CheckNetWork
                            .isConnectedToInternet(LoginActivity.this);
                    if (isConnectedToInternet) {
                        forgotPasswordrocessWithRetrofit(forgotPwdMobileNumber);
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                getResources().getString(
                                        R.string.networkerror), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.headrel:
                hideKeyboard();
                break;
            default:
                break;
        }
    }

    public void showValidation() {
        userName = mobilenumbertxt.getText().toString();
        password = passwordtxt.getText().toString();
        if (userName.equals("") && password.equals("")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pleaseentermobilesecurepin),
                    Toast.LENGTH_SHORT).show();
        } else {
            checkCredentials();
        }
    }

    public void checkCredentials() {
        isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(this);
        if (isConnectedToInternet) {
            loginProcessWithRetrofit(userName, password);
        } else {
            Toast.makeText(getApplicationContext(), R.string.networkerror,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loginProcessWithRetrofit(final String user_name, final String user_password) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.checkLogin(user_name, user_password);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("Response", " :" + response);
                Login mLoginObject = response.body();
                Log.e("mLoginObject", " :" + mLoginObject);
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    Log.e("Status", " :" + status);
                    String message = mLoginObject.getMessage();
                    if (status.equals("1")) {
                        SharedDB.saveToLoginPrefs(getApplicationContext(), user_name, user_password);
                        String role = mLoginObject.getRole();
                        String primaryId = mLoginObject.getPrimaryid();
                        String userName = mLoginObject.getUserName();
                        String mobile = mLoginObject.getMobile();
                        String address = mLoginObject.getAddress();
                        String pincode = mLoginObject.getPincode();
                        String imageurl = mLoginObject.getDp();
                        String latitude = mLoginObject.getLatitude();
                        String longitude = mLoginObject.getLongitude();
                        String address_id = mLoginObject.getAddress_id();
                        String branch_id = mLoginObject.getBranch();
                        String branch_name = mLoginObject.getBranch_name();
                        String locale = mLoginObject.getLocale();
                        String short_form = mLoginObject.getShort_form();
                        String company = mLoginObject.getCompany();
                        String branch_contact = mLoginObject.getBranch_contact();

                        String branch_count = mLoginObject.getBranch_count();
                        String role_count = mLoginObject.getRole_count();
                        try {
                            List<ContactsModelDTO> companiesDTOList = mLoginObject.getCompaniesDTOList();
                            int companieslistsize = 0;
                            if (companiesDTOList != null) {
                                if (companiesDTOList.size() > 0) {
                                    companieslistsize = companiesDTOList.size();
                                    globalShare.setCompaniesList(companiesDTOList);
                                }
                            } else {
                                globalShare.setCompaniesList(companiesDTOList);
                            }
                            globalShare.setNotificationfrom("login");

                            if (short_form.equals("DEALER")) {
                                double lat = 0.0;
                                double lon = 0.0;
                                try {
                                    if (!latitude.equals("") || !longitude.equals("")) {
                                        lat = Double.parseDouble(latitude);
                                        lon = Double.parseDouble(longitude);
                                    }
                                } catch (Exception e) {
                                }
                                globalShare.setLoginselectedfromval("loginscreen");
                                SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
                                        "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
                                        branch_name, short_form, company, branch_contact, String.valueOf(companieslistsize),
                                        branch_count, role_count, "0");

                                if (short_form.equals("ADMIN")) {
                                    globalShare.setAdminmenuselectpos("1");
                                    Intent intent = new Intent(getApplicationContext(),
                                            AdminMenuScreenActivity.class);
                                    startActivity(intent);
                                } else if (short_form.equals("FM")) {
                                    globalShare.setFinancemenuselectpos("1");
                                    Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
                                    startActivity(intent);
                                } else if (short_form.equals("SE")) {

                                    globalShare.setDealerMenuSelectedPos("1");
                                    Intent intent = new Intent(getApplicationContext(),
                                            DealerMenuScreenActivity.class);
                                    startActivity(intent);
                                } else if (short_form.equals("DB")) {
                                    globalShare.setDeliverymenuselectpos("1");
                                    Intent intent = new Intent(getApplicationContext(),
                                            DeliveryMenuScreenActivity.class);
                                    startActivity(intent);
                                } else if (short_form.equals("PACKER")) {
                                    globalShare.setDispatchmenuselectpos("1");
                                    Intent intent = new Intent(getApplicationContext(),
                                            DispatchMenuScreenActivity.class);
                                    startActivity(intent);
                                } else if (short_form.equals("DEALER")) {
                                    globalShare.setDealerMenuSelectedPos("1");
                                    Intent intent = new Intent(getApplicationContext(),
                                            DealerScreenActivity.class);
                                    startActivity(intent);

                                } else if (short_form.equals("SA")) {
                                    globalShare.setSuperadminmenuselectpos("1");
                                    Intent intent = new Intent(getApplicationContext(),
                                            SuperAdminMenuScrenActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            } else {
                                List<RolesModelDTO> rolesDTOList = mLoginObject.getRolesDTOLis();
                                if (rolesDTOList != null) {
                                    if (rolesDTOList.size() > 0) {
                                        globalShare.setRolesList(rolesDTOList);
                                        if (rolesDTOList.size() == 1) {
                                            String id = rolesDTOList.get(0).getId();
                                            double lat = 0.0;
                                            double lon = 0.0;
                                            try {
                                                if (!latitude.equals("") || !longitude.equals("")) {
                                                    lat = Double.parseDouble(latitude);
                                                    lon = Double.parseDouble(longitude);
                                                }
                                            } catch (Exception e) {
                                            }
                                            globalShare.setLoginselectedfromval("loginscreen");
                                            SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
                                                    "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
                                                    branch_name, short_form, company, branch_contact, String.valueOf(companieslistsize),
                                                    branch_count, role_count, id);

                                            if (short_form.equals("ADMIN")) {
                                                globalShare.setAdminmenuselectpos("1");
                                                Intent intent = new Intent(getApplicationContext(),
                                                        AdminMenuScreenActivity.class);
                                                startActivity(intent);
                                            } else if (short_form.equals("FM")) {
                                                globalShare.setFinancemenuselectpos("1");
                                                Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
                                                startActivity(intent);
                                            } else if (short_form.equals("SE")) {

                                                globalShare.setDealerMenuSelectedPos("1");
                                                Intent intent = new Intent(getApplicationContext(),
                                                        DealerMenuScreenActivity.class);
                                                startActivity(intent);
                                            } else if (short_form.equals("DB")) {
                                                globalShare.setDeliverymenuselectpos("1");
                                                Intent intent = new Intent(getApplicationContext(),
                                                        DeliveryMenuScreenActivity.class);
                                                startActivity(intent);
                                            } else if (short_form.equals("PACKER")) {
                                                globalShare.setDispatchmenuselectpos("1");
                                                Intent intent = new Intent(getApplicationContext(),
                                                        DispatchMenuScreenActivity.class);
                                                startActivity(intent);
                                            } else if (short_form.equals("DEALER")) {
                                                globalShare.setDealerMenuSelectedPos("1");
                                                Intent intent = new Intent(getApplicationContext(),
                                                        DealerScreenActivity.class);
                                                startActivity(intent);
                                            } else if (short_form.equals("SA")) {
                                                globalShare.setSuperadminmenuselectpos("1");
                                                Intent intent = new Intent(getApplicationContext(),
                                                        SuperAdminMenuScrenActivity.class);
                                                startActivity(intent);
                                            }
                                            finish();
                                        } else {
                                            double lat = 0.0;
                                            double lon = 0.0;
                                            try {
                                                if (!latitude.equals("") || !longitude.equals("")) {
                                                    lat = Double.parseDouble(latitude);
                                                    lon = Double.parseDouble(longitude);
                                                }
                                            } catch (Exception e) {
                                            }

                                            SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
                                                    "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
                                                    branch_name, short_form, company, branch_contact, String.valueOf(companieslistsize),
                                                    branch_count, role_count, "");


                                            SharedDB.multiroleSahred(getApplicationContext(), branch_id, branch_name, short_form, company,
                                                    "", "", "");

                                            Intent intent = new Intent(getApplicationContext(), LoginRoleBranchesActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                } else {
                                    globalShare.setRolesList(rolesDTOList);

                                    double lat = 0.0;
                                    double lon = 0.0;
                                    try {
                                        if (!latitude.equals("") || !longitude.equals("")) {
                                            lat = Double.parseDouble(latitude);
                                            lon = Double.parseDouble(longitude);
                                        }
                                    } catch (Exception e) {
                                    }
                                    globalShare.setLoginselectedfromval("loginscreen");
                                    SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
                                            "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
                                            branch_name, short_form, company, branch_contact, String.valueOf(companieslistsize),
                                            branch_count, role_count, "0");

                                    if (short_form.equals("ADMIN")) {
                                        globalShare.setAdminmenuselectpos("1");
                                        Intent intent = new Intent(getApplicationContext(),
                                                AdminMenuScreenActivity.class);
                                        startActivity(intent);
                                    } else if (short_form.equals("FM")) {
                                        globalShare.setFinancemenuselectpos("1");
                                        Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
                                        startActivity(intent);
                                    } else if (short_form.equals("SE")) {

                                        globalShare.setDealerMenuSelectedPos("1");
                                        Intent intent = new Intent(getApplicationContext(),
                                                DealerMenuScreenActivity.class);
                                        startActivity(intent);
                                    } else if (short_form.equals("DB")) {
                                        globalShare.setDeliverymenuselectpos("1");
                                        Intent intent = new Intent(getApplicationContext(),
                                                DeliveryMenuScreenActivity.class);
                                        startActivity(intent);
                                    } else if (short_form.equals("PACKER")) {
                                        globalShare.setDispatchmenuselectpos("1");
                                        Intent intent = new Intent(getApplicationContext(),
                                                DispatchMenuScreenActivity.class);
                                        startActivity(intent);
                                    } else if (short_form.equals("DEALER")) {
                                        globalShare.setDealerMenuSelectedPos("1");
                                        Intent intent = new Intent(getApplicationContext(),
                                                DealerScreenActivity.class);
                                        startActivity(intent);
                                    } else if (short_form.equals("SA")) {
                                        globalShare.setSuperadminmenuselectpos("1");
                                        Intent intent = new Intent(getApplicationContext(),
                                                SuperAdminMenuScrenActivity.class);
                                        startActivity(intent);
                                    }
                                    finish();
                                }
                            }

//                            if(short_form.equals("ADMIN")||short_form.equals("PACKER")|| short_form.equals("FM")
//                                    || short_form.equals("SE"))
//                            {
//                                if(branch_count.equals(role_count))
//                                {
//                                    if(Integer.parseInt(branch_count)>1 || Integer.parseInt(role_count)>1)
//                                    {
//                                        double lat = 0.0;
//                                        double lon = 0.0;
//                                        try {
//                                            if (!latitude.equals("") || !longitude.equals("")) {
//                                                lat = Double.parseDouble(latitude);
//                                                lon = Double.parseDouble(longitude);
//                                            }
//                                        } catch (Exception e) {
//                                        }
//
//                                        SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                                "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                                branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                                branch_count,role_count);
//
//
//                                        SharedDB.multiroleSahred(getApplicationContext(), branch_id, branch_name, short_form,company,
//                                                "","","");
//
//                                        Intent intent=new Intent(getApplicationContext(),LoginRoleBranchesActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }else {
//                                        double lat = 0.0;
//                                        double lon = 0.0;
//                                        try {
//                                            if (!latitude.equals("") || !longitude.equals("")) {
//                                                lat = Double.parseDouble(latitude);
//                                                lon = Double.parseDouble(longitude);
//                                            }
//                                        } catch (Exception e) {
//                                        }
//                                        globalShare.setLoginselectedfromval("loginscreen");
//                                        SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                                "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                                branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                                branch_count,role_count);
//
//                                        if (short_form.equals("ADMIN")) {
//                                            globalShare.setAdminmenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    AdminMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("FM")) {
//                                            globalShare.setFinancemenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("SE")) {
//
//                                            globalShare.setDealerMenuSelectedPos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    DealerMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("DB")) {
//                                            globalShare.setDeliverymenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    DeliveryMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("PACKER")) {
//                                            globalShare.setDispatchmenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    DispatchMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("DEALER")) {
//                                            globalShare.setDealerMenuSelectedPos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    DealerMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("SA")) {
//                                            globalShare.setSuperadminmenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    SuperAdminMenuScrenActivity.class);
//                                            startActivity(intent);
//                                        }
//                                        finish();
//                                    }
//                                }else{
//                                    if(Integer.parseInt(branch_count)>1 || Integer.parseInt(role_count)>1)
//                                    {
//                                        double lat = 0.0;
//                                        double lon = 0.0;
//                                        try {
//                                            if (!latitude.equals("") || !longitude.equals("")) {
//                                                lat = Double.parseDouble(latitude);
//                                                lon = Double.parseDouble(longitude);
//                                            }
//                                        } catch (Exception e) {
//                                        }
//
//                                        SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                                "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                                branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                                branch_count,role_count);
//
//
//                                        SharedDB.multiroleSahred(getApplicationContext(), branch_id, branch_name, short_form,company,
//                                                "","","");
//
//                                        Intent intent=new Intent(getApplicationContext(),LoginRoleBranchesActivity.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }else{
//                                        double lat = 0.0;
//                                        double lon = 0.0;
//                                        try {
//                                            if (!latitude.equals("") || !longitude.equals("")) {
//                                                lat = Double.parseDouble(latitude);
//                                                lon = Double.parseDouble(longitude);
//                                            }
//                                        } catch (Exception e) {
//                                        }
//                                        globalShare.setLoginselectedfromval("loginscreen");
//                                        SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                                "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                                branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                                branch_count,role_count);
//
//                                        if (short_form.equals("ADMIN")) {
//                                            globalShare.setAdminmenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    AdminMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("FM")) {
//                                            globalShare.setFinancemenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("SE")) {
//
//                                            globalShare.setDealerMenuSelectedPos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    DealerMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("DB")) {
//                                            globalShare.setDeliverymenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    DeliveryMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("PACKER")) {
//                                            globalShare.setDispatchmenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    DispatchMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("DEALER")) {
//                                            globalShare.setDealerMenuSelectedPos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    DealerMenuScreenActivity.class);
//                                            startActivity(intent);
//                                        } else if (short_form.equals("SA")) {
//                                            globalShare.setSuperadminmenuselectpos("1");
//                                            Intent intent = new Intent(getApplicationContext(),
//                                                    SuperAdminMenuScrenActivity.class);
//                                            startActivity(intent);
//                                        }
//                                        finish();
//                                    }
//                                }
//                            }else{
//                                double lat = 0.0;
//                                double lon = 0.0;
//                                try {
//                                    if (!latitude.equals("") || !longitude.equals("")) {
//                                        lat = Double.parseDouble(latitude);
//                                        lon = Double.parseDouble(longitude);
//                                    }
//                                } catch (Exception e) {
//                                }
//                                globalShare.setLoginselectedfromval("loginscreen");
//                                SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                        "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                        branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                        branch_count,role_count);
//
//                                if (short_form.equals("ADMIN")) {
//                                    globalShare.setAdminmenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            AdminMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("FM")) {
//                                    globalShare.setFinancemenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("SE")) {
//
//                                    globalShare.setDealerMenuSelectedPos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            DealerMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("DB")) {
//                                    globalShare.setDeliverymenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            DeliveryMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("PACKER")) {
//                                    globalShare.setDispatchmenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            DispatchMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("DEALER")) {
//                                    globalShare.setDealerMenuSelectedPos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            DealerMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("SA")) {
//                                    globalShare.setSuperadminmenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            SuperAdminMenuScrenActivity.class);
//                                    startActivity(intent);
//                                }
//                                finish();
//                            }
                        } catch (Exception e) {
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showForgotPasswordPopup() {
        /** Used for Show Disclaimer Pop up screen */
        alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = this.getLayoutInflater();
        layout = inflater.inflate(R.layout.forgotpasswordpopup, null);
        alertDialog.setContentView(layout);
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        submittxt = layout.findViewById(R.id.submittxt);
        mobilenumberedittxt = layout
                .findViewById(R.id.mobilenumberedittxt);

        closeicon = layout.findViewById(R.id.closeicon);
        closeicon.setOnClickListener(LoginActivity.this);
        submittxt.setOnClickListener(LoginActivity.this);
    }

    private void forgotPasswordrocessWithRetrofit(String mobileno) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Login> mService = mApiService.getForgotPassword(mobileno);
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
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
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
                //     Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            SharedPreferences sharedpreferences = getSharedPreferences(
                    SharedDB.LOGINPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();
            SharedDB.clearAuthentication(LoginActivity.this);
        } catch (Exception e) {
        }
    }
}


//package com.nova.login;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nova.R;
//import com.nova.b2b_admin.AdminMenuScreenActivity;
//import com.nova.b2b_dealer.DealerMenuScreenActivity;
//import com.nova.b2b_delivery_dept.DeliveryMenuScreenActivity;
//import com.nova.b2b_dispatch_dept.DispatchMenuScreenActivity;
//import com.nova.b2b_finance_dept.FinanceDeptMenuScreenActivity;
//import com.nova.b2b_superadmin.SuperAdminMenuScrenActivity;
//import com.nova.constants.CheckNetWork;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.ContactsModelDTO;
//import com.nova.model.Login;
//import com.nova.model.RolesModelDTO;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
///**
// * Created by android on 27-02-2018.
// */
//public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
//    Button loginbtn;
//    TextView forgotpwdtxt;
//    EditText mobilenumbertxt, passwordtxt;
//    private String userName = "";
//    int securePinLength = 0;
//    private boolean isConnectedToInternet;
//    String secure_Pin = "";
//    Context context;
//    private Dialog alertDialog;
//    private View layout;
//    private TextView submittxt;
//    private EditText mobilenumberedittxt;
//    private ImageView closeicon;
//    private String forgotPwdMobileNumber = "";
//    private String password = "";
//    private GlobalShare globalShare;
//    public static Activity mainfinish;
//    RelativeLayout headrel;
//    // TextView registertxt;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = this;
//        mainfinish = this;
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_login);
//        globalShare = (GlobalShare) getApplicationContext();
//
//        loginbtn = findViewById(R.id.loginbtn);
//        forgotpwdtxt = findViewById(R.id.forgotpwdtxt);
//
//        mobilenumbertxt = findViewById(R.id.mobilenumbertxt);
//        passwordtxt = findViewById(R.id.passwordtxt);
//        headrel = findViewById(R.id.headrel);
//
//        loginbtn.setOnClickListener(LoginActivity.this);
//        forgotpwdtxt.setOnClickListener(LoginActivity.this);
//        headrel.setOnClickListener(LoginActivity.this);
//
//        passwordtxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                try{
//                    if (actionId == EditorInfo.IME_ACTION_DONE) {
//                        showValidation();
//                    }
//                }catch (Exception e)
//                {
//                }
//                return false;
//            }
//        });
//     //   createnewfirebaseid();
//        hideKeyboard();
//
////        startService(new Intent(this, MyFirebaseInstanceIDService.class));
////        startService(new Intent(this, MyFirebaseMessagingService.class));
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.loginbtn:
//                showValidation();
//                break;
//            case R.id.forgotpwdtxt:
//                showForgotPasswordPopup();
//                break;
//            case R.id.closeicon:
//                alertDialog.dismiss();
//                break;
//            case R.id.submittxt:
//                forgotPwdMobileNumber = mobilenumberedittxt.getText().toString().trim();
//                if (forgotPwdMobileNumber == null || "".equalsIgnoreCase(forgotPwdMobileNumber)
//                        || forgotPwdMobileNumber.equals("")) {
//                    Toast.makeText(getApplicationContext(), R.string.mobilevalidmsg,
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    isConnectedToInternet = CheckNetWork
//                            .isConnectedToInternet(LoginActivity.this);
//                    if (isConnectedToInternet) {
//                        forgotPasswordrocessWithRetrofit(forgotPwdMobileNumber);
//                    } else {
//                        Toast.makeText(
//                                getApplicationContext(),
//                                getResources().getString(
//                                        R.string.networkerror), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//            case R.id.headrel:
//                hideKeyboard();
//                break;
//            default:
//                break;
//        }
//    }
//
//    public void showValidation()
//    {
//        userName = mobilenumbertxt.getText().toString();
//        password = passwordtxt.getText().toString();
//        if (userName.equals("") && password.equals("")) {
//            Toast.makeText(getApplicationContext(),getResources().getString(R.string.pleaseentermobilesecurepin),
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            checkCredentials();
//        }
//    }
//
//    public void checkCredentials() {
//        isConnectedToInternet = CheckNetWork
//                .isConnectedToInternet(this);
//        if (isConnectedToInternet) {
//            loginProcessWithRetrofit(userName, password);
//        } else {
//            Toast.makeText(getApplicationContext(), R.string.networkerror,
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void loginProcessWithRetrofit(String user_name, String user_password) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Login> mService = mApiService.checkLogin(user_name, user_password);
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                Log.e("Response", " :" + response);
//                Login mLoginObject = response.body();
//                Log.e("mLoginObject", " :" + mLoginObject);
//                dialog.dismiss();
//                try {
//                    String status = mLoginObject.getStatus();
//                    Log.e("Status", " :" + status);
//                    String message = mLoginObject.getMessage();
//                    if (status.equals("1")) {
//                        String role = mLoginObject.getRole();
//                        String primaryId = mLoginObject.getPrimaryid();
//                        String userName = mLoginObject.getUserName();
//                        String mobile = mLoginObject.getMobile();
//                        String address = mLoginObject.getAddress();
//                        String pincode = mLoginObject.getPincode();
//                        String imageurl = mLoginObject.getDp();
//                        String latitude = mLoginObject.getLatitude();
//                        String longitude = mLoginObject.getLongitude();
//                        String address_id = mLoginObject.getAddress_id();
//                        String branch_id = mLoginObject.getBranch();
//                        String branch_name = mLoginObject.getBranch_name();
//                        String locale = mLoginObject.getLocale();
//                        String short_form = mLoginObject.getShort_form();
//                        String company = mLoginObject.getCompany();
//                        String branch_contact = mLoginObject.getBranch_contact();
//
//                        String branch_count = mLoginObject.getBranch_count();
//                        String role_count = mLoginObject.getRole_count();
//                        try{
//                            List<ContactsModelDTO> companiesDTOList=mLoginObject.getCompaniesDTOList();
//                            int companieslistsize=0;
//                            if(companiesDTOList!=null)
//                            {
//                                if(companiesDTOList.size()>0)
//                                {
//                                    companieslistsize=companiesDTOList.size();
//                                    globalShare.setCompaniesList(companiesDTOList);
//                                }
//                            }else{
//                                globalShare.setCompaniesList(companiesDTOList);
//                            }
//                            globalShare.setNotificationfrom("login");
//
//                            if(short_form.equals("DEALER"))
//                            {
//                                double lat = 0.0;
//                                double lon = 0.0;
//                                try {
//                                    if (!latitude.equals("") || !longitude.equals("")) {
//                                        lat = Double.parseDouble(latitude);
//                                        lon = Double.parseDouble(longitude);
//                                    }
//                                } catch (Exception e) {
//                                }
//                                globalShare.setLoginselectedfromval("loginscreen");
//                                SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                        "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                        branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                        branch_count,role_count,"0");
//
//                                if (short_form.equals("ADMIN")) {
//                                    globalShare.setAdminmenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            AdminMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("FM")) {
//                                    globalShare.setFinancemenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("SE")) {
//
//                                    globalShare.setDealerMenuSelectedPos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            DealerMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("DB")) {
//                                    globalShare.setDeliverymenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            DeliveryMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("PACKER")) {
//                                    globalShare.setDispatchmenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            DispatchMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("DEALER")) {
//                                    globalShare.setDealerMenuSelectedPos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            DealerMenuScreenActivity.class);
//                                    startActivity(intent);
//                                } else if (short_form.equals("SA")) {
//                                    globalShare.setSuperadminmenuselectpos("1");
//                                    Intent intent = new Intent(getApplicationContext(),
//                                            SuperAdminMenuScrenActivity.class);
//                                    startActivity(intent);
//                                }
//                                finish();
//                            }else{
//                                List<RolesModelDTO> rolesDTOList=mLoginObject.getRolesDTOLis();
//                                if(rolesDTOList!=null)
//                                {
//                                    if(rolesDTOList.size()>0)
//                                    {
//                                        globalShare.setRolesList(rolesDTOList);
//                                        if(rolesDTOList.size()==1)
//                                        {
//                                            String id= rolesDTOList.get(0).getId();
//                                            double lat = 0.0;
//                                            double lon = 0.0;
//                                            try {
//                                                if (!latitude.equals("") || !longitude.equals("")) {
//                                                    lat = Double.parseDouble(latitude);
//                                                    lon = Double.parseDouble(longitude);
//                                                }
//                                            } catch (Exception e) {
//                                            }
//                                            globalShare.setLoginselectedfromval("loginscreen");
//                                            SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                                    "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                                    branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                                    branch_count,role_count,id);
//
//                                            if (short_form.equals("ADMIN")) {
//                                                globalShare.setAdminmenuselectpos("1");
//                                                Intent intent = new Intent(getApplicationContext(),
//                                                        AdminMenuScreenActivity.class);
//                                                startActivity(intent);
//                                            } else if (short_form.equals("FM")) {
//                                                globalShare.setFinancemenuselectpos("1");
//                                                Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
//                                                startActivity(intent);
//                                            } else if (short_form.equals("SE")) {
//
//                                                globalShare.setDealerMenuSelectedPos("1");
//                                                Intent intent = new Intent(getApplicationContext(),
//                                                        DealerMenuScreenActivity.class);
//                                                startActivity(intent);
//                                            } else if (short_form.equals("DB")) {
//                                                globalShare.setDeliverymenuselectpos("1");
//                                                Intent intent = new Intent(getApplicationContext(),
//                                                        DeliveryMenuScreenActivity.class);
//                                                startActivity(intent);
//                                            } else if (short_form.equals("PACKER")) {
//                                                globalShare.setDispatchmenuselectpos("1");
//                                                Intent intent = new Intent(getApplicationContext(),
//                                                        DispatchMenuScreenActivity.class);
//                                                startActivity(intent);
//                                            } else if (short_form.equals("DEALER")) {
//                                                globalShare.setDealerMenuSelectedPos("1");
//                                                Intent intent = new Intent(getApplicationContext(),
//                                                        DealerMenuScreenActivity.class);
//                                                startActivity(intent);
//                                            } else if (short_form.equals("SA")) {
//                                                globalShare.setSuperadminmenuselectpos("1");
//                                                Intent intent = new Intent(getApplicationContext(),
//                                                        SuperAdminMenuScrenActivity.class);
//                                                startActivity(intent);
//                                            }
//                                            finish();
//                                        }else{
//                                            double lat = 0.0;
//                                            double lon = 0.0;
//                                            try {
//                                                if (!latitude.equals("") || !longitude.equals("")) {
//                                                    lat = Double.parseDouble(latitude);
//                                                    lon = Double.parseDouble(longitude);
//                                                }
//                                            } catch (Exception e) {
//                                            }
//
//                                            SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                                    "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                                    branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                                    branch_count,role_count,"");
//
//
//                                            SharedDB.multiroleSahred(getApplicationContext(), branch_id, branch_name, short_form,company,
//                                                    "","","");
//
//                                            Intent intent=new Intent(getApplicationContext(),LoginRoleBranchesActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    }
//                                }else{
//                                    globalShare.setRolesList(rolesDTOList);
//
//                                    double lat = 0.0;
//                                    double lon = 0.0;
//                                    try {
//                                        if (!latitude.equals("") || !longitude.equals("")) {
//                                            lat = Double.parseDouble(latitude);
//                                            lon = Double.parseDouble(longitude);
//                                        }
//                                    } catch (Exception e) {
//                                    }
//                                    globalShare.setLoginselectedfromval("loginscreen");
//                                    SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
//                                            "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
//                                            branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
//                                            branch_count,role_count,"0");
//
//                                    if (short_form.equals("ADMIN")) {
//                                        globalShare.setAdminmenuselectpos("1");
//                                        Intent intent = new Intent(getApplicationContext(),
//                                                AdminMenuScreenActivity.class);
//                                        startActivity(intent);
//                                    } else if (short_form.equals("FM")) {
//                                        globalShare.setFinancemenuselectpos("1");
//                                        Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
//                                        startActivity(intent);
//                                    } else if (short_form.equals("SE")) {
//
//                                        globalShare.setDealerMenuSelectedPos("1");
//                                        Intent intent = new Intent(getApplicationContext(),
//                                                DealerMenuScreenActivity.class);
//                                        startActivity(intent);
//                                    } else if (short_form.equals("DB")) {
//                                        globalShare.setDeliverymenuselectpos("1");
//                                        Intent intent = new Intent(getApplicationContext(),
//                                                DeliveryMenuScreenActivity.class);
//                                        startActivity(intent);
//                                    } else if (short_form.equals("PACKER")) {
//                                        globalShare.setDispatchmenuselectpos("1");
//                                        Intent intent = new Intent(getApplicationContext(),
//                                                DispatchMenuScreenActivity.class);
//                                        startActivity(intent);
//                                    } else if (short_form.equals("DEALER")) {
//                                        globalShare.setDealerMenuSelectedPos("1");
//                                        Intent intent = new Intent(getApplicationContext(),
//                                                DealerMenuScreenActivity.class);
//                                        startActivity(intent);
//                                    } else if (short_form.equals("SA")) {
//                                        globalShare.setSuperadminmenuselectpos("1");
//                                        Intent intent = new Intent(getApplicationContext(),
//                                                SuperAdminMenuScrenActivity.class);
//                                        startActivity(intent);
//                                    }
//                                    finish();
//                                }
//                            }
//
////                            if(short_form.equals("ADMIN")||short_form.equals("PACKER")|| short_form.equals("FM")
////                                    || short_form.equals("SE"))
////                            {
////                                if(branch_count.equals(role_count))
////                                {
////                                    if(Integer.parseInt(branch_count)>1 || Integer.parseInt(role_count)>1)
////                                    {
////                                        double lat = 0.0;
////                                        double lon = 0.0;
////                                        try {
////                                            if (!latitude.equals("") || !longitude.equals("")) {
////                                                lat = Double.parseDouble(latitude);
////                                                lon = Double.parseDouble(longitude);
////                                            }
////                                        } catch (Exception e) {
////                                        }
////
////                                        SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
////                                                "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
////                                                branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
////                                                branch_count,role_count);
////
////
////                                        SharedDB.multiroleSahred(getApplicationContext(), branch_id, branch_name, short_form,company,
////                                                "","","");
////
////                                        Intent intent=new Intent(getApplicationContext(),LoginRoleBranchesActivity.class);
////                                        startActivity(intent);
////                                        finish();
////                                    }else {
////                                        double lat = 0.0;
////                                        double lon = 0.0;
////                                        try {
////                                            if (!latitude.equals("") || !longitude.equals("")) {
////                                                lat = Double.parseDouble(latitude);
////                                                lon = Double.parseDouble(longitude);
////                                            }
////                                        } catch (Exception e) {
////                                        }
////                                        globalShare.setLoginselectedfromval("loginscreen");
////                                        SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
////                                                "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
////                                                branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
////                                                branch_count,role_count);
////
////                                        if (short_form.equals("ADMIN")) {
////                                            globalShare.setAdminmenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    AdminMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("FM")) {
////                                            globalShare.setFinancemenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("SE")) {
////
////                                            globalShare.setDealerMenuSelectedPos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    DealerMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("DB")) {
////                                            globalShare.setDeliverymenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    DeliveryMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("PACKER")) {
////                                            globalShare.setDispatchmenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    DispatchMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("DEALER")) {
////                                            globalShare.setDealerMenuSelectedPos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    DealerMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("SA")) {
////                                            globalShare.setSuperadminmenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    SuperAdminMenuScrenActivity.class);
////                                            startActivity(intent);
////                                        }
////                                        finish();
////                                    }
////                                }else{
////                                    if(Integer.parseInt(branch_count)>1 || Integer.parseInt(role_count)>1)
////                                    {
////                                        double lat = 0.0;
////                                        double lon = 0.0;
////                                        try {
////                                            if (!latitude.equals("") || !longitude.equals("")) {
////                                                lat = Double.parseDouble(latitude);
////                                                lon = Double.parseDouble(longitude);
////                                            }
////                                        } catch (Exception e) {
////                                        }
////
////                                        SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
////                                                "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
////                                                branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
////                                                branch_count,role_count);
////
////
////                                        SharedDB.multiroleSahred(getApplicationContext(), branch_id, branch_name, short_form,company,
////                                                "","","");
////
////                                        Intent intent=new Intent(getApplicationContext(),LoginRoleBranchesActivity.class);
////                                        startActivity(intent);
////                                        finish();
////                                    }else{
////                                        double lat = 0.0;
////                                        double lon = 0.0;
////                                        try {
////                                            if (!latitude.equals("") || !longitude.equals("")) {
////                                                lat = Double.parseDouble(latitude);
////                                                lon = Double.parseDouble(longitude);
////                                            }
////                                        } catch (Exception e) {
////                                        }
////                                        globalShare.setLoginselectedfromval("loginscreen");
////                                        SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
////                                                "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
////                                                branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
////                                                branch_count,role_count);
////
////                                        if (short_form.equals("ADMIN")) {
////                                            globalShare.setAdminmenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    AdminMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("FM")) {
////                                            globalShare.setFinancemenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("SE")) {
////
////                                            globalShare.setDealerMenuSelectedPos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    DealerMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("DB")) {
////                                            globalShare.setDeliverymenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    DeliveryMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("PACKER")) {
////                                            globalShare.setDispatchmenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    DispatchMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("DEALER")) {
////                                            globalShare.setDealerMenuSelectedPos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    DealerMenuScreenActivity.class);
////                                            startActivity(intent);
////                                        } else if (short_form.equals("SA")) {
////                                            globalShare.setSuperadminmenuselectpos("1");
////                                            Intent intent = new Intent(getApplicationContext(),
////                                                    SuperAdminMenuScrenActivity.class);
////                                            startActivity(intent);
////                                        }
////                                        finish();
////                                    }
////                                }
////                            }else{
////                                double lat = 0.0;
////                                double lon = 0.0;
////                                try {
////                                    if (!latitude.equals("") || !longitude.equals("")) {
////                                        lat = Double.parseDouble(latitude);
////                                        lon = Double.parseDouble(longitude);
////                                    }
////                                } catch (Exception e) {
////                                }
////                                globalShare.setLoginselectedfromval("loginscreen");
////                                SharedDB.loginSahred(getApplicationContext(), mobile, address, role, "",
////                                        "", lat, lon, primaryId, pincode, userName, imageurl, address_id, branch_id,
////                                        branch_name, short_form,company,branch_contact,String.valueOf(companieslistsize),
////                                        branch_count,role_count);
////
////                                if (short_form.equals("ADMIN")) {
////                                    globalShare.setAdminmenuselectpos("1");
////                                    Intent intent = new Intent(getApplicationContext(),
////                                            AdminMenuScreenActivity.class);
////                                    startActivity(intent);
////                                } else if (short_form.equals("FM")) {
////                                    globalShare.setFinancemenuselectpos("1");
////                                    Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
////                                    startActivity(intent);
////                                } else if (short_form.equals("SE")) {
////
////                                    globalShare.setDealerMenuSelectedPos("1");
////                                    Intent intent = new Intent(getApplicationContext(),
////                                            DealerMenuScreenActivity.class);
////                                    startActivity(intent);
////                                } else if (short_form.equals("DB")) {
////                                    globalShare.setDeliverymenuselectpos("1");
////                                    Intent intent = new Intent(getApplicationContext(),
////                                            DeliveryMenuScreenActivity.class);
////                                    startActivity(intent);
////                                } else if (short_form.equals("PACKER")) {
////                                    globalShare.setDispatchmenuselectpos("1");
////                                    Intent intent = new Intent(getApplicationContext(),
////                                            DispatchMenuScreenActivity.class);
////                                    startActivity(intent);
////                                } else if (short_form.equals("DEALER")) {
////                                    globalShare.setDealerMenuSelectedPos("1");
////                                    Intent intent = new Intent(getApplicationContext(),
////                                            DealerMenuScreenActivity.class);
////                                    startActivity(intent);
////                                } else if (short_form.equals("SA")) {
////                                    globalShare.setSuperadminmenuselectpos("1");
////                                    Intent intent = new Intent(getApplicationContext(),
////                                            SuperAdminMenuScrenActivity.class);
////                                    startActivity(intent);
////                                }
////                                finish();
////                            }
//                        }catch (Exception e)
//                        {
//                        }
//                    } else {
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showForgotPasswordPopup() {
//        /** Used for Show Disclaimer Pop up screen */
//        alertDialog = new Dialog(this);
//        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        alertDialog.getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent);
//        LayoutInflater inflater = this.getLayoutInflater();
//        layout = inflater.inflate(R.layout.forgotpasswordpopup, null);
//        alertDialog.setContentView(layout);
//        alertDialog.setCancelable(true);
//        if (!alertDialog.isShowing()) {
//            alertDialog.show();
//        }
//        submittxt = layout.findViewById(R.id.submittxt);
//        mobilenumberedittxt = layout
//                .findViewById(R.id.mobilenumberedittxt);
//
//        closeicon = layout.findViewById(R.id.closeicon);
//        closeicon.setOnClickListener(LoginActivity.this);
//        submittxt.setOnClickListener(LoginActivity.this);
//    }
//
//    private void forgotPasswordrocessWithRetrofit(String mobileno) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Login> mService = mApiService.getForgotPassword(mobileno);
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                Log.e("response", "" + response);
//                Login mLoginObject = response.body();
//                dialog.dismiss();
//                try {
//                    String status = mLoginObject.getStatus();
//                    if (status.equals("1")) {
//                        String message = mLoginObject.getMessage();
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                        if (alertDialog != null) {
//                            alertDialog.dismiss();
//                        }
//                    } else {
//                        String message = mLoginObject.getMessage();
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void hideKeyboard() {
//        // Check if no view has focus:
//        View view = getCurrentFocus();
//        if (view != null) {
//            InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        try {
//            SharedPreferences sharedpreferences = getSharedPreferences(
//                    SharedDB.LOGINPREFERENCES, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedpreferences.edit();
//            editor.clear();
//            editor.commit();
//            SharedDB.clearAuthentication(LoginActivity.this);
//        }catch (Exception e)
//        {
//        }
//    }
//
//
//}
//
