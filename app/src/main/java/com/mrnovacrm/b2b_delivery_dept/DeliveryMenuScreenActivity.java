package com.mrnovacrm.b2b_delivery_dept;

//public class DeliveryMenuScreenActivity {
//}

import android.app.Activity;
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
import android.view.View;
import android.view.WindowManager;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.ApplicationReloadActivity;
import com.mrnovacrm.activity.FragmentDrawer;
import com.mrnovacrm.b2b_dealer.DealerMenuScreenActivity;
import com.mrnovacrm.changepassword.ChangePasswordFragment;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.login.LoginActivity;
import com.mrnovacrm.userprofile.UserProfileActivity;
/**
 * Created by android on 14-03-2018.
 */

public class DeliveryMenuScreenActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    public static Activity mainfinish;
    GlobalShare globalShare;

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

        globalShare=(GlobalShare)getApplicationContext();
        mainfinish = this;
        setContentView(R.layout.activity_menu_screen);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Pickup Orders");
        setSupportActionBar(mToolbar);
        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        if(globalShare.getDeliverymenuselectpos()!=null)
        {
            String pos=globalShare.getDeliverymenuselectpos();
            int posval=Integer.parseInt(pos);
            displayView(posval);
        }else{
            displayView(1);
        }
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
                mToolbar.setTitle("Pickup Orders");

                fragment = new DeliveryOrderListFragment();
               // fragment = new DeliveryOrdersListFragment();
                break;
            case 2:
                mToolbar.setTitle("Pickup History");
                fragment=new PickupHistoryTabFragment();
                break;
            case 3:
                mToolbar.setTitle("Delivery Orders");
                fragment = new DeliveryProcessingFragment();
              //  fragment = new DeliveryOrdersListFragment();
                break;
            case 4:
                mToolbar.setTitle("Delivery History");
                fragment = new DeliverOrdersTabFragment();
                break;
            case 5:
                mToolbar.setTitle("Change Password");
                fragment = new ChangePasswordFragment();
                break;
            case 6:
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
                                SharedDB.clearAuthentication(DeliveryMenuScreenActivity.this);

                                if(LoginActivity.mainfinish!=null)
                                {
                                    LoginActivity.mainfinish.finish();
                                }
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
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

    protected DealerMenuScreenActivity.OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(DealerMenuScreenActivity.OnBackPressedListener onBackPressedListener) {
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