package com.mrnovacrm.wallet;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.FileDownloader;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.db.SharedDB;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StatementListActivity extends AppCompatActivity implements View.OnTouchListener {

    private String titleval;
    private String PRIMARYID;
    private String SHORTFORM;
    private String BRANCHID;
    public static EditText edtxt_fromdate, edtxt_todate;
    ImageView search;
    private SimpleDateFormat dfDate;
    RelativeLayout imgrel;
    ImageView imageview;
    WebView webview;
    ProgressBar progressbar;
    Context context;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;

        Bundle bundle = getIntent().getExtras();
        titleval = bundle.getString("title");
        setTitle(titleval + " List");
        //setContentView(R.layout.layout_creditlist);
        setContentView(R.layout.layout_statementslist);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }

        imgrel = findViewById(R.id.imgrel);
        imageview = findViewById(R.id.imageview);

        webview = findViewById(R.id.webview);
        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);

        search = findViewById(R.id.search);
        edtxt_fromdate = findViewById(R.id.edtxt_fromdate);
        edtxt_todate = findViewById(R.id.edtxt_todate);

        edtxt_fromdate.setOnTouchListener(StatementListActivity.this);
        edtxt_todate.setOnTouchListener(StatementListActivity.this);

        requestPermission();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FROMDATE = edtxt_fromdate.getText().toString().trim();
                String TODATE = edtxt_todate.getText().toString().trim();
                // dfDate = new SimpleDateFormat("yyyy-MM-dd");
                dfDate = new SimpleDateFormat("dd-MM-yyyy");
                if (FROMDATE.equals("") || FROMDATE.equals(null) || TODATE.equals("") || TODATE.equals(null)) {
                    Toast.makeText(getApplicationContext(), "Please enter from date and to date", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
                            boolean isConnectedToInternet = CheckNetWork
                                    .isConnectedToInternet(getApplicationContext());
                            if (isConnectedToInternet) {
                                loadwebviewdata(FROMDATE,TODATE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {
                            boolean isConnectedToInternet = CheckNetWork
                                    .isConnectedToInternet(getApplicationContext());
                            if (isConnectedToInternet) {
                                loadwebviewdata(FROMDATE,TODATE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "From Date Should be above To Date", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void loadwebviewdata(String FROMDATE,String TODATE)
    {
        progressbar.setVisibility(View.VISIBLE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        String filename = SharedDB.URL + "statement?user="+PRIMARYID+"&fromdate="+FROMDATE+"&todate="+TODATE+"&from_role="+SHORTFORM+"&branch="+BRANCHID;
        Log.e("filename", filename);

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(StatementListActivity.this);
        if(isConnectedToInternet)
        {
            try{
                String encodeURL= URLEncoder.encode( filename, "UTF-8" );
                webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + encodeURL);
            }catch(Exception e)
            {
            }
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.GONE);
        }

        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                progressbar.setVisibility(View.GONE);
            }
        });

        boolean isConnectedToInternet1 = CheckNetWork.isConnectedToInternet(this);
        if (isConnectedToInternet1) {
            DownLoadFilesAsyncTask asyncTask = new DownLoadFilesAsyncTask(context);
            asyncTask.execute(filename, 1 + ".pdf");
        }
    }

    public class DownLoadFilesAsyncTask extends AsyncTask<String, Void, Void> {
        private Context mContext;
        private boolean isConnectedToInternet;
        ArrayList<String> searchList;
        private String response;
        int status;

        public DownLoadFilesAsyncTask(Context context) {
            this.mContext = context;
        }
        @Override
        protected void onPreExecute() {
            /* Show ProgressDialog */
        }
        /* Getting RoadNumber and RoadName using highway and avenue urls */
        @Override
        protected Void doInBackground(String... params) {

            try {
                String downnLoadURL = params[0];
                String fileName = params[1];
                String extStorageDirectory = Environment
                        .getExternalStorageDirectory().toString();
                File folder = new File(extStorageDirectory, ".NOVA_WALLET");
                folder.mkdir();
                File imageFile = new File(folder, fileName);
                try {
                    imageFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileDownloader.downloadFile(downnLoadURL, imageFile);
            } catch (Exception e) {
            }
            return null;
        }

        protected void onPostExecute(Void results) {
            try {
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_invoice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.invoice:
                try {
                    boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(this);
                    if (isConnectedToInternet) {
                        String extStorageDirectory = Environment
                                .getExternalStorageDirectory().toString();
                        File fileStrage = new File(extStorageDirectory + "/.NOVA_WALLET");
                        if (fileStrage.exists()) {
                            File pdfFile = new File(extStorageDirectory + "/.NOVA_WALLET", 1 + ".pdf");
                            if (pdfFile.exists()) {

                                Uri path = Uri.fromFile(pdfFile);
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("vnd.android.cursor.dir/email");
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                // the mail subject
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reg : statement");
                                startActivity(Intent.createChooser(emailIntent, "Share statement to..."));
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_fromdate) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getSupportFragmentManager(), "Date Picker");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_todate) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("DateType", "toDate");
            DialogFragment tofragment = new DatePickerFragment();
            tofragment.setArguments(bundle2);
            tofragment.show(getSupportFragmentManager(), "Date Picker");
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

                } else if (type.equals("toDate")) {
                    return new DatePickerDialog(getActivity(), to_dateListener, year, month, day);
                }
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        edtxt_fromdate.setText(dayOfMonth + "-" + month + "-" + year);

                    }
                };
        private DatePickerDialog.OnDateSetListener to_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        edtxt_todate.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            //          Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    //    boolean READ_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                }
                break;
        }
    }
}


//package com.nova.wallet;
//
//import android.Manifest;
//import android.app.DatePickerDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nova.R;
//import com.nova.activity.FileDownloader;
//import com.nova.constants.CheckNetWork;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.Order;
//import com.nova.model.StatementDetailsDTO;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class StatementListActivity extends AppCompatActivity implements View.OnTouchListener {
//
//    RecyclerView recyclerView;
//    private String titleval;
//    Context mContext;
//    private String PRIMARYID;
//    private RelativeLayout imgrel;
//    ImageView imageview;
//    WebView webview;;
//    ProgressBar progressbar;
//    Context context;
//    public static final int RequestPermissionCode = 1;
//    public static EditText edtxt_fromdate, edtxt_todate;
//    ImageView search;
//    private SimpleDateFormat dfDate;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext=this;
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setTheme(R.style.AppTheme);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        Bundle bundle=getIntent().getExtras();
//        titleval=bundle.getString("title");
//        setTitle(titleval);
//
//        if (SharedDB.isLoggedIn(getApplicationContext())) {
//            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//        }
//        setContentView(R.layout.layout_statementslist);
//
//        imgrel = findViewById(R.id.imgrel);
//        imageview = findViewById(R.id.imageview);
//        progressbar = findViewById(R.id.progressbar);
//        progressbar.setVisibility(View.GONE);
//
//        search = findViewById(R.id.search);
//        edtxt_fromdate = findViewById(R.id.edtxt_fromdate);
//        edtxt_todate = findViewById(R.id.edtxt_todate);
//
//        edtxt_fromdate.setOnTouchListener(StatementListActivity.this);
//        edtxt_todate.setOnTouchListener(StatementListActivity.this);
//
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String FROMDATE=edtxt_fromdate.getText().toString().trim();
//                String TODATE=edtxt_todate.getText().toString().trim();
//
//                // dfDate = new SimpleDateFormat("yyyy-MM-dd");
//                dfDate = new SimpleDateFormat("dd-MM-yyyy");
//                if(FROMDATE.equals("")|| FROMDATE.equals(null) || TODATE.equals("")|| TODATE.equals(null))
//                {
//                    Toast.makeText(getApplicationContext(),"Please enter from date and to date",Toast.LENGTH_SHORT).show();
//                }else{
//                    try {
//                        if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
//                            boolean isConnectedToInternet = CheckNetWork
//                                    .isConnectedToInternet(getApplicationContext());
//                            if(isConnectedToInternet)
//                            {
//                                loadwebviewdata(FROMDATE,TODATE);
//                            }else{
//                                Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
//                            }
//                        } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {
//
//                            boolean isConnectedToInternet = CheckNetWork
//                                    .isConnectedToInternet(getApplicationContext());
//                            if(isConnectedToInternet)
//                            {
//                                loadwebviewdata(FROMDATE,TODATE);
//                            }else{
//                                Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getApplicationContext(),
//                                    "From Date Should be above To Date", Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    } catch (ParseException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        requestPermission();
//    }
//
//    public void loadwebviewdata(String FROMDATE,String TODATE)
//    {
//        webview = findViewById(R.id.webview);
//        progressbar.setVisibility(View.VISIBLE);
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setBuiltInZoomControls(true);
//        String filename = SharedDB.URL + "invoice/invoice/" + 19;
//
//        boolean isConnectedToInternet = CheckNetWork
//                .isConnectedToInternet(StatementListActivity.this);
//        if(isConnectedToInternet)
//        {
//            try{
//                webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);
//            }catch(Exception e)
//            {
//            }
//        }else{
//            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
//            progressbar.setVisibility(View.GONE);
//        }
//
//        webview.setWebViewClient(new WebViewClient() {
//            public void onPageFinished(WebView view, String url) {
//                // do your stuff here
//                progressbar.setVisibility(View.GONE);
//            }
//        });
//
//        boolean isConnectedToInternet1 = CheckNetWork.isConnectedToInternet(this);
//        if (isConnectedToInternet1) {
//            DownLoadFilesAsyncTask asyncTask = new DownLoadFilesAsyncTask(context);
//            asyncTask.execute(filename, 19 + ".pdf");
//        }
//    }
//
//    public class DownLoadFilesAsyncTask extends AsyncTask<String, Void, Void> {
//
//        private Context mContext;
//        private boolean isConnectedToInternet;
//        ArrayList<String> searchList;
//        private String response;
//        int status;
//
//        public DownLoadFilesAsyncTask(Context context) {
//            this.mContext = context;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            /* Show ProgressDialog */
//        }
//
//        /* Getting RoadNumber and RoadName using highway and avenue urls */
//        @Override
//        protected Void doInBackground(String... params) {
//
//            try {
//                String downnLoadURL = params[0];
//                String fileName = params[1];
//                String extStorageDirectory = Environment
//                        .getExternalStorageDirectory().toString();
//                File folder = new File(extStorageDirectory, ".NOVA_Wallet");
//                folder.mkdir();
//                File imageFile = new File(folder, fileName);
//                try {
//                    imageFile.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                FileDownloader.downloadFile(downnLoadURL, imageFile);
//            } catch (Exception e) {
//            }
//            return null;
//        }
//
//        protected void onPostExecute(Void results) {
//            try {
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(new String[]{
//                            //          Manifest.permission.READ_EXTERNAL_STORAGE,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    RequestPermissionCode);
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case RequestPermissionCode:
//                if (grantResults.length > 0) {
//                    //    boolean READ_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean WRITE_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                }
//                break;
//        }
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_invoice, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//
//            case R.id.invoice:
//                try {
//                    boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(this);
//                    if (isConnectedToInternet) {
//                        String extStorageDirectory = Environment
//                                .getExternalStorageDirectory().toString();
//                        File fileStrage = new File(extStorageDirectory + "/.NOVA_Wallet");
//                        if (fileStrage.exists()) {
//                            File pdfFile = new File(extStorageDirectory + "/.NOVA_Wallet", 19 + ".pdf");
//                            if (pdfFile.exists()) {
////                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
////                            shareIntent.setType("application/pdf");
////                            //  final File photoFile = new File(getFilesDir(), "foo.jpg");
////                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfFile));
////
////                            PackageManager pm = getPackageManager();
////                            PackageInfo info = pm.getPackageInfo("com.whatsapp",
////                                    PackageManager.GET_META_DATA);
////                            shareIntent.setPackage("com.whatsapp");
////                            startActivity(Intent.createChooser(shareIntent, "Share invoice using"));
//
//                                Uri path = Uri.fromFile(pdfFile);
//                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                                emailIntent.setType("vnd.android.cursor.dir/email");
//                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
//                                // the mail subject
//                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reg : " + 19 + " invoice");
//                                startActivity(Intent.createChooser(emailIntent, "Share statement to..."));
//                            }
//                        }
//                    }else{
//                        Toast.makeText(getApplicationContext(),R.string.networkerror,Toast.LENGTH_SHORT).show();
//                    }
//                }catch (Exception e)
//                {
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_fromdate) {
//            Bundle bundle = new Bundle();
//            bundle.putString("DateType", "fromDate");
//            DialogFragment fromfragment = new WalletCreditListActivity.DatePickerFragment();
//            fromfragment.setArguments(bundle);
//            fromfragment.show(getSupportFragmentManager(), "Date Picker");
//        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_todate) {
//            Bundle bundle2 = new Bundle();
//            bundle2.putString("DateType", "toDate");
//            DialogFragment tofragment = new WalletCreditListActivity.DatePickerFragment();
//            tofragment.setArguments(bundle2);
//            tofragment.show(getSupportFragmentManager(), "Date Picker");
//        }
//        return true;
//    }
//
//    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            //Use the current date as the default date in the date picker
//            final Calendar c = Calendar.getInstance();
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//            String type;
//
//            //Create a new DatePickerDialog instance and return it
//        /*
//            DatePickerDialog Public Constructors - Here we uses first one
//            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
//            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
//         */
//            if (getArguments() != null) {
//                type = getArguments().getString("DateType");
//                if (type.equals("fromDate")) {
//                    return new DatePickerDialog(getActivity(), from_dateListener, year, month, day);
//                } else if (type.equals("toDate")) {
//                    return new DatePickerDialog(getActivity(), to_dateListener, year, month, day);
//                }
//            }
//            return new DatePickerDialog(getActivity(), this, year, month, day);
//        }
//
//        private DatePickerDialog.OnDateSetListener from_dateListener =
//                new DatePickerDialog.OnDateSetListener() {
//
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        int month = monthOfYear + 1;
//                        edtxt_fromdate.setText(dayOfMonth + "-" + month + "-" + year);
//
//                    }
//                };
//        private DatePickerDialog.OnDateSetListener to_dateListener =
//                new DatePickerDialog.OnDateSetListener() {
//
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        int month = monthOfYear + 1;
//                        edtxt_todate.setText(dayOfMonth + "-" + month + "-" + year);
//                    }
//                };
//        @Override
//        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//        }
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
//        private Context mContext;
//        ArrayList<HashMap<String, String>> ordersList = new ArrayList<HashMap<String, String>>();
//
//        public RecyclerViewAdapter(Context mContext, ArrayList<HashMap<String, String>> hashmapList) {
//            this.mContext = mContext;
//            ordersList = hashmapList;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.layout_statementlistadapter, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
////            String id = ordersList.get(position).get("id");
////            String orderId = ordersList.get(position).get("orderId");
////            String orderedOn = ordersList.get(position).get("orderedOn");
////            holder.orderidval_txt.setText("");
////            holder.date_text.setText("");
//
//
//
//            String cheque_no = ordersList.get(position).get("cheque_no");
//            String cheque_date = ordersList.get(position).get("cheque_date");
//            String credit = ordersList.get(position).get("credit");
//            String debit = ordersList.get(position).get("debit");
//            String closing_balance = ordersList.get(position).get("closing_balance");
//
//
//
//            holder.chequerefnumber_txt.setText(cheque_no);
//            holder.chequedate_txt.setText(cheque_date);
//
//            if(credit!=null)
//            {
//                if(!credit.equals(""))
//                {
//                    if(credit.equals("NA"))
//                    {
//                        holder.creditamount_txt.setText(credit);
//                    }else{
//                        holder.creditamount_txt.setText("Rs."+credit+"/-");
//                    }
//                }
//            }
//            if(debit!=null)
//            {
//                if(!debit.equals(""))
//                {
//                    if(debit.equals("NA"))
//                    {
//                        holder.debitamount_txt.setText(debit);
//                    }else {
//                        holder.debitamount_txt.setText("Rs." + debit + "/-");
//                    }
//                }
//            }
//            if(closing_balance!=null)
//            {
//                if(!closing_balance.equals(""))
//                {
//                    holder.closingbalance_txt.setText("Rs."+closing_balance+"/-");
//                }
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//               return ordersList.size();
//            //return 10;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            TextView chequerefnumber_txt,chequedate_txt,creditamount_txt,debitamount_txt,closingbalance_txt;
//            Button viewbtn;
//            MyViewHolder(View view) {
//                super(view);
//                chequerefnumber_txt=view.findViewById(R.id.chequerefnumber_txt);
//                chequedate_txt=view.findViewById(R.id.chequedate_txt);
//                creditamount_txt=view.findViewById(R.id.creditamount_txt);
//                debitamount_txt=view.findViewById(R.id.debitamount_txt);
//                closingbalance_txt=view.findViewById(R.id.closingbalance_txt);
//
//                view.setOnClickListener(this);
//            }
//            @Override
//            public void onClick(View view) {
//            }
//        }
//    }
//
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        switch (item.getItemId()) {
////            case android.R.id.home:
////                finish();
////                return true;
////            default:
////                return super.onOptionsItemSelected(item);
////        }
////    }
//
//    private void getStatementList() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Order> mService = null;
//        mService = mApiService.getWalletStatementDetails(PRIMARYID);
//        mService.enqueue(new Callback<Order>() {
//            @Override
//            public void onResponse(Call<Order> call, Response<Order> response) {
//                dialog.dismiss();
//
//                try {
//                    Log.e("response", "" + response);
//                    Order mOrderObject = response.body();
//                    String status = mOrderObject.getStatus();
//                    Log.e("ordersstatus", "" + status);
//                    if (Integer.parseInt(status) == 1) {
//                        recyclerView.setVisibility(View.VISIBLE);
//                        imgrel.setVisibility(View.GONE);
//                        List<StatementDetailsDTO> ordersList = mOrderObject.getStatementDetailsDTOS();
//                        if (ordersList != null) {
//                            if (ordersList.size() > 0) {
//                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
//                                for (int i = 0; i < ordersList.size(); i++) {
//                                    HashMap<String, String> hashMap = new HashMap<String, String>();
//                                    String cheque_no = ordersList.get(i).getCheque_no();
//                                    String cheque_date = ordersList.get(i).getCheque_date();
//                                    String credit = ordersList.get(i).getCredit();
//                                    String debit = ordersList.get(i).getDebit();
//                                    String closing_balance = ordersList.get(i).getClosing_balance();
//
//                                    hashMap.put("cheque_no", cheque_no);
//                                    hashMap.put("cheque_date", cheque_date);
//                                    hashMap.put("credit", credit);
//                                    hashMap.put("debit", debit);
//                                    hashMap.put("closing_balance", closing_balance);
//
//
//                                    hashmapList.add(hashMap);
//                                }
//                                showOrdersData(hashmapList);
//                            } else {
//                                recyclerView.setVisibility(View.GONE);
//                                imgrel.setVisibility(View.VISIBLE);
//                                imageview.setImageResource(R.drawable.noordersfound);
//                            }
//                        }
//                    } else {
//                        recyclerView.setVisibility(View.GONE);
//                        imgrel.setVisibility(View.VISIBLE);
//                        imageview.setImageResource(R.drawable.noordersfound);
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Order> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                //     Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void showOrdersData(ArrayList<HashMap<String, String>> hashmapList)
//    {
//        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this,hashmapList);
//        recyclerView.setAdapter(adapter);
//    }
//}
