package com.mrnovacrm.b2b_dealer;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.AutoCompleteAdapter;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.EmployeeListDTO;

import java.io.File;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesExecutiveWalletFragment extends Fragment implements View.OnTouchListener, AdapterView.OnItemClickListener {

    private String BRANCHID;
    private String PRIMARYID;
    private String SHORTFORM;
    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> dealernamesList=new ArrayList<>();
    HashMap<String,String> dealernameHashMap=new HashMap<String,String>();
    HashMap<String,String> dealeraddressHashMap=new HashMap<String,String>();
    AutoCompleteTextView autocompletetextview;
    private String searchText;
    String SELECT_DEALER_ID="";
    RelativeLayout imgrel;
    ImageView imageview;
    WebView webview;
    ProgressBar progressbar;
    public static EditText edtxt_fromdate, edtxt_todate;
    ImageView search;
    private SimpleDateFormat dfDate;
    public static final int RequestPermissionCode = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_sewallet, container, false);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHID = values.get(SharedDB.BRANCHID);
            PRIMARYID = values.get(SharedDB.PRIMARYID);
        }
        autocompletetextview=rootView.findViewById(R.id.autocompletetextview);

        imgrel = rootView.findViewById(R.id.imgrel);
        imageview = rootView.findViewById(R.id.imageview);

        webview = rootView.findViewById(R.id.webview);
        progressbar = rootView.findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);

        search = rootView.findViewById(R.id.search);
        edtxt_fromdate = rootView.findViewById(R.id.edtxt_fromdate);
        edtxt_todate = rootView.findViewById(R.id.edtxt_todate);

        edtxt_fromdate.setOnTouchListener(SalesExecutiveWalletFragment.this);
        edtxt_todate.setOnTouchListener(SalesExecutiveWalletFragment.this);

        deleteInvoices();

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            getDealersList();
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }

        requestPermission();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FROMDATE = edtxt_fromdate.getText().toString().trim();
                String TODATE = edtxt_todate.getText().toString().trim();
                // dfDate = new SimpleDateFormat("yyyy-MM-dd");
                dfDate = new SimpleDateFormat("dd-MM-yyyy");
                if (FROMDATE.equals("") || FROMDATE.equals(null) || TODATE.equals("") || TODATE.equals(null)) {
                    Toast.makeText(getActivity(), "Please enter from date and to date", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if(!SELECT_DEALER_ID.equals("")) {
                            if (dfDate.parse(FROMDATE).before(dfDate.parse(TODATE))) {
                                boolean isConnectedToInternet = CheckNetWork
                                        .isConnectedToInternet(getActivity());
                                if (isConnectedToInternet) {
                                    loadwebviewdata(FROMDATE, TODATE);
                                } else {
                                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                }
                            } else if (dfDate.parse(FROMDATE).equals(dfDate.parse(TODATE))) {
                                boolean isConnectedToInternet = CheckNetWork
                                        .isConnectedToInternet(getActivity());
                                if (isConnectedToInternet) {
                                    loadwebviewdata(FROMDATE, TODATE);
                                } else {
                                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(),
                                        "From Date Should be above To Date", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }else{
                            Toast.makeText(getActivity(),
                                    "Please select dealer", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        return rootView;
    }

    public void loadwebviewdata(String FROMDATE,String TODATE)
    {
        progressbar.setVisibility(View.VISIBLE);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        String filename = SharedDB.URL + "statement?user="+SELECT_DEALER_ID+"&fromdate="+FROMDATE+"&todate="+TODATE+"&from_role=DEALER&branch="+BRANCHID;
        Log.e("filename", filename);

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            try{
                String encodeURL= URLEncoder.encode( filename, "UTF-8" );
                webview.loadUrl("http://docs.google.com/gview?embedded=true&url=" + encodeURL);
            }catch(Exception e)
            {
            }
        }else{
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            progressbar.setVisibility(View.GONE);
        }

        webview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void getDealersList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeeDTO> mService=null;
        mService = mApiService.getDealersList(BRANCHID, PRIMARYID, SHORTFORM);
        mService.enqueue(new Callback<EmployeeDTO>() {
            @Override
            public void onResponse(Call<EmployeeDTO> call, Response<EmployeeDTO> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    EmployeeDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);
                    if (Integer.parseInt(status) == 1) {
                        List<EmployeeListDTO> ordersList = mOrderObject.getEmployeeListDTO();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String empid = ordersList.get(i).getEmp_id();
                                    String name = ordersList.get(i).getName();
                                    String address = ordersList.get(i).getAddress();
                                    String role = ordersList.get(i).getRole_name();
                                    String mobile = ordersList.get(i).getMobile();
                                    String branch_name = ordersList.get(i).getBranch_name();
                                    String company_name = ordersList.get(i).getCompany_name();

                                    hashMap.put("company_name", company_name);
                                    hashMap.put("id", id);
                                    hashMap.put("empid", empid);
                                    hashMap.put("name", name);
                                    hashMap.put("role", role);
                                    hashMap.put("address", address);
                                    hashMap.put("mobile", mobile);
                                    hashMap.put("branch_name", branch_name);

                                    dealernameHashMap.put(company_name,id);
                                    dealernameHashMap.put(empid,id);

                                    dealeraddressHashMap.put(id,address);

                                    hashmapList.add(hashMap);
                                    dealernamesList.add(company_name);
                                    dealernamesList.add(empid);
                                }
                                loadData();
                            } else {
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<EmployeeDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData()
    {
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(getActivity(),
                R.layout.searchautocomplete, R.id.searchtextview, dealernamesList);
        autocompletetextview.setAdapter(adapter);
        autocompletetextview.setThreshold(1);
//        autocompletetextview.setOnTouchListener(AdminPaymentsActivity.this);
        autocompletetextview.setOnItemClickListener(SalesExecutiveWalletFragment.this);
        autocompletetextview.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyboard();
                    searchText = autocompletetextview.getText().toString();
                    if (searchText == null || autocompletetextview.equals("")) {
                        Toast.makeText(getActivity(), "Please Select Dealer", Toast.LENGTH_SHORT).show();
                    } else {
                        if(dealernameHashMap.containsKey(searchText))
                        {
                            String itemId=dealernameHashMap.get(searchText);
                            SELECT_DEALER_ID=dealernameHashMap.get(searchText);
                            String address=dealeraddressHashMap.get(SELECT_DEALER_ID);

                          //  address_linear.setVisibility(View.VISIBLE);
                           // addresstext.setText(address);
                        }else{
                            //address_linear.setVisibility(View.GONE);
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        searchText = parent.getItemAtPosition(position).toString();
        hideKeyboard();
        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if (isConnectedToInternet) {
            searchText = autocompletetextview.getText().toString();
            if (searchText == null || searchText.equals("")) {
                Toast.makeText(getActivity(), "Please select dealer", Toast.LENGTH_SHORT).show();
            } else {
                if(dealernameHashMap.containsKey(searchText))
                {
                    String itemId=dealernameHashMap.get(searchText);
                    SELECT_DEALER_ID=dealernameHashMap.get(searchText);
                    String address=dealeraddressHashMap.get(SELECT_DEALER_ID);
//                    address_linear.setVisibility(View.VISIBLE);
//                    addresstext.setText(address);
                }else{
                   // address_linear.setVisibility(View.GONE);
                }
            }
        } else {
            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }


    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_fromdate) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DatePickerFragment();
            fromfragment.setArguments(bundle);
            fromfragment.show(getFragmentManager(), "Date Picker");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_todate) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("DateType", "toDate");
            DialogFragment tofragment = new DatePickerFragment();
            tofragment.setArguments(bundle2);
            tofragment.show(getFragmentManager(), "Date Picker");
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
    public void deleteInvoices()
    {
        try{
            File dir = new File(Environment.getExternalStorageDirectory()+"/.NOVA_WALLET");
            if (dir.exists()) {
                if (dir.isDirectory())
                {
                    String[] children = dir.list();

                    if(children!=null)
                    {
                        if(children.length>0)
                        {
                            for (int i = 0; i < children.length; i++)
                            {
                                new File(dir, children[i]).delete();
                            }
                        }
                    }
                }
            }
        }catch (Exception e)
        {
        }
    }

}
