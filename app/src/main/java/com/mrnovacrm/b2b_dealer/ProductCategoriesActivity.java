package com.mrnovacrm.b2b_dealer;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.mrnovacrm.R;
import com.mrnovacrm.activity.CountDrawable;
import com.mrnovacrm.activity.FragmentDrawer;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Product;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoriesActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    public static final int RequestPermissionCode = 1;
    public static Activity mainfinish;
    CountDrawable badge;
    private String PRIMARYID = "";
    private static ProductCategoriesActivity mInstance = null;
    private static String itemscount = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String USERTYPE;
    private String SHORTFORM;
    private String BRANCHCONTACT="";
    GlobalShare globalShare;
    Context mContext;
    private final int CALL_REQUEST = 100;
    private String IDVAL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mainfinish = this;
        mInstance = this;
        mContext=this;
        globalShare=(GlobalShare)getApplicationContext();

        setTheme(R.style.AppTheme);

        Bundle bundle=getIntent().getExtras();
        String title=bundle.getString("title");
        IDVAL=bundle.getString("id");
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.layout_productioncategory);

        displayView();
        HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
        String LAT = values.get(SharedDB.LATITUDE);
        String LNG = values.get(SharedDB.LONGITIDE);
        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE=values.get(SharedDB.USERTYPE);
            SHORTFORM=values.get(SharedDB.SHORTFORM);
            BRANCHCONTACT=values.get(SharedDB.BRANCHCONTACT);
        }
        requestPermission();
        getCartCount();
    }

    private void displayView() {
        Fragment fragment = null;
        fragment = new ProductCategoriesFragment();
        if (fragment != null) {
            globalShare.setSelectCategoryId(IDVAL);
            FrameLayout frameLayout = findViewById(R.id.container_body);
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
       // Call<Product> mService = mApiService.getCartCount(PRIMARYID);

        Call<Product> mService=null;
        if(SHORTFORM.equals("SE"))
        {
            if(SharedDB.isDealerExists(getApplicationContext()))
            {
                HashMap<String, String> VALS = SharedDB.getDealerDetails(getApplicationContext());
                String DEALERID=VALS.get(SharedDB.DEALERID);
                mService = mApiService.getCartCount(DEALERID);
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
                      //  Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                mService = mApiService.getCartCount(PRIMARYID);
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
                     //   Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
            mService = mApiService.getCartCount(PRIMARYID);
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
                   // Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
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
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void updateCartCountNotification(String count) {
        itemscount = count;
        ProductCategoriesActivity instance = ProductCategoriesActivity.getInstance();
        if (instance != null) {
            instance.getCartCount(count);
        }
    }

    private static synchronized ProductCategoriesActivity getInstance() {
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
    protected void onResume() {
        super.onResume();
    }

//     public void callPhoneNumber()
//    {
//        try
//        {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            {
//                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    ActivityCompat.requestPermissions(ProductCategoriesActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
//                    return;
//                }
//            }
//            Intent callIntent = new Intent(Intent.ACTION_CALL);
//            callIntent.setData(Uri.parse("tel:" +BRANCHCONTACT));
//            startActivity(callIntent);
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }

    public void callPhoneNumber() {
        if (!BRANCHCONTACT.equals("")) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        ActivityCompat.requestPermissions(ProductCategoriesActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
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
}