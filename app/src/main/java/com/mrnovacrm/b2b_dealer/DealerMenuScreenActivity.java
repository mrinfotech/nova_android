package com.mrnovacrm.b2b_dealer;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mrnovacrm.R;
import com.mrnovacrm.activity.ApplicationReloadActivity;
import com.mrnovacrm.activity.CountDrawable;
import com.mrnovacrm.activity.FragmentDrawer;
import com.mrnovacrm.activity.NotificationFragment;
import com.mrnovacrm.b2b_dispatch_dept.DispatchInvoiceListFragment;
import com.mrnovacrm.b2b_finance_dept.FinanceEmployeesListActivity;
import com.mrnovacrm.changepassword.ChangePasswordFragment;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.login.LoginActivity;
import com.mrnovacrm.model.ContactsModelDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Product;
import com.mrnovacrm.model.SellerDTO;
import com.mrnovacrm.model.SellersOrdersListDTO;
import com.mrnovacrm.userprofile.UserProfileActivity;
import com.mrnovacrm.utils.Config;
import com.mrnovacrm.utils.NotificationUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerMenuScreenActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    public static final int RequestPermissionCode = 1;
    public static Activity mainfinish;
    CountDrawable badge;
    private String PRIMARYID = "";
    private static DealerMenuScreenActivity mInstance = null;
    private static String itemscount = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String USERTYPE;
    private String SHORTFORM;
    private String BRANCHCONTACT = "";
    GlobalShare globalShare;
    Context mContext;
    private final int CALL_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish = this;
        mInstance = this;
        mContext = this;

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
        globalShare = (GlobalShare) getApplicationContext();
        setContentView(R.layout.activity_menu_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Nova");
        setSupportActionBar(mToolbar);
        requestPermission();
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
        String LAT = values.get(SharedDB.LATITUDE);
        String LNG = values.get(SharedDB.LONGITIDE);
        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
        }
        try {
            if (globalShare.getDealerMenuSelectedPos() != null) {
                String pos = globalShare.getDealerMenuSelectedPos();
                Log.e("pos", pos);
                displayView(Integer.parseInt(pos));
            } else {
                displayView(1);
            }
        } catch (Exception e) {
            displayView(1);
        }

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(this);
        if (isConnectedToInternet) {
            getCartCount();
            if (!SHORTFORM.equals("SE")) {
                getCompaniesList();
            }
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
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        //   mToolbar.setTitle("Deals");
        switch (position) {
            case 0:
                if (SHORTFORM.equals("SE")) {
                    Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), DealerProfileActivity.class);
                    startActivity(intent);
                }
                break;
            case 1:
                globalShare.setBtfrom("other");
                mToolbar.setTitle("Nova");
                // fragment = new ProductsListFragment();
                fragment = new DealsFragment();
                break;
            case 2:
                mToolbar.setTitle("Products");
                globalShare.setBtfrom("other");
                // fragment = new ProductCategoriesFragment();
                fragment = new ProductsHomeFragment();
                break;
            case 3:
                mToolbar.setTitle("Orders");
                fragment = new StoreOrdersTabFragment();
                break;
            case 4:
                getDiscounts();
                break;
            case 5:
                if (SHORTFORM.equals("SE")) {
                    // mToolbar.setTitle("Dealers List");
                    Intent intent = new Intent(getApplicationContext(), FinanceEmployeesListActivity.class);
                    intent.putExtra("SHORTFROM", "DEALER");
                    intent.putExtra("TITLE", "Dealers List");
                    startActivity(intent);
                } else {
                    mToolbar.setTitle("Received Orders");
              //      fragment = new ReceivedOrdersFragment();
                    fragment = new DealerReceivedOrdersTabFragment();
                }
                break;

            case 6:
                if (SHORTFORM.equals("SE")) {
                    mToolbar.setTitle("Dealer Registration");
                    fragment = new DealerRegistrationFragment();
                } else {
                    mToolbar.setTitle("Track Order");
                    fragment = new TrackOrderListFragment();
                }
                break;

            case 7:
                if (SHORTFORM.equals("SE")) {
                    mToolbar.setTitle("Invoice");
                    fragment = new DispatchInvoiceListFragment();
                } else {
                    mToolbar.setTitle("Invoice");
                    //  fragment = new InvoiceOrdersTabFragment();
                    //fragment = new TaxInvoiceFragment();
                    fragment = new DispatchInvoiceListFragment();
                }
                break;
            case 8:
                if (SHORTFORM.equals("SE")) {
                    mToolbar.setTitle("Received Orders");
                    //      fragment = new ReceivedOrdersFragment();
                    fragment = new DealerReceivedOrdersTabFragment();
                } else {
                    mToolbar.setTitle("Wallet");
                    fragment = new DealerWalletFragment();
                }
                break;
            case 9:
                if (SHORTFORM.equals("SE")) {
                    mToolbar.setTitle("Track Order");
                    fragment = new TrackOrderListFragment();
                } else {
                    mToolbar.setTitle("Notifications");
                    fragment = new NotificationFragment();
                }
                break;

            case 10:
                if (SHORTFORM.equals("SE")) {
                    mToolbar.setTitle("Wallet");
                    fragment = new SalesExecutiveWalletFragment();
                } else {
                    mToolbar.setTitle("Change Password");
                    fragment = new ChangePasswordFragment();
                }
                break;
            case 11:
                if (SHORTFORM.equals("SE")) {
                    mToolbar.setTitle("Notifications");
                    fragment = new NotificationFragment();
                } else {
                    showLogout();
                }
                break;
            case 12:
                if (SHORTFORM.equals("SE")) {
                    mToolbar.setTitle("Change Password");
                    fragment = new ChangePasswordFragment();
                }
                break;
            case 13:
                if (SHORTFORM.equals("SE")) {
                    showLogout();
                }
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

    public  void getDiscounts(){
        adad
    }

    private void getCartCount() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Product> mService = null;
        if (SHORTFORM.equals("SE")) {
            if (SharedDB.isDealerExists(getApplicationContext())) {
                HashMap<String, String> VALS = SharedDB.getDealerDetails(getApplicationContext());
                String DEALERID = VALS.get(SharedDB.DEALERID);
                mService = mApiService.getCartCount(DEALERID);
                mService.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        dialog.dismiss();
                        try {
                    //        Log.e("response123", "" + response);
                            Product mProductObject = response.body();
                            if (mProductObject != null) {
                                itemscount = mProductObject.getItems_count();
                                updateCartCountNotification(itemscount);
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        call.cancel();
                        dialog.dismiss();
                        //    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                mService = mApiService.getCartCount(PRIMARYID);
                mService.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        dialog.dismiss();
                        try {
                            Log.e("response123", "" + response);
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
        } else {
            mService = mApiService.getCartCount(PRIMARYID);
            mService.enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    dialog.dismiss();
                    try {
                        Log.e("response123", "" + response);
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

//        if(SHORTFORM.equals("SE"))
//        {
//            if(SharedDB.isDealerExists(getApplicationContext()))
//            {
//                HashMap<String, String> VALS = SharedDB.getDealerDetails(getApplicationContext());
//                String DEALERID=VALS.get(SharedDB.DEALERID);
//                mService = mApiService.getCartCount(DEALERID);
//            }
//        }else{
        //   mService = mApiService.getCartCount(PRIMARYID);
        //    }
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
                .setMessage(getResources().getString(R.string.confirmsignout))
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                refreshToken();
                                SharedPreferences sharedpreferences = getSharedPreferences(
                                        SharedDB.LOGINPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear();
                                editor.commit();
                                SharedDB.clearAuthentication(DealerMenuScreenActivity.this);

                                if (SHORTFORM.equals("SE")) {
                                    SharedPreferences dealerpreferences = getSharedPreferences(
                                            SharedDB.DEALERPREFERENCES, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor dealereditor = dealerpreferences.edit();
                                    dealereditor.clear();
                                    dealereditor.commit();
                                    SharedDB.dealerclearAuthentication(DealerMenuScreenActivity.this);
                                }
                                refreshToken();
                                GlobalShare.getInstance().clearApplicationData();

                                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                SharedPreferences.Editor shareeditor = pref.edit();
                                shareeditor.clear();
                                shareeditor.commit();

                                SharedDB.clearAuthentication(DealerMenuScreenActivity.this);
                                GlobalShare.getInstance().clearApplicationData();

                                if (LoginActivity.mainfinish != null) {
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
        DealerMenuScreenActivity instance = DealerMenuScreenActivity.getInstance();
        if (instance != null) {
            instance.getCartCount(count);
        }
    }

    private static synchronized DealerMenuScreenActivity getInstance() {
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
        //  Log.e("token Val",""+token);
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.saveFCMToken(PRIMARYID, SHORTFORM, token);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                try {
                    Login mLoginObject = response.body();
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        globalShare.setNotificationfrom("splash");
                    } else {
                        globalShare.setNotificationfrom("login");
                    }
                } catch (Exception e) {
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
        } catch (Exception e) {
        }
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
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public void callPhoneNumber() {
        if (!BRANCHCONTACT.equals("")) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(DealerMenuScreenActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                        return;
                    }
                }
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + BRANCHCONTACT));
                startActivity(callIntent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Branch contact not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCompaniesList() {
  /*      final TransparentProgressDialog dialog = new TransparentProgressDialog(this);
        dialog.show()*/
        ;
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = null;
        mService = mApiService.getDealerCompaniesList(PRIMARYID);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                //dialog.dismiss();
                try {
                    Log.e("response123", "" + response);
                    Login mProductObject = response.body();
                    if (mProductObject != null) {
                        String status = mProductObject.getStatus();
                        if (status.equals("1")) {
                            List<ContactsModelDTO> companiesList = mProductObject.getCompaniesDTOList();
                            if (companiesList != null) {
                                if (companiesList.size() > 0) {
                                    globalShare.setCompaniesList(companiesList);
                                    HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
                                    String ADDRESSID = values.get(SharedDB.ADDRESSID);
                                    USERTYPE = values.get(SharedDB.USERTYPE);
                                    SHORTFORM = values.get(SharedDB.SHORTFORM);
                                    String USERNAME = values.get(SharedDB.USERNAME);
                                    String MOBILE = values.get(SharedDB.MOBILE);
                                    String DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                                    String COMPANY = values.get(SharedDB.COMPANY);
                                    String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
                                    String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
                                    String BRANCHID = values.get(SharedDB.BRANCHID);
                                    String PINCODE = values.get(SharedDB.PINCODE);
                                    String LATITUDE = values.get(SharedDB.LATITUDE);
                                    String LONGITIDE = values.get(SharedDB.LONGITIDE);
                                    String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
                                    String IMAGEURL = values.get(SharedDB.IMAGEURL);
                                    String BRANCHCOUNT = values.get(SharedDB.BRANCHCOUNT);
                                    String ROLECOUNT = values.get(SharedDB.ROLECOUNT);
                                    String ROLEPKEY = values.get(SharedDB.ROLEPKEY);

                                    SharedDB.loginSahred(getApplicationContext(), MOBILE, DELIVERY_ADDRESS, USERTYPE, "",
                                            "", Double.parseDouble(LATITUDE), Double.parseDouble(LONGITIDE), PRIMARYID, PINCODE,
                                            USERNAME, IMAGEURL, ADDRESSID, BRANCHID,
                                            BRANCHNAME, SHORTFORM, COMPANY, BRANCHCONTACT,
                                            String.valueOf(companiesList.size()), BRANCHCONTACT, ROLECOUNT, ROLEPKEY);
                                }
                            }
                        } else {
                        }
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                //dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshToken() {
        try {
            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();
//            Intent intent = new Intent(this, MyFirebaseMessagingService.class);
//            stopService(intent);
//
//            Intent intent1 = new Intent(this, MyFirebaseInstanceIDService.class);
//            stopService(intent1);

            // Clear current saved token
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
}


//package com.nova.b2b_dealer;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.IntentService;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.LayerDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.FrameLayout;
//import android.widget.Toast;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.nova.R;
//import com.nova.activity.CountDrawable;
//import com.nova.activity.FragmentDrawer;
//import com.nova.activity.NotificationFragment;
//import com.nova.b2b_dispatch_dept.DispatchInvoiceListFragment;
//import com.nova.b2b_finance_dept.FinanceEmployeesListActivity;
//import com.nova.changepassword.ChangePasswordFragment;
//import com.nova.constants.CheckNetWork;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.login.LoginActivity;
//import com.nova.model.ContactsModelDTO;
//import com.nova.model.Login;
//import com.nova.model.Product;
//import com.nova.userprofile.UserProfileActivity;
//import com.nova.utils.Config;
//import com.nova.utils.NotificationUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class DealerMenuScreenActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
//
//    private Toolbar mToolbar;
//    private FragmentDrawer drawerFragment;
//    public static final int RequestPermissionCode = 1;
//    public static Activity mainfinish;
//    CountDrawable badge;
//    private String PRIMARYID = "";
//    private static DealerMenuScreenActivity mInstance = null;
//    private static String itemscount = "";
//    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    private String USERTYPE;
//    private String SHORTFORM;
//    private String BRANCHCONTACT="";
//    GlobalShare globalShare;
//    Context mContext;
//    private final int CALL_REQUEST = 100;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mainfinish = this;
//        mInstance = this;
//        mContext=this;
//
////        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
////            @Override
////            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
////                //Catch your exception
////                finish();
////                Intent intent = new Intent(getApplicationContext(), ApplicationReloadActivity.class);
////                startActivity(intent);
////                System.exit(0);
////            }
////        });
//
//        globalShare=(GlobalShare)getApplicationContext();
//        setContentView(R.layout.activity_menu_screen);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle("Nova");
//        setSupportActionBar(mToolbar);
//        requestPermission();
//        drawerFragment = (FragmentDrawer)
//                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
//        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
//        drawerFragment.setDrawerListener(this);
//
//        HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
//        String LAT = values.get(SharedDB.LATITUDE);
//        String LNG = values.get(SharedDB.LONGITIDE);
//        if (SharedDB.isLoggedIn(getApplicationContext())) {
//            values = SharedDB.getUserDetails(getApplicationContext());
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//            USERTYPE=values.get(SharedDB.USERTYPE);
//            SHORTFORM=values.get(SharedDB.SHORTFORM);
//            BRANCHCONTACT=values.get(SharedDB.BRANCHCONTACT);
//        }
//
//        try {
//            if (globalShare.getDealerMenuSelectedPos() != null) {
//                String pos = globalShare.getDealerMenuSelectedPos();
//                Log.e("pos",pos);
//                displayView(Integer.parseInt(pos));
//            }else{
//                displayView(1);
//            }
//        }catch (Exception e)
//        {
//            displayView(1);
//        }
//
//        boolean isConnectedToInternet = CheckNetWork
//                .isConnectedToInternet(this);
//        if(isConnectedToInternet)
//        {
//            getCartCount();
//            if(!SHORTFORM.equals("SE")) {
//                getCompaniesList();
//            }
//        }
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // checking for type intent filter
//                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
//                    displayFirebaseRegId();
//                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//                    String message = intent.getStringExtra("message");
////                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//                    //                  txtMessage.setText(message);
//                }
//            }
//        };
//        displayFirebaseRegId();
//    }
//    // Fetches reg id from shared preferences
//    // and displays on the screen
//    private void displayFirebaseRegId() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//        Log.e("regID",regId);
//        if(!TextUtils.isEmpty(regId))
//        {
//            if(globalShare.getNotificationfrom()!=null)
//            {
//                if(globalShare.getNotificationfrom().equals("login"))
//                {
//                    fcmtokenRegisterWithRetrofit(regId);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onDrawerItemSelected(View view, int position) {
//        displayView(position);
//    }
//
//    private void displayView(int position) {
//        Fragment fragment = null;
//     //   mToolbar.setTitle("Deals");
//        switch (position) {
//            case 0:
//                if(SHORTFORM.equals("SE"))
//                {
//                    Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
//                    startActivity(intent);
//                }else{
//                    Intent intent = new Intent(getApplicationContext(), DealerProfileActivity.class);
//                    startActivity(intent);
//                }
//                break;
//            case 1:
//                globalShare.setBtfrom("other");
//                mToolbar.setTitle("Nova");
//                // fragment = new ProductsListFragment();
//                fragment = new DealsFragment();
//                break;
//            case 2:
//                mToolbar.setTitle("Products");
//                globalShare.setBtfrom("other");
//                // fragment = new ProductCategoriesFragment();
//                fragment = new ProductsHomeFragment();
//                break;
//            case 3:
//                mToolbar.setTitle("Orders");
//                fragment = new StoreOrdersTabFragment();
//
//                break;
//            case 4:
//                if(SHORTFORM.equals("SE"))
//                {
//                   // mToolbar.setTitle("Dealers List");
//                    Intent intent = new Intent(getApplicationContext(), FinanceEmployeesListActivity.class);
//                    intent.putExtra("SHORTFROM","DEALER");
//                    intent.putExtra("TITLE","Dealers List");
//                    startActivity(intent);
//                }else {
//                    mToolbar.setTitle("Received Orders");
//                    fragment = new ReceivedOrdersFragment();
//                }
//                break;
//            case 5:
//                if(SHORTFORM.equals("SE"))
//                {
//                    mToolbar.setTitle("Dealer Registration");
//                    fragment = new DealerRegistrationFragment();
//                }else {
//                    mToolbar.setTitle("Track Order");
//                    fragment = new TrackOrderListFragment();
//                }
//                break;
//            case 6:
//                if(SHORTFORM.equals("SE"))
//                {
//                    mToolbar.setTitle("Invoice");
//                    fragment = new DispatchInvoiceListFragment();
//                }else {
//                    mToolbar.setTitle("Invoice");
//                  //  fragment = new InvoiceOrdersTabFragment();
//                    //fragment = new TaxInvoiceFragment();
//                    fragment = new DispatchInvoiceListFragment();
//                }
//                break;
//
//            case 7:
//                if(SHORTFORM.equals("SE"))
//                {
//                    mToolbar.setTitle("Track Order");
//                    fragment = new TrackOrderListFragment();
//                }else {
//                    mToolbar.setTitle("Wallet");
//                    fragment = new DealerWalletFragment();
//                }
//                break;
//            case 8:
//                if(SHORTFORM.equals("SE")) {
//                    mToolbar.setTitle("Wallet");
//                    fragment = new SalesExecutiveWalletFragment();
//                }else{
//                    mToolbar.setTitle("Notifications");
//                    fragment = new NotificationFragment();
//                }
//                break;
//            case 9:
//                if(SHORTFORM.equals("SE")) {
//                    mToolbar.setTitle("Notifications");
//                    fragment = new NotificationFragment();
//
//                }else{
//                    mToolbar.setTitle("Change Password");
//                    fragment = new ChangePasswordFragment();
//                }
//                break;
//             case 10:
//                 if(SHORTFORM.equals("SE")) {
//                     mToolbar.setTitle("Change Password");
//                     fragment = new ChangePasswordFragment();
//
//                 }else {
//                     showLogout();
//                 }
//                break;
//            case 11:
//                if(SHORTFORM.equals("SE")) {
//                    showLogout();
//                }
//                break;
//            default:
//                break;
//        }
//        if (fragment != null) {
//            FrameLayout frameLayout = findViewById(R.id.container_body);
////            frameLayout.removeAllViews();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container_body, fragment);
//            fragmentTransaction.commit();
//        }
//    }
//
//    private void getCartCount() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Product> mService=null;
//        if(SHORTFORM.equals("SE"))
//        {
//            if(SharedDB.isDealerExists(getApplicationContext()))
//            {
//                HashMap<String, String> VALS = SharedDB.getDealerDetails(getApplicationContext());
//                String DEALERID=VALS.get(SharedDB.DEALERID);
//                mService = mApiService.getCartCount(DEALERID);
//                mService.enqueue(new Callback<Product>() {
//                    @Override
//                    public void onResponse(Call<Product> call, Response<Product> response) {
//                        dialog.dismiss();
//                        try {
//                            Log.e("response123", "" + response);
//                            Product mProductObject = response.body();
//                            if (mProductObject != null) {
//                                itemscount = mProductObject.getItems_count();
//                                updateCartCountNotification(itemscount);
//                            }
//                        } catch (Exception e) {
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<Product> call, Throwable t) {
//                        call.cancel();
//                        dialog.dismiss();
//                    //    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }else{
//                mService = mApiService.getCartCount(PRIMARYID);
//                mService.enqueue(new Callback<Product>() {
//                    @Override
//                    public void onResponse(Call<Product> call, Response<Product> response) {
//                        dialog.dismiss();
//                        try {
//                            Log.e("response123", "" + response);
//                            Product mProductObject = response.body();
//                            if (mProductObject != null) {
//                                itemscount = mProductObject.getItems_count();
//                                Log.e("MainCartCount", "" + itemscount);
//                                updateCartCountNotification(itemscount);
//                            }
//                        } catch (Exception e) {
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<Product> call, Throwable t) {
//                        call.cancel();
//                        dialog.dismiss();
//                        Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }else{
//            mService = mApiService.getCartCount(PRIMARYID);
//            mService.enqueue(new Callback<Product>() {
//                @Override
//                public void onResponse(Call<Product> call, Response<Product> response) {
//                    dialog.dismiss();
//                    try {
//                        Log.e("response123", "" + response);
//                        Product mProductObject = response.body();
//                        if (mProductObject != null) {
//                            itemscount = mProductObject.getItems_count();
//                            Log.e("MainCartCount", "" + itemscount);
//                            updateCartCountNotification(itemscount);
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Product> call, Throwable t) {
//                    call.cancel();
//                    dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//
//
////        if(SHORTFORM.equals("SE"))
////        {
////            if(SharedDB.isDealerExists(getApplicationContext()))
////            {
////                HashMap<String, String> VALS = SharedDB.getDealerDetails(getApplicationContext());
////                String DEALERID=VALS.get(SharedDB.DEALERID);
////                mService = mApiService.getCartCount(DEALERID);
////            }
////        }else{
//         //   mService = mApiService.getCartCount(PRIMARYID);
//    //    }
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//
//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{
//                            Manifest.permission.CAMERA,
//                            Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.CALL_PHONE},
//                    RequestPermissionCode);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case RequestPermissionCode:
//                if (grantResults.length > 0) {
//                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean READ_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean WRITE_EXTERNAL_STORAGE = grantResults[2] == PackageManager.PERMISSION_GRANTED;
//                    boolean PhonePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
//
//                    if (CameraPermission && READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
//                    } else {
//                    }
//                }
//                break;
//        }
//    }
//
//    public void showLogout() {
//        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(
//                this);
//        // set title
//        alertDialogBuilder.setTitle(getResources().getString(R.string.app_name));
//        // set dialog message
//        alertDialogBuilder
//                .setMessage(getResources().getString(R.string.confirmsignout))
//                .setCancelable(false)
//                .setPositiveButton("Yes",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
//                                SharedPreferences sharedpreferences = getSharedPreferences(
//                                        SharedDB.LOGINPREFERENCES, Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sharedpreferences.edit();
//                                editor.clear();
//                                editor.commit();
//                                SharedDB.clearAuthentication(DealerMenuScreenActivity.this);
//
//                                if(SHORTFORM.equals("SE")) {
//                                    SharedPreferences dealerpreferences = getSharedPreferences(
//                                            SharedDB.DEALERPREFERENCES, Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor dealereditor = dealerpreferences.edit();
//                                    dealereditor.clear();
//                                    dealereditor.commit();
//                                    SharedDB.dealerclearAuthentication(DealerMenuScreenActivity.this);
//                                }
//
//                                GlobalShare.getInstance().clearApplicationData();
//                                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//                                SharedPreferences.Editor shareeditor = pref.edit();
//                                shareeditor.clear();
//                                shareeditor.commit();
//
//                                SharedDB.clearAuthentication(DealerMenuScreenActivity.this);
//                                GlobalShare.getInstance().clearApplicationData();
//
//                                if(LoginActivity.mainfinish!=null)
//                                {
//                                    LoginActivity.mainfinish.finish();
//                                }
//
//
//
//                              //  MyFirebaseInstanceIDService.
//                             //  FirebaseInstanceId.getInstance().getToken();
//
//                                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem menuItem = menu.findItem(R.id.cart);
//        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
//
//        // Reuse drawable if possible
//        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
//        if (reuse != null && reuse instanceof CountDrawable) {
//            badge = (CountDrawable) reuse;
//        } else {
//            badge = new CountDrawable(getApplicationContext());
//        }
//        badge.setCount(String.valueOf(itemscount));
//        icon.mutate();
//        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
//        return true;
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.cart:
//                Intent intent = new Intent(getApplicationContext(), CartListActivity.class);
//                startActivity(intent);
//                return true;
////            case R.id.barcode:
////                Intent barcode_intent = new Intent(getApplicationContext(), BarCodeScannerActivity.class);
////                startActivity(barcode_intent);
////                return true;
//            case R.id.phone:
//                callPhoneNumber();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    public static void updateCartCountNotification(String count) {
//        itemscount = count;
//        DealerMenuScreenActivity instance = DealerMenuScreenActivity.getInstance();
//        if (instance != null) {
//            instance.getCartCount(count);
//        }
//    }
//
//    private static synchronized DealerMenuScreenActivity getInstance() {
//        return mInstance;
//    }
//
//    public void getCartCount(final String count) {
//        badge.setCount(String.valueOf(count));
//    }
//
//    protected OnBackPressedListener onBackPressedListener;
//
//    public interface OnBackPressedListener {
//        void doBack();
//    }
//
//    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
//        this.onBackPressedListener = onBackPressedListener;
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (onBackPressedListener != null) {
//            onBackPressedListener.doBack();
//        } else {
//            if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
//                super.onBackPressed();
//            } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//                FragmentDrawer.closeDrawer();
//                android.support.v7.app.AlertDialog alertbox = new android.support.v7.app.AlertDialog.Builder(this)
//                        .setMessage("Do you want to exit application?")
//                        .setPositiveButton("Yes",
//                                new DialogInterface.OnClickListener() {
//                                    // do something when the button is clicked
//                                    public void onClick(DialogInterface arg0, int arg1) {
//                                        finish();
//                                    }
//                                })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//
//                            // do something when the button is clicked
//                            public void onClick(DialogInterface arg0, int arg1) {
//                            }
//                        }).show();
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.REGISTRATION_COMPLETE));
//
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.PUSH_NOTIFICATION));
//        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }
//
//    private void fcmtokenRegisterWithRetrofit(String token) {
//      //  Log.e("token Val",""+token);
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Login> mService = mApiService.saveFCMToken(PRIMARYID,SHORTFORM,token);
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                try{
//                    Login mLoginObject = response.body();
//                    String status = mLoginObject.getStatus();
//                    if (status.equals("1")) {
//                        globalShare.setNotificationfrom("splash");
//                    }else{
//                        globalShare.setNotificationfrom("login");
//                    }
//                }catch (Exception e)
//                {
//                }
//            }
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                globalShare.setNotificationfrom("login");
//            }
//        });
//    }
//
//    public static void deleteCache(Context context) {
//        try {
//            File dir = context.getCacheDir();
//            deleteDir(dir);
//        } catch (Exception e) {}
//    }
//
//    public static boolean deleteDir(File dir) {
//        if (dir != null && dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//            return dir.delete();
//        } else if(dir!= null && dir.isFile()) {
//            return dir.delete();
//        } else {
//            return false;
//        }
//    }
//
//    public void callPhoneNumber()
//    {
//        if(!BRANCHCONTACT.equals("")) {
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        ActivityCompat.requestPermissions(DealerMenuScreenActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
//                        return;
//                    }
//                }
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:" + BRANCHCONTACT));
//                startActivity(callIntent);
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }else{
//            Toast.makeText(getApplicationContext(),"Branch contact not available",Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void getCompaniesList() {
//  /*      final TransparentProgressDialog dialog = new TransparentProgressDialog(this);
//        dialog.show()*/;
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Login> mService=null;
//            mService = mApiService.getDealerCompaniesList(PRIMARYID);
//            mService.enqueue(new Callback<Login>() {
//                @Override
//                public void onResponse(Call<Login> call, Response<Login> response) {
//                    //dialog.dismiss();
//                    try {
//                        Log.e("response123", "" + response);
//                        Login mProductObject = response.body();
//                        if (mProductObject != null) {
//                            String status=mProductObject.getStatus();
//                            if(status.equals("1"))
//                            {
//                                List<ContactsModelDTO> companiesList = mProductObject.getCompaniesDTOList();
//                                if(companiesList!=null)
//                                {
//                                    if(companiesList.size()>0)
//                                    {
//                                        globalShare.setCompaniesList(companiesList);
//                                        HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
//                                        String ADDRESSID = values.get(SharedDB.ADDRESSID);
//                                        USERTYPE = values.get(SharedDB.USERTYPE);
//                                        SHORTFORM = values.get(SharedDB.SHORTFORM);
//                                        String USERNAME = values.get(SharedDB.USERNAME);
//                                        String MOBILE = values.get(SharedDB.MOBILE);
//                                        String DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
//                                        String COMPANY = values.get(SharedDB.COMPANY);
//                                        String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
//                                        String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
//                                        String BRANCHID = values.get(SharedDB.BRANCHID);
//                                        String PINCODE = values.get(SharedDB.PINCODE);
//                                        String LATITUDE = values.get(SharedDB.LATITUDE);
//                                        String LONGITIDE = values.get(SharedDB.LONGITIDE);
//                                        String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
//                                        String IMAGEURL = values.get(SharedDB.IMAGEURL);
//                                        String BRANCHCOUNT = values.get(SharedDB.BRANCHCOUNT);
//                                        String ROLECOUNT = values.get(SharedDB.ROLECOUNT);
//                                        String ROLEPKEY = values.get(SharedDB.ROLEPKEY);
//
//                                        SharedDB.loginSahred(getApplicationContext(), MOBILE, DELIVERY_ADDRESS, USERTYPE, "",
//                                                "", Double.parseDouble(LATITUDE), Double.parseDouble(LONGITIDE), PRIMARYID, PINCODE,
//                                                USERNAME, IMAGEURL, ADDRESSID, BRANCHID,
//                                                BRANCHNAME, SHORTFORM, COMPANY, BRANCHCONTACT,
//                                                String.valueOf(companiesList.size()),BRANCHCONTACT,ROLECOUNT,ROLEPKEY);
//                                    }
//                                }
//                            }else{
//                            }
//                        }
//                    } catch (Exception e) {
//                    }
//                }
//                @Override
//                public void onFailure(Call<Login> call, Throwable t) {
//                    call.cancel();
//                    //dialog.dismiss();
//                    Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//    public class DeleteTokenService extends IntentService
//    {
//       // String TAG = DeleteTokenService.class.getSimpleName();
//
//        public DeleteTokenService()
//        {
//
//            super(DeleteTokenService.class.getSimpleName());
//            Log.e("called","yes");
//        }
//
//        @Override
//        protected void onHandleIntent(Intent intent)
//        {
//            try
//            {
//                // Check for current token
//                String originalToken = getTokenFromPrefs();
//                Log.e("onHandleIntent called","yes");
//
//                Log.e("originalToken",originalToken);
//                Log.d(DeleteTokenService.class.getSimpleName(), "Token before deletion: " + originalToken);
//
//                // Resets Instance ID and revokes all tokens.
//                FirebaseInstanceId.getInstance().deleteInstanceId();
//                // Clear current saved token
//                saveTokenToPrefs("");
//
//                // Check for success of empty token
//                String tokenCheck = getTokenFromPrefs();
//                Log.d(DeleteTokenService.class.getSimpleName(), "Token deleted. Proof: " + tokenCheck);
//
//                // Now manually call onTokenRefresh()
//                Log.d(DeleteTokenService.class.getSimpleName(), "Getting new token");
//                FirebaseInstanceId.getInstance().getToken();
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
//
//        private void saveTokenToPrefs(String _token)
//        {
//            // Access Shared Preferences
//            SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//         //   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DealerMenuScreenActivity.this);
//            SharedPreferences.Editor editor = preferences.edit();
//
//            Log.e("_token",_token);
//
//            // Save to SharedPreferences
//            editor.putString("regId", _token);
//            editor.apply();
//        }
//
//        private String getTokenFromPrefs()
//        {
//            SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//         //   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DealerMenuScreenActivity.this);
//            return preferences.getString("regId", null);
//        }
//    }
//
//    private void saveTokenToPrefs(String _token)
//    {
//        // Access Shared Preferences
//        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        SharedPreferences.Editor editor = preferences.edit();
//    // Save to SharedPreferences
//        editor.putString("regId", _token);
//        editor.apply();
//    }
//
//    public void refreshToken()
//    {
//        try{
//            // Resets Instance ID and revokes all tokens.
//            FirebaseInstanceId.getInstance().deleteInstanceId();
//            // Clear current saved token
//            saveTokenToPrefs("");
//        }catch (Exception e)
//        {
//
//        }
//
//
////        try {
////            // Resets Instance ID and revokes all tokens.
////            Log.e("OLD FCM ID",FirebaseInstanceId.getInstance().getToken());
////            FirebaseInstanceId.getInstance().deleteInstanceId();
////
////            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
////            Log.e("refreshed Token ID",FirebaseInstanceId.getInstance().getToken());
////        }catch (Exception e)
////        {
////
////        }
//
////        new AsyncTask<Void,Void,Void>() {
////            @Override
////            protected Void doInBackground(Void... params) {
////                try
////                {
////                    FirebaseInstanceId.getInstance().deleteInstanceId();
////                } catch (IOException e)
////                {
////                    e.printStackTrace();
////                }
////                return null;
////            }
////
////            @Override
////            protected void onPostExecute(Void result) {
////                // Call your Activity where you want to land after log out
////            }
////        }.execute();
//    }
//}