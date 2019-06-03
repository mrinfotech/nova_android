package com.mrnovacrm.b2b_dealer;

//public class NotificationStatementActivity {
//}


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class NotificationStatementActivity extends AppCompatActivity implements View.OnTouchListener {

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
    RelativeLayout header;
    String FROMDATE;
    String TODATE;
    public static Activity mainfinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
        mainfinish=this;

        Bundle bundle = getIntent().getExtras();
        titleval = bundle.getString("title");
        PRIMARYID = bundle.getString("user");
        FROMDATE = bundle.getString("fromdate");
        SHORTFORM = bundle.getString("user_role");
        BRANCHID = bundle.getString("branch");

        setTitle(titleval + " List");
        //setContentView(R.layout.layout_creditlist);
        setContentView(R.layout.layout_statementslist);

        header=findViewById(R.id.header);
        header.setVisibility(View.GONE);

//        if (SharedDB.isLoggedIn(getApplicationContext())) {
//            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//            SHORTFORM = values.get(SharedDB.SHORTFORM);
//            BRANCHID = values.get(SharedDB.BRANCHID);
//        }

        imgrel = findViewById(R.id.imgrel);
        imageview = findViewById(R.id.imageview);

        webview = findViewById(R.id.webview);
        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);

        search = findViewById(R.id.search);
        edtxt_fromdate = findViewById(R.id.edtxt_fromdate);
        edtxt_todate = findViewById(R.id.edtxt_todate);

        edtxt_fromdate.setOnTouchListener(NotificationStatementActivity.this);
        edtxt_todate.setOnTouchListener(NotificationStatementActivity.this);

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
        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getApplicationContext());
        if (isConnectedToInternet) {
            loadwebviewdata(FROMDATE,FROMDATE);
        } else {
            Toast.makeText(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadwebviewdata(String FROMDATE,String TODATE)
    {
        progressbar.setVisibility(View.VISIBLE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        String filename = SharedDB.URL + "statement?user="+PRIMARYID+"&fromdate="+FROMDATE+"&todate="+TODATE+"&from_role="+SHORTFORM+"&branch="+BRANCHID;
        Log.e("filename url is", filename);
        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(NotificationStatementActivity.this);
        if(isConnectedToInternet)
        {
            try{
               // Log.e("filename url ********", "http://docs.google.com/gview?embedded=true&url=" + filename);
                String encodeURL= URLEncoder.encode( filename, "UTF-8" );
                //Log.e("encodeURL",encodeURL);
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

        //        boolean isConnectedToInternet1 = CheckNetWork.isConnectedToInternet(this);
//        if (isConnectedToInternet1) {
//            DownLoadFilesAsyncTask asyncTask = new DownLoadFilesAsyncTask(context);
//            asyncTask.execute(filename, 1 + ".pdf");
//        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_invoice, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

//            case R.id.invoice:
//                try {
//                    boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(this);
//                    if (isConnectedToInternet) {
//                        String extStorageDirectory = Environment
//                                .getExternalStorageDirectory().toString();
//                        File fileStrage = new File(extStorageDirectory + "/.NOVA_WALLET");
//                        if (fileStrage.exists()) {
//                            File pdfFile = new File(extStorageDirectory + "/.NOVA_WALLET", 1 + ".pdf");
//                            if (pdfFile.exists()) {
//
//                                Uri path = Uri.fromFile(pdfFile);
//                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                                emailIntent.setType("vnd.android.cursor.dir/email");
//                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
//                                // the mail subject
//                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reg : statement");
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

