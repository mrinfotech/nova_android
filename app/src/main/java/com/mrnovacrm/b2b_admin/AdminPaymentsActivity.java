package com.mrnovacrm.b2b_admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.EmployeeDTO;
import com.mrnovacrm.model.EmployeeListDTO;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.SellerDTO;
import com.mrnovacrm.model.SellersOrdersListDTO;
import com.mrnovacrm.model.WalletCreditDetailsDTO;
import com.mrnovacrm.model.WalletDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPaymentsActivity extends AppCompatActivity implements View.OnTouchListener, AdapterView.OnItemClickListener, View.OnClickListener {

    String BRANCHIDVAL;
    private String PRIMARYID;
    private String SHORTFORM;
    ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
    ArrayList<String> dealernamesList=new ArrayList<>();

    HashMap<String,String> dealernameHashMap=new HashMap<String,String>();
    HashMap<String,String> dealeraddressHashMap=new HashMap<String,String>();

    AutoCompleteTextView autocompletetextview;
    private String searchText;
    private boolean isConnectedToInternet;
    private ArrayList<HashMap<String, String>> cityZipList;
    ArrayList<HashMap<String,String>> autoCompleteList;

    Spinner orders_spinner,paymentmode_spinner,chequemode_spinner;

    ArrayList<String> orderNamesList = new ArrayList<>();
    ArrayList<String> orderIdsList = new ArrayList<>();

    ArrayList<String> paymentmodeNamesList = new ArrayList<>();
    ArrayList<String> paymentmodeIdsList = new ArrayList<>();
    private String ORDER_ID="";
    private String PAYMENTMODE_ID="";

    LinearLayout chequenumber_linear,depositdate_linear,address_linear;
    static EditText transactiondate_txt;

    ArrayList<String> checkmodelist=new ArrayList<>();
    private String CHEQUEMODE_ID="";
    EditText bankaccountnumber_txt,bankaccountname_txt,bankname_txt,amount_txt,chequenumber_txt,discount_txt,
            transactionno_txt;
    Button submit_btn;
    Context mContext;
    RadioGroup radiogroup;
    private String REASONVAL;
    private String SELECT_DEALER_ID="";
    String discount_point_id="";
    String discount_point="";
    String TRANSACTION_NUMBER="";
    public static Activity mainfinish;
    TextView addresstext;
    private String BRANCH_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        mContext=this;
        mainfinish=this;
        setContentView(R.layout.layout_adminpayment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Payments");

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHIDVAL = values.get(SharedDB.BRANCHID);
        }

        Bundle bundle=getIntent().getExtras();
        BRANCH_ID=bundle.getString("BRANCH_ID");

        radiogroup=findViewById(R.id.radiogroup);
       // depositdate_linear=findViewById(R.id.depositdate_linear);
        depositdate_linear=findViewById(R.id.depositdate_linear);
        orders_spinner=findViewById(R.id.orders_spinner);
        paymentmode_spinner=findViewById(R.id.paymentmode_spinner);
        chequemode_spinner=findViewById(R.id.chequemode_spinner);

        autocompletetextview=findViewById(R.id.autocompletetextview);
        chequenumber_linear=findViewById(R.id.chequenumber_linear);
        transactiondate_txt=findViewById(R.id.transactiondate_txt);
        chequenumber_txt=findViewById(R.id.chequenumber_txt);

        discount_txt=findViewById(R.id.discount_txt);

        transactionno_txt=findViewById(R.id.transactionno_txt);

        bankaccountnumber_txt=findViewById(R.id.bankaccountnumber_txt);
        bankaccountname_txt=findViewById(R.id.bankaccountname_txt);
        bankname_txt=findViewById(R.id.bankname_txt);
        amount_txt=findViewById(R.id.amount_txt);
        submit_btn=findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(AdminPaymentsActivity.this);


        address_linear=findViewById(R.id.address_linear);
        addresstext=findViewById(R.id.addresstext);

     //   transactiondate_txt.setHint("Transaction date");

        transactiondate_txt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.transactiondate_txt) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DateType", "fromDate");
                    DialogFragment fromfragment = new DatePickerFragment();
                    fromfragment.setArguments(bundle);
                    fromfragment.show(getSupportFragmentManager(), "Date Picker");
                }
                return true;
            }
        });

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getApplicationContext());
        if(isConnectedToInternet)
        {
            getDealersList();
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        chequenumber_linear.setVisibility(View.GONE);
        depositdate_linear.setVisibility(View.VISIBLE);
        chequemode_spinner.setVisibility(View.GONE);
        hintPaymentmodes();
    //    hintOrders();
        loadPaymentmodes();

        checkmodelist.add("Select cheque status *");
        checkmodelist.add("pass");
        checkmodelist.add("bounce");
        loadChequeModeList();

        getRejectPoints();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.submit_btn:
                showValidation();
                break;
            default:
                break;
        }
    }

    public void showValidation()
    {
        String DEALER_NAME=autocompletetextview.getText().toString();
        String CHEQUE_NUMER=chequenumber_txt.getText().toString();
        String BANKACCOUNT_NUMBER=bankaccountnumber_txt.getText().toString();
        String BANKACCOUNT_NAME=bankaccountname_txt.getText().toString();
        String BANK_NAME=bankname_txt.getText().toString();
        String AMOUNT=amount_txt.getText().toString();
        String DEPOSIT_DATE=transactiondate_txt.getText().toString();

//        if (SELECT_DEALER_ID == null || "".equalsIgnoreCase(DEALER_NAME) || SELECT_DEALER_ID.equals("")
//               // || ORDER_ID == null || "".equalsIgnoreCase(ORDER_ID) || ORDER_ID.equals("")
//                || PAYMENTMODE_ID == null || "".equalsIgnoreCase(PAYMENTMODE_ID) || PAYMENTMODE_ID.equals("")
//                || BANKACCOUNT_NUMBER == null || "".equalsIgnoreCase(BANKACCOUNT_NUMBER) || BANKACCOUNT_NUMBER.equals("")
//                || BANKACCOUNT_NAME == null || "".equalsIgnoreCase(BANKACCOUNT_NAME) || BANKACCOUNT_NAME.equals("")
//                || BANK_NAME == null || "".equalsIgnoreCase(BANK_NAME) || BANK_NAME.equals("")
//                || AMOUNT == null || "".equalsIgnoreCase(AMOUNT) || AMOUNT.equals("")
//                ) {
//            Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
//        } else {
//            if(PAYMENTMODE_ID.equals("CHEQUE"))
//            {
//                if (CHEQUE_NUMER == null || "".equalsIgnoreCase(CHEQUE_NUMER) || CHEQUE_NUMER.equals("")
//                    || DEPOSIT_DATE == null || "".equalsIgnoreCase(DEPOSIT_DATE) || DEPOSIT_DATE.equals("")   ) {
//                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
//                }else{
//                    submitDetails(DEALER_NAME,ORDER_ID,PAYMENTMODE_ID,CHEQUEMODE_ID,CHEQUE_NUMER,BANKACCOUNT_NUMBER,BANK_NAME,AMOUNT,BANKACCOUNT_NAME,DEPOSIT_DATE);
//                }
//            }else{
//                submitDetails(DEALER_NAME,ORDER_ID,PAYMENTMODE_ID,CHEQUEMODE_ID,CHEQUE_NUMER,BANKACCOUNT_NUMBER,BANK_NAME,AMOUNT,BANKACCOUNT_NAME,DEPOSIT_DATE);
//            }
//        }

        if (SELECT_DEALER_ID == null || "".equalsIgnoreCase(SELECT_DEALER_ID) || SELECT_DEALER_ID.equals("")
                || PAYMENTMODE_ID == null || "".equalsIgnoreCase(PAYMENTMODE_ID) || PAYMENTMODE_ID.equals("")
//                || BANKACCOUNT_NUMBER == null || "".equalsIgnoreCase(BANKACCOUNT_NUMBER) || BANKACCOUNT_NUMBER.equals("")
//                || BANKACCOUNT_NAME == null || "".equalsIgnoreCase(BANKACCOUNT_NAME) || BANKACCOUNT_NAME.equals("")
//                || BANK_NAME == null || "".equalsIgnoreCase(BANK_NAME) || BANK_NAME.equals("")
                || AMOUNT == null || "".equalsIgnoreCase(AMOUNT) || AMOUNT.equals("")
                ) {
            Toast.makeText(getApplicationContext(), "* fields are mandatory", Toast.LENGTH_SHORT).show();
        } else {
            if(PAYMENTMODE_ID.equals("CHEQUE"))
            {
                if (CHEQUE_NUMER == null || "".equalsIgnoreCase(CHEQUE_NUMER) || CHEQUE_NUMER.equals("")
                        || DEPOSIT_DATE == null || "".equalsIgnoreCase(DEPOSIT_DATE) || DEPOSIT_DATE.equals("")
                        || CHEQUEMODE_ID == null || "".equalsIgnoreCase(CHEQUEMODE_ID) || CHEQUEMODE_ID.equals("")
                        ) {
                    Toast.makeText(getApplicationContext(), "* fields are mandatory", Toast.LENGTH_SHORT).show();
                }else{
                    submitDetails(DEALER_NAME,ORDER_ID,PAYMENTMODE_ID,CHEQUEMODE_ID,CHEQUE_NUMER,BANKACCOUNT_NUMBER,BANK_NAME,AMOUNT,BANKACCOUNT_NAME,DEPOSIT_DATE);
                }
            }else{
                submitDetails(DEALER_NAME,ORDER_ID,PAYMENTMODE_ID,CHEQUEMODE_ID,CHEQUE_NUMER,BANKACCOUNT_NUMBER,BANK_NAME,AMOUNT,BANKACCOUNT_NAME,DEPOSIT_DATE);
            }
        }
    }

    public void submitDetails(String DEALER_NAME,String ORDER_ID,String PAYMENTMODE_ID,String CHEQUEMODE_ID,
                              String CHEQUE_NUMER,String BANKACCOUNT_NUMBER,String BANK_NAME,String AMOUNT,String BANKACCOUNT_NAME,
                              String DEPOSIT_DATE)
    {

        String DISCOUNT=discount_txt.getText().toString().trim();
        String TRANSACTION_NO=transactionno_txt.getText().toString().trim();

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(AdminPaymentsActivity.this);
        if(isConnectedToInternet)
        {
            final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
            dialog.show();
            RetrofitAPI mApiService = SharedDB.getInterfaceService();
            Call<Login> mService = null;

//            mService = mApiService.addPaymentFealds(ORDER_ID,PAYMENTMODE_ID,CHEQUE_NUMER,CHEQUEMODE_ID,BANKACCOUNT_NAME,
//                    BANKACCOUNT_NUMBER,BANK_NAME,PRIMARYID,SHORTFORM,DEPOSIT_DATE);

            mService = mApiService.addPaymentFealds(SELECT_DEALER_ID,AMOUNT,DISCOUNT,discount_point_id,discount_point,
                    PAYMENTMODE_ID,CHEQUE_NUMER,DEPOSIT_DATE,CHEQUEMODE_ID,BANKACCOUNT_NAME,BANKACCOUNT_NUMBER,
                    BANK_NAME,PRIMARYID,TRANSACTION_NO,BRANCH_ID);

           mService.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    Login mLoginObject = response.body();
                    dialog.dismiss();
                    try {
                        String status = mLoginObject.getStatus();
                        if (status.equals("1")) {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(getApplicationContext(),AdminPaymentsActivity.class);
                            intent.putExtra("BRANCH_ID",BRANCH_ID);
                            startActivity(intent);
                            finish();
                        } else {
                            String message = mLoginObject.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }
                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    call.cancel();
                    dialog.dismiss();
                    //     Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
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
                }
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        private DatePickerDialog.OnDateSetListener from_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        transactiondate_txt.setText(dayOfMonth + "-" + month + "-" + year);
                    }
                };


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        }
    }

    private void getDealersList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(AdminPaymentsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<EmployeeDTO> mService=null;
        mService = mApiService.getDealersList(BRANCH_ID);

        mService.enqueue(new Callback<EmployeeDTO>() {
            @Override
            public void onResponse(Call<EmployeeDTO> call, Response<EmployeeDTO> response) {
                Log.e("response",""+response);
                dialog.dismiss();
                try {
                    EmployeeDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
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

//                                    hashMap.put("company_name", company_name);
//                                    hashMap.put("id", id);
//                                    hashMap.put("empid", empid);
//                                    hashMap.put("name", name);
//                                    hashMap.put("role", role);
//                                    hashMap.put("address", address);
//                                    hashMap.put("mobile", mobile);
//                                    hashMap.put("branch_name", branch_name);
//
//                                    dealernameHashMap.put(company_name,id);
//                                    dealernameHashMap.put(empid,id);
//
//                                    dealeraddressHashMap.put(id,address);
//
//                                    hashmapList.add(hashMap);
//                                    dealernamesList.add(company_name);
//                                    dealernamesList.add(empid);



                                    hashMap.put("company_name", company_name);
                                    hashMap.put("id", id);
                                    hashMap.put("empid", empid);
                                    hashMap.put("name", name);
                                    hashMap.put("role", role);
                                    hashMap.put("address", address);
                                    hashMap.put("mobile", mobile);
                                    hashMap.put("branch_name", branch_name);

                                    dealernameHashMap.put(empid,id);
                                    dealeraddressHashMap.put(id,address);
                                    hashmapList.add(hashMap);

                                    if(address.equals(""))
                                    {
                                        dealernamesList.add(company_name);
                                        dealernameHashMap.put(company_name,id);
                                    }else{
                                        dealernamesList.add(company_name+", \n"+address);
                                        dealernameHashMap.put(company_name+", \n"+address,id);
                                    }

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
                   Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadData()
    {
        AutoCompleteAdapter adapter = new AutoCompleteAdapter(this,
                R.layout.searchautocomplete_payments, R.id.searchtextview, dealernamesList);
        autocompletetextview.setAdapter(adapter);
        autocompletetextview.setThreshold(1);
//        autocompletetextview.setOnTouchListener(AdminPaymentsActivity.this);
        autocompletetextview.setOnItemClickListener(AdminPaymentsActivity.this);
        autocompletetextview.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    hideKeyboard();
                    searchText = autocompletetextview.getText().toString();
                    if (searchText == null || autocompletetextview.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Select Item", Toast.LENGTH_SHORT).show();
                    } else {
                        autocompletetextview.setSelection(0);
                        if(dealernameHashMap.containsKey(searchText))
                        {
                            String itemId=dealernameHashMap.get(searchText);
                            SELECT_DEALER_ID=dealernameHashMap.get(searchText);
                            String address=dealeraddressHashMap.get(SELECT_DEALER_ID);

                            address_linear.setVisibility(View.VISIBLE);
                            addresstext.setText(address);

//                            boolean isConnectedToInternet = CheckNetWork
//                                    .isConnectedToInternet(getApplicationContext());
//                            if (isConnectedToInternet) {
//                                getOrdersList(itemId);
//                            } else {
//                                Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
//                            }
                        }else{
                            address_linear.setVisibility(View.GONE);
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
        isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(AdminPaymentsActivity.this);
        if (isConnectedToInternet) {

            searchText = autocompletetextview.getText().toString();
            if (searchText == null || searchText.equals("")) {
                Toast.makeText(getApplicationContext(), "Please select item", Toast.LENGTH_SHORT).show();
            } else {
                autocompletetextview.setSelection(0);
                if(dealernameHashMap.containsKey(searchText))
                {
                    String itemId=dealernameHashMap.get(searchText);
                    SELECT_DEALER_ID=dealernameHashMap.get(searchText);

                    String address=dealeraddressHashMap.get(SELECT_DEALER_ID);

                    address_linear.setVisibility(View.VISIBLE);
                    addresstext.setText(address);

//                    boolean isConnectedToInternet = CheckNetWork
//                            .isConnectedToInternet(getApplicationContext());
//                    if (isConnectedToInternet) {
//                        String itemId=dealernameHashMap.get(searchText);
//                        getOrdersList(itemId);
//                    } else {
//                        Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
//                    }
                }else{
                    address_linear.setVisibility(View.GONE);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    public void loadChequeModeList() {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, checkmodelist);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        chequemode_spinner.setAdapter(spinnerClass);
        chequemode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    CHEQUEMODE_ID = "";
                } else {
                    CHEQUEMODE_ID=checkmodelist.get(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void hintPaymentmodes() {
        paymentmodeNamesList.clear();
        paymentmodeNamesList.add("Select Payment mode *");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, paymentmodeNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        paymentmode_spinner.setAdapter(spinnerClass);
        paymentmode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    PAYMENTMODE_ID = "";
                } else {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void hintOrders() {
        orderNamesList.clear();
        orderNamesList.add("Select Order *");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, orderNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        orders_spinner.setAdapter(spinnerClass);
        orders_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    ORDER_ID = "";
                } else {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void loadPaymentmodes() {
        paymentmodeNamesList.clear();
        paymentmodeNamesList.add("Select payment mode *");
        paymentmodeNamesList.add("CHEQUE");
        paymentmodeNamesList.add("IMPS");
        paymentmodeNamesList.add("NEFT");
        paymentmodeNamesList.add("RTGS");

        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, paymentmodeNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        paymentmode_spinner.setAdapter(spinnerClass);
        paymentmode_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    PAYMENTMODE_ID = "";
                    transactiondate_txt.setHint("Transaction date");
                } else {
                    PAYMENTMODE_ID=paymentmodeNamesList.get(position);
                    if(paymentmodeNamesList.get(position).equals("CHEQUE"))
                    {
                        chequenumber_linear.setVisibility(View.VISIBLE);
                        depositdate_linear.setVisibility(View.VISIBLE);
                        chequemode_spinner.setVisibility(View.VISIBLE);
                        transactiondate_txt.setHint("Deposit date *");
                        loadChequeModeList();
                    }else{
                        chequenumber_linear.setVisibility(View.GONE);
                        depositdate_linear.setVisibility(View.VISIBLE);
                        chequemode_spinner.setVisibility(View.GONE);
                        transactiondate_txt.setHint("Transaction date");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void getOrdersList(String dealerid) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(AdminPaymentsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<WalletDTO> mService = mApiService.getWalletOrdersList(dealerid);

        mService.enqueue(new Callback<WalletDTO>() {
            @Override
            public void onResponse(Call<WalletDTO> call, Response<WalletDTO> response) {
                dialog.dismiss();
                try {
                    WalletDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    if (Integer.parseInt(status) == 1) {
                        List<WalletCreditDetailsDTO> ordersList = mOrderObject.getDebitordersDTOList();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                ArrayList<String> orderNamesList=new ArrayList<>();
                                orderNamesList.add("Select order id");
                                ArrayList<String> orderIdsList=new ArrayList<>();

                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String orderId = ordersList.get(i).getOrder_id();
                                    orderNamesList.add(orderId);
                                    orderIdsList.add(id);
                                    hashMap.put("id", id);
                                    hashMap.put("orderId", orderId);
                                    hashmapList.add(hashMap);
                                }
                                loadOrders(orderNamesList,orderIdsList);
                            } else {
                            }
                        }
                    } else {
                        hintOrders();
                        Toast.makeText(getApplicationContext(),"No orders found",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<WalletDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //    Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadOrders(ArrayList<String> orderNamesList,final ArrayList<String> orderIdsList)
    {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, orderNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        orders_spinner.setAdapter(spinnerClass);
        orders_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    ORDER_ID = "";
                } else {
                    ORDER_ID=orderIdsList.get(position-1);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void getRejectPoints() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<SellerDTO> mService = mApiService.getDiscountPoints();

        mService.enqueue(new Callback<SellerDTO>() {
            @Override
            public void onResponse(Call<SellerDTO> call, Response<SellerDTO> response) {
                dialog.dismiss();
                try {
                    SellerDTO mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    if (Integer.parseInt(status) == 1) {
                        List<SellersOrdersListDTO> sellersList = mOrderObject.getSellersOrdersListDTO();
                        if (sellersList != null) {
                            if (sellersList.size() > 0) {
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < sellersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = sellersList.get(i).getId();
                                    String dis_point = sellersList.get(i).getDis_point();
                                    hashMap.put("id", id);
                                    hashMap.put("dis_point", dis_point);
                                    hashmapList.add(hashMap);
                                }
                                loadreasondata(hashmapList);
                            } else {
                            }
                        }
                    } else {
                        String message = mOrderObject.getMessage();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<SellerDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadreasondata(final ArrayList<HashMap<String, String>> hashmapList)
    {
        RadioGroup.LayoutParams rprms;
        for (int i = 0; i < hashmapList.size(); i++) {

            String id = hashmapList.get(i).get("id");
            String dis_point = hashmapList.get(i).get("dis_point");
            RadioButton rdbtn = new RadioButton(mContext);
            rdbtn.setId(Integer.parseInt(id));
            rdbtn.setText(dis_point);
            rdbtn.setTextColor(Color.BLACK);
//                rdbtn.setTextColor(getResources().getColor(R.color.black));
            rprms = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            radiogroup.addView(rdbtn, rprms);
        }

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                REASONVAL = String.valueOf(checkedId);
               // DISCOUNT = String.valueOf(checkedId);
                discount_point_id=String.valueOf(checkedId);

                for (int i = 0; i < hashmapList.size(); i++) {
                    String id = hashmapList.get(i).get("id");
                    if(id.equals(discount_point_id))
                    {
                        discount_point=hashmapList.get(i).get("dis_point");
                    }

                }


            }
        });
    }


//    private class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
//
//
//        public AutoCompleteAdapter(Context context, int textViewResourceId,ArrayList<HashMap<String,String>> data) {
//            super(context, textViewResourceId);
//        }
//
//        @Override
//        public int getCount() {
//            return autoCompleteList.size();
//        }
//
//        @Override
//        public String getItem(int position) {
//            return autoCompleteList.get(position).get("city");
//        }
//
//        @Override
//        public Filter getFilter() {
//            Filter filter = new Filter() {
//                @Override
//                protected FilterResults performFiltering(final CharSequence constraint) {
//                    FilterResults filterResults = new FilterResults();
//                    if (constraint != null) {
//                        autoCompleteList = new ArrayList<HashMap<String, String>>();
//                        for (int i = 0; i < cityZipList.size(); i++) {
//                            if (cityZipList.get(i).get("city").startsWith(
//                                    constraint.toString()) || cityZipList.get(i).get("zip").startsWith(
//                                    constraint.toString())) {
//                                autoCompleteList.add(cityZipList.get(i));
//                            }
//                        }
//                        // Now assign the values and count to the FilterResults
//                        // object
//                        filterResults.values = autoCompleteList;
//                        filterResults.count = autoCompleteList.size();
//                    }
//                    return filterResults;
//                }
//                @Override
//                protected void publishResults(CharSequence constraint, FilterResults results) {
//                    if (results != null && results.count > 0) {
//                        notifyDataSetChanged();
//                    } else {
//                        notifyDataSetInvalidated();
//                    }
//                }
//            };
//            return filter;
//        }
//    }

    public class AutoCompleteAdapter extends ArrayAdapter<String> implements
            Filterable {

        private ArrayList<String> fullList;
        private ArrayList<String> mOriginalValues;
        private ArrayFilter mFilter;

        public AutoCompleteAdapter(Context context, int resource,
                                   int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
            fullList = (ArrayList<String>) objects;
            mOriginalValues = new ArrayList<String>(fullList);
        }

        @Override
        public int getCount() {
            return fullList.size();
        }

        @Override
        public String getItem(int position) {
            return fullList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new ArrayFilter();
            }
            return mFilter;
        }

        private class ArrayFilter extends Filter {
            private Object lock;

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();

                if (mOriginalValues == null) {
                    synchronized (lock) {
                        mOriginalValues = new ArrayList<String>(fullList);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    synchronized (lock) {
                        ArrayList<String> list = new ArrayList<String>(
                                mOriginalValues);
                        results.values = list;
                        results.count = list.size();
                    }
                } else {
                    final String prefixString = prefix.toString().toLowerCase();

                    ArrayList<String> values = mOriginalValues;
                    int count = values.size();

                    ArrayList<String> newValues = new ArrayList<String>(count);

                    for (int i = 0; i < count; i++) {
                        String item = values.get(i);

                        if (item.toLowerCase().startsWith(prefixString)) {
                            newValues.add(item);
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                if (results.values != null) {
                    fullList = (ArrayList<String>) results.values;
                } else {
                    fullList = new ArrayList<String>();
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }

    @SuppressLint("ViewHolder")
    public class SpinnerItemsAdapter extends ArrayAdapter<String> {
        private final Activity context;
        ArrayList<String> values;
        /*
         * Override the constructor for ArrayAdapter the only variable objects,
         * because it is the list of objects to display.
         */
        public SpinnerItemsAdapter(Activity context, int textViewResourceId,
                                   ArrayList<String> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        public int getCount() {
            return values.size();
        }

        public long getItemId(int position) {
            return position;
        }

        /* Overriding the getView method */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater
                    .inflate(R.layout.layout_spinneritems, null, true);
            TextView txtTitle = (TextView) rowView
                    .findViewById(R.id.spinnersubitemslist);
            txtTitle.setText(values.get(position));

            if(position==0)
            {
                txtTitle.setTextColor(getResources().getColor(R.color.darkgrey));
            }else{
                txtTitle.setTextColor(getResources().getColor(R.color.light_black));
            }
            return rowView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater
                    .inflate(R.layout.layout_spinneritems, null, true);
            TextView txtTitle = (TextView) rowView
                    .findViewById(R.id.spinnersubitemslist);
            txtTitle.setText(values.get(position));

            if(position==0)
            {
                txtTitle.setTextColor(getResources().getColor(R.color.darkgrey));
            }else{
                txtTitle.setTextColor(getResources().getColor(R.color.light_black));
            }
            return rowView;
        }
    }
}