package com.mrnovacrm.b2b_dealer;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.FileDownloader;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.db.SharedDB;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class InvoiceDetailsActivity extends AppCompatActivity {
    WebView webview;
    ProgressBar progressbar;
    private String IDVAL;
    private String INVOICEID;
    // ImageView imgfileshare;
    public static final int RequestPermissionCode = 1;
    Context context;

    String mailSubject = "Attachment Sample";
    public static Activity mainfinish;

      private String PRIMARYID = "";
      private String SHORTFORM="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        mainfinish=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }

        Intent intent = getIntent();
        IDVAL = intent.getStringExtra("id");
        INVOICEID = intent.getStringExtra("invoice_id");

        if(INVOICEID.equals("noinvoiceid"))
        {
            setTitle("Invoice");
        }else{
            setTitle("Invoice id: " + INVOICEID);
        }

        setContentView(R.layout.activity_demo_web_view);
        webview = findViewById(R.id.webview);
        progressbar = findViewById(R.id.progressbar);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
//        String filename = SharedDB.URL + "invoice/invoice/" + IDVAL;
        String filename = SharedDB.URL + "invoice/invoice/" + IDVAL+"/"+SHORTFORM+"/"+PRIMARYID;
        Log.e("filename", filename);

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(InvoiceDetailsActivity.this);
        if(isConnectedToInternet)
        {
            try{
                webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);
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
        requestPermission();
        boolean isConnectedToInternet1 = CheckNetWork.isConnectedToInternet(this);
        if (isConnectedToInternet1) {
            DownLoadFilesAsyncTask asyncTask = new DownLoadFilesAsyncTask(context);
            asyncTask.execute(filename, IDVAL + ".pdf");
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
                        File fileStrage = new File(extStorageDirectory + "/.Nova");
                        if (fileStrage.exists()) {
                            File pdfFile = new File(extStorageDirectory + "/.Nova", IDVAL + ".pdf");
                            if (pdfFile.exists()) {
//                            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                            shareIntent.setType("application/pdf");
//                            //  final File photoFile = new File(getFilesDir(), "foo.jpg");
//                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdfFile));
//
//                            PackageManager pm = getPackageManager();
//                            PackageInfo info = pm.getPackageInfo("com.whatsapp",
//                                    PackageManager.GET_META_DATA);
//                            shareIntent.setPackage("com.whatsapp");
//                            startActivity(Intent.createChooser(shareIntent, "Share invoice using"));

                                Uri path = Uri.fromFile(pdfFile);
                                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                                emailIntent.setType("vnd.android.cursor.dir/email");
                                emailIntent.putExtra(Intent.EXTRA_STREAM, path);
                                // the mail subject
                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reg : " + INVOICEID + " invoice");
                                startActivity(Intent.createChooser(emailIntent, "Share invoice to..."));


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

    public void sendMail(Context context, String mailID, String subject, File attachment, Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_EMAIL, mailID);
        // Need to grant this permission
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // Attachment
        intent.setType("vnd.android.cursor.dir/email");

        if (attachment != null)
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(attachment));
        else if (uri != null)
            intent.putExtra(Intent.EXTRA_STREAM, uri);

        if (!TextUtils.isEmpty(subject))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (isAppAvailable(context, "com.google.android.gm"))
            intent.setPackage("com.google.android.gm");
        startActivityForResult(intent, 101);
    }

    // Check the applications presence

    public static Boolean isAppAvailable(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        boolean isInstalled;
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
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
                File folder = new File(extStorageDirectory, ".NOVA");
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

//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.WindowManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.mrnovacrm.R;
//import com.mrnovacrm.activity.FileDownloader;
//import com.mrnovacrm.constants.CheckNetWork;
//import com.mrnovacrm.db.SharedDB;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class InvoiceDetailsActivity extends AppCompatActivity {
//    WebView webview;
//    ProgressBar progressbar;
//    private String IDVAL;
//    private String INVOICEID;
//   // ImageView imgfileshare;
//   public static final int RequestPermissionCode = 1;
//   Context context;
//
//    String mailID = "kadamba.prasad@gmail.com";
//    String mailSubject = "Attachment Sample";
//    public static Activity mainfinish;
//
//    private String PRIMARYID = "";
//    private String SHORTFORM;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setTheme(R.style.AppTheme);
//        mainfinish=this;
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//    //    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        context = this;
//        Intent intent = getIntent();
//        IDVAL = intent.getStringExtra("id");
//        INVOICEID = intent.getStringExtra("invoice_id");
//
//        if(INVOICEID.equals("noinvoiceid"))
//        {
//            setTitle("Invoice");
//        }else{
//            setTitle("Invoice id: " + INVOICEID);
//        }
//
//        setContentView(R.layout.activity_demo_web_view);
//
//        if (SharedDB.isLoggedIn(getApplicationContext())) {
//            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
//            PRIMARYID = values.get(SharedDB.PRIMARYID);
//            SHORTFORM = values.get(SharedDB.SHORTFORM);
//        }
//        webview = findViewById(R.id.webview);
//        progressbar = findViewById(R.id.progressbar);
//
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setBuiltInZoomControls(true);
//        String filename = SharedDB.URL + "invoice/invoice/" + IDVAL+"/"+SHORTFORM+"/"+PRIMARYID;
//        Log.e("filename", filename);
//
//        boolean isConnectedToInternet = CheckNetWork
//                .isConnectedToInternet(InvoiceDetailsActivity.this);
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
//        requestPermission();
//        boolean isConnectedToInternet1 = CheckNetWork.isConnectedToInternet(this);
//        if (isConnectedToInternet1) {
//            DownLoadFilesAsyncTask asyncTask = new DownLoadFilesAsyncTask(context);
//            asyncTask.execute(filename, INVOICEID + ".pdf");
//        }
//    }
//
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
//                        File fileStrage = new File(extStorageDirectory + "/.Nova");
//                        if (fileStrage.exists()) {
//                            File pdfFile = new File(extStorageDirectory + "/.Nova", INVOICEID + ".pdf");
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
//                                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Reg : " + INVOICEID + " invoice");
//                                startActivity(Intent.createChooser(emailIntent, "Share invoice to..."));
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
//    public void sendMail(Context context, String mailID, String subject, File attachment, Uri uri) {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_SEND);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(Intent.EXTRA_EMAIL, mailID);
//        // Need to grant this permission
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        // Attachment
//        intent.setType("vnd.android.cursor.dir/email");
//
//        if (attachment != null)
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(attachment));
//        else if (uri != null)
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
//
//        if (!TextUtils.isEmpty(subject))
//            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        if (isAppAvailable(context, "com.google.android.gm"))
//            intent.setPackage("com.google.android.gm");
//        startActivityForResult(intent, 101);
//    }
//
//    // Check the applications presence
//
//    public static Boolean isAppAvailable(Context context, String appName) {
//        PackageManager pm = context.getPackageManager();
//        boolean isInstalled;
//        try {
//            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
//            isInstalled = true;
//        } catch (PackageManager.NameNotFoundException e) {
//            isInstalled = false;
//        }
//        return isInstalled;
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
//                File folder = new File(extStorageDirectory, ".NOVA");
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
//}
