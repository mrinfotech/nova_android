package com.mrnovacrm.b2b_sales_dept;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.ApplicationReloadActivity;
import com.mrnovacrm.activity.CountDrawable;
import com.mrnovacrm.activity.FragmentDrawer;
import com.mrnovacrm.b2b_dealer.CartListActivity;
import com.mrnovacrm.b2b_dealer.ProductsHomeFragment;
import com.mrnovacrm.b2b_dealer.ProductsListFragment;
import com.mrnovacrm.b2b_dealer.StoreOrdersTabFragment;
import com.mrnovacrm.b2b_finance_dept.ManageDealersFragment;
import com.mrnovacrm.changepassword.ChangePasswordFragment;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.login.LoginActivity;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Product;
import com.mrnovacrm.userprofile.UserProfileActivity;
import com.mrnovacrm.utils.Config;
import com.mrnovacrm.utils.NotificationUtils;

import java.io.File;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesMenuScreenActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    public static final int RequestPermissionCode = 1;
    public static Activity mainfinish;
    CountDrawable badge;
    private String PRIMARYID = "";
    private static SalesMenuScreenActivity mInstance = null;
    private static String itemscount = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String USERTYPE;
    GlobalShare globalShare;
    Context mContext;
    private final int CALL_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish = this;
        mInstance = this;
        mContext=this;

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

        setContentView(R.layout.activity_menu_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Deals");
        setSupportActionBar(mToolbar);
        requestPermission();
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        try {
            if (globalShare.getSalesmenuselectpos() != null) {
                String pos = globalShare.getSalesmenuselectpos();
                displayView(Integer.parseInt(pos));
            }
        }catch (Exception e)
        {
            displayView(1);
        }

        HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
        String LAT = values.get(SharedDB.LATITUDE);
        String LNG = values.get(SharedDB.LONGITIDE);
        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE=values.get(SharedDB.USERTYPE);
        }
        getCartCount();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        if(!TextUtils.isEmpty(regId))
        {
            if(globalShare.getNotificationfrom()!=null)
            {
                if(globalShare.getNotificationfrom().equals("login"))
                {
                    fcmtokenRegisterWithRetrofit(regId);
                }
            }
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        mToolbar.setTitle("Deals");
        switch (position) {
            case 0:
                Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(intent);
                break;
            case 1:
                globalShare.setBtfrom("other");
                mToolbar.setTitle("Deals");
                fragment = new ProductsListFragment();
                break;
            case 2:
                mToolbar.setTitle("Products");
                globalShare.setBtfrom("other");
                fragment=new ProductsHomeFragment();
             //   fragment = new ProductCategoriesFragment();
                break;
            case 3:
                mToolbar.setTitle("Orders");
                fragment = new StoreOrdersTabFragment();
                break;
            case 4:
                mToolbar.setTitle("Dealer Registration");
                fragment = new ManageDealersFragment();
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
            FrameLayout frameLayout = findViewById(R.id.container_body);
//            frameLayout.removeAllViews();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
        }
    }

    private void getCartCount() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Product> mService = mApiService.getCartCount(PRIMARYID);
        mService.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                dialog.dismiss();
                Log.e("response123", "" + response);
                try {
                    Product mProductObject = response.body();
                    if (mProductObject != null) {
                        itemscount = mProductObject.getItems_count();
                        Log.e("MainCartCount", "" + itemscount);
                        updateCartCountNotification(itemscount);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
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

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CALL_PHONE},
                    RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean READ_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean PhonePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                    } else {
                    }
                }
                break;
        }
    }

    public void showLogout() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
                this);
        // set title
        alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
        // set dialog message
        alertDialogBuilder
                .setMessage("Confirm Logout?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                SharedPreferences sharedpreferences = getSharedPreferences(
                                        SharedDB.LOGINPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear();
                                editor.commit();
                                SharedDB.clearAuthentication(SalesMenuScreenActivity.this);

                                if(LoginActivity.mainfinish!=null)
                                {
                                    LoginActivity.mainfinish.finish();
                                }
                                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(getApplicationContext());
        }
        badge.setCount(String.valueOf(itemscount));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                Intent intent = new Intent(getApplicationContext(), CartListActivity.class);
                startActivity(intent);
                return true;
//            case R.id.barcode:
//                Intent barcode_intent = new Intent(getApplicationContext(), BarCodeScannerActivity.class);
//                startActivity(barcode_intent);
//                return true;
            case R.id.phone:
                callPhoneNumber();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void updateCartCountNotification(String count) {
        itemscount = count;
        SalesMenuScreenActivity instance = SalesMenuScreenActivity.getInstance();
        if (instance != null) {
            instance.getCartCount(count);
        }
    }

    private static synchronized SalesMenuScreenActivity getInstance() {
        return mInstance;
    }

    public void getCartCount(final String count) {
        badge.setCount(String.valueOf(count));
    }

    protected OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
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

    @Override
    protected void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void fcmtokenRegisterWithRetrofit(String token) {
        Log.e("token Val",""+token);
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.saveFCMToken(PRIMARYID,"store",token);
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

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    ActivityCompat.requestPermissions(SalesMenuScreenActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                    return;
                }
            }
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" +"9959010142"));
            startActivity(callIntent);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}