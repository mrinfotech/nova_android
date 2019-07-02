package com.mrnovacrm.b2b_superadmin;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mrnovacrm.R;
import com.mrnovacrm.activity.ApplicationReloadActivity;
import com.mrnovacrm.activity.FragmentDrawer;
import com.mrnovacrm.b2b_dealer.DiscountsRequestFragment;
import com.mrnovacrm.b2b_dispatch_dept.DispatchInvoiceListFragment;
import com.mrnovacrm.b2b_finance_dept.ManageEmployeeDeatailsFragment;
import com.mrnovacrm.changepassword.ChangePasswordFragment;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.login.LoginActivity;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.userprofile.UserProfileActivity;
import com.mrnovacrm.utils.Config;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by android on 14-03-2018.
 */
public class SuperAdminMenuScrenActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    private Toolbar mToolbar;
    public static Activity mainfinish;
    GlobalShare globalShare;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String PRIMARYID = "";
    private String USERTYPE;
    private String BRANCHID;
    private String SHORTFORM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                //Catch your exception
                finish();
                Intent intent = new Intent(getApplicationContext(), ApplicationReloadActivity.class);
                startActivity(intent);
                System.exit(0);
            }
        });

        HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            BRANCHID = values.get(SharedDB.BRANCHID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }
        globalShare=(GlobalShare)getApplicationContext();
        mainfinish = this;
        setContentView(R.layout.activity_menu_screen);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Manage Branches");
        setSupportActionBar(mToolbar);
        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        if(globalShare.getSuperadminmenuselectpos()!=null)
        {
            String pos=globalShare.getSuperadminmenuselectpos();
            int posval=Integer.parseInt(pos);
            displayView(posval);
        }else{
            displayView(1);
        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    //                  txtMessage.setText(message);
                }
            }
        };
        displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        if(regId!=null)
        {
            if (!TextUtils.isEmpty(regId)) {
                if (globalShare.getNotificationfrom() != null) {
                    if (globalShare.getNotificationfrom().equals("login") || globalShare.getNotificationfrom().equals("splash") ) {
                        fcmtokenRegisterWithRetrofit(regId);
                    }
                }
            }
        }else{
            FirebaseInstanceId.getInstance().getToken();
        }
    }

    private void fcmtokenRegisterWithRetrofit(String token) {
        //  Log.e("token Val",""+token);
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.saveFCMToken(PRIMARYID,SHORTFORM,token);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                try{
                    Login mLoginObject = response.body();
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        globalShare.setNotificationfrom("splash");
                    }else{
                        globalShare.setNotificationfrom("login");
                    }
                }catch (Exception e)
                {
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                globalShare.setNotificationfrom("login");
            }
        });
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        mToolbar.setTitle("Orders List");
        switch (position) {
            case 0:
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
                break;

            case 1:
                mToolbar.setTitle("Dashboard");
                fragment=new SuperAdminDashBoardFragment();
                break;

            case 2:
                mToolbar.setTitle("Manage Branches");
                fragment=new ManageBranchesFragment();
                break;

            case 3:
                mToolbar.setTitle("Manage Finance Admin");
                //fragment=new ManageAdminFragment();
                fragment = new ManageEmployeeDeatailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("fromval", "ADMIN");
                bundle.putString("title", "Manage Admin");
                fragment.setArguments(bundle);
                break;

            case 4:
                mToolbar.setTitle("Discounts Request");
                fragment = new DiscountsRequestFragment();
                break;

            case 5:
                mToolbar.setTitle("Invoice");
                fragment = new DispatchInvoiceListFragment();
                break;
/*            case 5:
                mToolbar.setTitle("Notifications");
                fragment = new NotificationFragment();
                break;*/

            case 6:
                mToolbar.setTitle("Change Password");
                fragment = new ChangePasswordFragment();
                break;
            case 7:
                showLogout();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void showLogout() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                this);
        // set title
        alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
        // set dialog message
        alertDialogBuilder
                .setMessage(getResources().getString(R.string.confirmsignout))
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                SharedPreferences sharedpreferences = getSharedPreferences(
                                        SharedDB.LOGINPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear();
                                editor.apply();
                                SharedDB.clearAuthentication(SuperAdminMenuScrenActivity.this);

                                if(LoginActivity.mainfinish!=null)
                                {
                                    LoginActivity.mainfinish.finish();
                                }

                                refreshToken();
                                GlobalShare.getInstance().clearApplicationData();
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                SharedPreferences.Editor shareeditor = pref.edit();
                                shareeditor.clear();
                                shareeditor.commit();

                                SharedDB.clearAuthentication(SuperAdminMenuScrenActivity.this);
                                GlobalShare.getInstance().clearApplicationData();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();

//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                startActivity(intent);
//                                finish();
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void refreshToken() {
        try {
            FirebaseInstanceId.getInstance().deleteInstanceId();
            saveTokenToPrefs("");
            globalShare.setNotificationfrom("login");
        } catch (Exception e) {

        }
    }
    private void saveTokenToPrefs(String _token) {
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//     //   Log.e("refreshedToken", refreshedToken);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = preferences.edit();
//       // Log.e("_token", _token);
//      //  Log.e("_token", refreshedToken);
//        // Save to SharedPreferences
        //    editor.putString("regId", refreshedToken);
        editor.putString("regId", _token);
        editor.apply();
    }

    protected SuperAdminMenuScrenActivity.OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(SuperAdminMenuScrenActivity.OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                super.onBackPressed();
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                FragmentDrawer.closeDrawer();
                android.support.v7.app.AlertDialog alertbox = new android.support.v7.app.AlertDialog.Builder(this)
                        .setMessage("Do you want to exit application?")
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    // do something when the button is clicked
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        finish();
                                    }
                                })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {

                            // do something when the button is clicked
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        }).show();
            }
        }
    }
}