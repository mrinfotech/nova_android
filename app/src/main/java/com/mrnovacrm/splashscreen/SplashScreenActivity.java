package com.mrnovacrm.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
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
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.login.LoginActivity;
import com.mrnovacrm.login.LoginRoleBranchesActivity;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {
    private boolean isConnectedToInternet;
    int DELAY = 3000;
    private GlobalShare globalShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        globalShare=(GlobalShare)getApplicationContext();
        isConnectedToInternet = CheckNetWork.isConnectedToInternet(SplashScreenActivity.this);

        if (isConnectedToInternet) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (SharedDB.isLoggedIn(getApplicationContext())) {
                        HashMap<String, String> values = new HashMap<String, String>();
                        values = SharedDB.getUserDetails(getApplicationContext());
                        String userType = values.get(SharedDB.SHORTFORM);

                        Log.e("userType",userType);

                        if(SharedDB.isMultiroleExists(getApplicationContext()))
                        {
                            Intent intent = new Intent(SplashScreenActivity.this,
                                    LoginRoleBranchesActivity.class);
                            startActivity(intent);
                        }else{
                            globalShare.setLoginselectedfromval("splash");

                            if(userType.equals("DEALER")) {
                                globalShare.setDealerMenuSelectedPos("1");
                                Intent intent = new Intent(SplashScreenActivity.this,
                                        DealerScreenActivity.class);
                                startActivity(intent);
                            }else  if(userType.equals("FM")){
                                globalShare.setFinancemenuselectpos("1");
                                Intent intent = new Intent(SplashScreenActivity.this,
                                        FinanceDeptMenuScreenActivity.class);
                                startActivity(intent);
                            }else  if(userType.equals("PACKER")){
                                globalShare.setDispatchmenuselectpos("1");
                                Intent intent = new Intent(SplashScreenActivity.this,
                                        DispatchMenuScreenActivity.class);
                                startActivity(intent);
                            }else  if(userType.equals("DB")){
                                globalShare.setDeliverymenuselectpos("1");
                                Intent intent = new Intent(SplashScreenActivity.this,
                                        DeliveryMenuScreenActivity.class);
                                startActivity(intent);
                            }else  if(userType.equals("SE")){
                                globalShare.setDealerMenuSelectedPos("1");
                                Intent intent = new Intent(SplashScreenActivity.this,
                                        DealerMenuScreenActivity.class);
                                startActivity(intent);
                            }else  if(userType.equals("ADMIN")){
                                globalShare.setAdminmenuselectpos("1");
                                Intent intent = new Intent(SplashScreenActivity.this,
                                        AdminMenuScreenActivity.class);
                                startActivity(intent);
                            }else  if(userType.equals("SA")){
                                globalShare.setSuperadminmenuselectpos("1");
                                Intent intent = new Intent(SplashScreenActivity.this,
                                        SuperAdminMenuScrenActivity.class);
                                startActivity(intent);
                            }
                        }
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }, DELAY);
        } else {
            Toast.makeText(getApplicationContext(), R.string.networkerror, Toast.LENGTH_SHORT).show();
        }
    }
}