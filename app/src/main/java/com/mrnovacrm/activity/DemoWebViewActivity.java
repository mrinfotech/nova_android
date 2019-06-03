package com.mrnovacrm.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mrnovacrm.R;

public class DemoWebViewActivity extends AppCompatActivity {

    WebView mWebview;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_web_view);
        mWebview = findViewById(R.id.webview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        mWebview.getSettings().setJavaScriptEnabled(true);
   //     String filename ="http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";
        String filename="http://mitrayainfo.com/nova_production/invoice/invoice/4";
        mWebview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);
        mWebview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                progressbar.setVisibility(View.GONE);
            }
        });




//        boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(DemoWebViewActivity.this);
//        if (isConnectedToInternet) {
//            mWebview.setVisibility(View.VISIBLE);
//        } else {
//            mWebview.setVisibility(View.GONE);
//        }
//        mWebview.getSettings().setJavaScriptEnabled(true);
//        mWebview.getSettings().setSupportZoom(true);
//        mWebview.getSettings().setBuiltInZoomControls(true);
//        mWebview.getSettings().setDisplayZoomControls(true);
//
//        if (savedInstanceState == null) {
//            if (isConnectedToInternet) {
//                mWebview.setVisibility(View.VISIBLE);
//                loadWebView();
//            } else {
//                mWebview.setVisibility(View.GONE);
//            }
//        }










//        String weburl="http://mitrayainfo.com/nova_production/invoice/invoice/4";


//        mWebview.getSettings().setJavaScriptEnabled(true);
//        mWebview.loadUrl(weburl);

//        mWebview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + weburl);
//        mWebview.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//            }
//
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
//            }
//
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//        });
    }

//    public void loadWebView() {
//        // String pdf = "http://mitrayainfo.com/bt/sellers/genpdf?id="+IDVAL;
//        // String pdf = SharedDB.URL+"invoice/tax_invoice/"+IDVAL;
//
//        //String weburl="http://mitrayainfo.com/nova_production/invoice/invoice/4";
//
//        String pdf = SharedDB.URL+"invoice/invoice/"+4;
//        Log.e("pdf",pdf);
//
//        mWebview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
//        mWebview.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//            }
//
//            @SuppressWarnings("deprecation")
//            @Override
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
//            }
//
//            @TargetApi(android.os.Build.VERSION_CODES.M)
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
//                // Redirect to deprecated method, so you can use it in all SDK versions
//                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
//            }
//        });
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mWebview.saveState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mWebview.restoreState(savedInstanceState);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    if (mWebview.canGoBack()) {
//                        mWebview.goBack();
//                    } else {
//                        finish();
//                    }
//                    return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
