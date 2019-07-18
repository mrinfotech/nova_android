package com.mrnovacrm.b2b_dealer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Checkout;
import com.mrnovacrm.model.OrdersListDTO;
import com.mrnovacrm.model.TransportDTO;
import com.mrnovacrm.model.TransportList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    Button codbtn, paytmbtn, chequebtn;
    Context mContext;
    private HashMap<String, String> values;
    private static String PRIMARYID = "";
    private String DELIVERY_ADDRESS = "";
    private String USERNAME = "";
    private String SHORTFORM = "";
    private String ADDRESSIDVAL = "";
    // private static double LATITUDE_VALUE, LANGITUDE_VALUE;
    private double LATITUDE_VALUE, LANGITUDE_VALUE;
    // static String addressid="";
    GlobalShare globalShare;
    public static int calval = 0;
    public static Context mmContext;
    public static Activity mainfinish;
    public static String FROMVAL = "";
    private Dialog alertDialog;
    private View layout;
    private Spinner credits_spinner;
    private Button popup_submit;
    private Button cheqpopup_submit;
    CheckBox checkbox;
    ArrayList<String> creditDatesList = new ArrayList<>();
    String CREDIT_DAYS = "";
    Spinner transport_spinner;
    private Dialog chequealertDialog;
    private View chequelayout;
    private String DEALERIDVAL = "";
    private String BRANCHID;
    static EditText chequdateeedittext;
    private String TRANSPORT_ID = "";
    private String TRANSPORT_NAME = "";
    ArrayList<String> transportNamesList = new ArrayList<>();
    ArrayList<String> transportIDsList = new ArrayList<>();
    EditText remarkstxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mmContext = this;
        mainfinish = this;
        globalShare = (GlobalShare) getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_payment);

        View actionView = findViewById(R.id.include_actionbar);
        // Fetching the textview declared in footer.xml
        TextView actionTextView = (TextView) actionView.findViewById(R.id.actionbarheadertxt);
        actionTextView.setText("Payment");

        ImageView backimg = (ImageView) actionView.findViewById(R.id.backimg);
        backimg.setOnClickListener(UserPaymentActivity.this);
        codbtn = findViewById(R.id.codbtn);
        codbtn.setOnClickListener(UserPaymentActivity.this);
        paytmbtn = findViewById(R.id.paytmbtn);
        paytmbtn.setOnClickListener(UserPaymentActivity.this);

        remarkstxt = findViewById(R.id.remarkstxt);
        transport_spinner = findViewById(R.id.transport_spinner);
        chequebtn = findViewById(R.id.chequebtn);
        chequebtn.setOnClickListener(UserPaymentActivity.this);

        // addressid = getIntent().getStringExtra("addressid");

        Bundle bundle = getIntent().getExtras();
        DEALERIDVAL = bundle.getString("dealer_id");

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
            USERNAME = values.get(SharedDB.USERNAME);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            ADDRESSIDVAL = values.get(SharedDB.ADDRESSID);
            BRANCHID = values.get(SharedDB.BRANCHID);

            String LAT = values.get(SharedDB.LATITUDE);
            String LNG = values.get(SharedDB.LONGITIDE);
            LANGITUDE_VALUE = Double.parseDouble(LNG);
            LATITUDE_VALUE = Double.parseDouble(LAT);
        }
        if (SHORTFORM.equals("SE")) {
            chequebtn.setVisibility(View.VISIBLE);
            paytmbtn.setVisibility(View.GONE);
        } else {
            chequebtn.setVisibility(View.GONE);
            paytmbtn.setVisibility(View.VISIBLE);
        }
        hintTransport();
        getTrasportsList();
    }

    public void hintTransport() {
        transportNamesList.clear();
        transportNamesList.add("Select Transport");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, transportNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        transport_spinner.setAdapter(spinnerClass);
        transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    TRANSPORT_ID = "";
                } else {
                    TRANSPORT_ID = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backimg:
                finish();
                if (CartListActivity.mainfinish != null) {
                    CartListActivity.mainfinish.finish();
                }
                Intent intent = new Intent(getApplicationContext(), CartListActivity.class);
                startActivity(intent);
                break;
            case R.id.paytmbtn:
                //  FROMVAL="netbanking";
                //  checkoutCart("","","",LATITUDE_VALUE,LANGITUDE_VALUE,"");
                break;
            case R.id.codbtn:
                FROMVAL = "credit";
                showCreditPopup();
                /*Date Picker active with next 90 days from current date*/
         /*       DialogFragment fromfragment = new DatePickerFragment();
                fromfragment.show(getSupportFragmentManager(), "Date Picker");*/
                break;
            case R.id.chequebtn:
                FROMVAL = "cheque";
                showCreditPopup();
                break;
            default:
                break;
        }
    }

    public void showCreditPopup() {
        //	if(!globalShare.getSelectuserotp().equals("local")) {
        /** Used for Show Disclaimer Pop up screen */
        alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.layout_credittequestpopup, null);
        alertDialog.setContentView(layout);
        alertDialog.setCancelable(false);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        ImageView cancel = (ImageView) layout.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
            }
        });
        credits_spinner = layout.findViewById(R.id.credits_spinner);
        checkbox = layout.findViewById(R.id.checkbox);
        LinearLayout chequelinear = layout.findViewById(R.id.chequelinear);
        final EditText chequeedittext = layout.findViewById(R.id.chequeedittext);

        LinearLayout chequedatelinear = layout.findViewById(R.id.chequedatelinear);
        LinearLayout creditlnr = layout.findViewById(R.id.creditlnr);

        chequdateeedittext = layout.findViewById(R.id.chequdateeedittext);
        TextView termsandconditiontxt = layout.findViewById(R.id.termsandconditiontxt);
        TextView titletxt = layout.findViewById(R.id.titletxt);

//        termsandconditiontxt.setText(Html.fromHtml("I agree to the " +
//                "<a href='com.nova.b2b_dealer.TermsandConditionsActivity'>TERMS AND CONDITIONS</a>"));
//        termsandconditiontxt.setClickable(true);
//        termsandconditiontxt.setMovementMethod(LinkMovementMethod.getInstance());

        termsandconditiontxt.setText(R.string.agreetermsconditions);
        if (SHORTFORM.equals("SE")) {
            if (FROMVAL.equals("cheque")) {
                titletxt.setText("Enter cheque details");
                chequelinear.setVisibility(View.VISIBLE);
                chequedatelinear.setVisibility(View.VISIBLE);
                credits_spinner.setVisibility(View.GONE);
                creditlnr.setVisibility(View.GONE);
            } else {
                creditlnr.setVisibility(View.VISIBLE);
                checkbox.setVisibility(View.VISIBLE);
                termsandconditiontxt.setVisibility(View.VISIBLE);
                titletxt.setText("Select credit days");
                chequelinear.setVisibility(View.GONE);
                chequedatelinear.setVisibility(View.GONE);
                credits_spinner.setVisibility(View.VISIBLE);
                loadCredits();
            }
        } else {
            chequelinear.setVisibility(View.GONE);
            chequedatelinear.setVisibility(View.GONE);
            credits_spinner.setVisibility(View.VISIBLE);
            checkbox.setVisibility(View.VISIBLE);
            termsandconditiontxt.setVisibility(View.VISIBLE);
            creditlnr.setVisibility(View.VISIBLE);
            loadCredits();
        }

        termsandconditiontxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TermsandConditionsActivity.class);
                startActivity(intent);
            }
        });

        chequdateeedittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() == R.id.chequdateeedittext) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DateType", "fromDate");
                    DialogFragment fromfragment = new DatePickerFragment();
                    fromfragment.setArguments(bundle);
                    fromfragment.show(getSupportFragmentManager(), "Date Picker");
                }
                return true;
            }
        });

        popup_submit = (Button) layout.findViewById(R.id.popup_submit);
        popup_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SHORTFORM.equals("SE")) {
                    if (FROMVAL.equals("cheque")) {
                        String chequeno = chequeedittext.getText().toString().trim();
                        String cheque_dateval = chequdateeedittext.getText().toString().trim();
                        if (chequeno == null || "".equalsIgnoreCase(chequeno) || chequeno.equals("")
                                || cheque_dateval == null || "".equalsIgnoreCase(cheque_dateval) || cheque_dateval.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter cheque number and cheque date", Toast.LENGTH_SHORT).show();
                        } else {
                            checkoutCart("", "", "", LATITUDE_VALUE, LANGITUDE_VALUE, cheque_dateval, chequeno);
                        }
                    } else {
                        if (CREDIT_DAYS == null || "".equalsIgnoreCase(CREDIT_DAYS)
                                || CREDIT_DAYS.equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Please select credit", Toast.LENGTH_SHORT).show();
                        } else {
                            if (checkbox.isChecked()) {
                                checkoutCart("", "", "", LATITUDE_VALUE, LANGITUDE_VALUE, CREDIT_DAYS, "");
                            } else {
                                Toast.makeText(getApplicationContext(), "Please check Terms and Tonditions", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    if (CREDIT_DAYS == null || "".equalsIgnoreCase(CREDIT_DAYS)
                            || CREDIT_DAYS.equals("")) {
                        Toast.makeText(getApplicationContext(),
                                "Please select credit", Toast.LENGTH_SHORT).show();
                    } else {
                        if (checkbox.isChecked()) {
                            checkoutCart("", "", "", LATITUDE_VALUE, LANGITUDE_VALUE, CREDIT_DAYS, "");
                        } else {
                            Toast.makeText(getApplicationContext(), "Please check terms and conditions", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    public void loadCredits() {

        creditDatesList.clear();
        creditDatesList.add("7");
        creditDatesList.add("15");
        creditDatesList.add("25");
        creditDatesList.add("30");
        creditDatesList.add("45");
        creditDatesList.add("60");
        creditDatesList.add("90");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, creditDatesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        credits_spinner.setAdapter(spinnerClass);
        credits_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                CREDIT_DAYS = creditDatesList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
//            /*
//                add(int field, int value)
//                    Adds the given amount to a Calendar field.
//             */
//            // Add 3 days to Calendar
//            calendar.add(Calendar.DATE, 20000);
//
//            // Set the Calendar new date as maximum date of date picker
//            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
//
//          //   Subtract 6 days from Calendar updated date
//             calendar.add(Calendar.DATE, -20000);
//
//            // Set the Calendar new date as minimum date of date picker
//            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
            /*
                add(int field, int value)
                    Adds the given amount to a Calendar field.
             */
            // Add 3 days to Calendar
            calendar.add(Calendar.DATE, 20000);

            // Set the Calendar new date as maximum date of date picker
            dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            //   Subtract 6 days from Calendar updated date
            calendar.add(Calendar.DATE, -20000);

            // Set the Calendar new date as minimum date of date picker
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());

            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Do something with the chosen date
            //   TextView tv = (TextView) getActivity().findViewById(R.id.tv);

            // Create a Date variable/object with user chosen date

            int month = monthOfYear + 1;
            chequdateeedittext.setText(dayOfMonth + "-" + month + "-" + year);

//            Calendar cal = Calendar.getInstance();
//            cal.setTimeInMillis(0);
//            cal.set(year, month, day, 0, 0, 0);
//            Date chosenDate = cal.getTime();
//
//            // Format the date using style and locale
//            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
//            String formattedDate = df.format(chosenDate);
//            calval=1;
//            // Display the chosen date to app interface
//            chequdateeedittext.setText(formattedDate);


            //checkoutCart("","","",LATITUDE_VALUE,LANGITUDE_VALUE,formattedDate);
        }
    }

//    private static void checkoutCart(String address, String flat_no, String landmark, double latitude,
//                                     double longitude,String creditdate) {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(mmContext);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        String lat = String.valueOf(latitude);
//        String lon = String.valueOf(longitude);
//
//        Call<Checkout> mService = mApiService.checkoutCart(PRIMARYID, addressid, "", "", "", "",
//                FROMVAL,creditdate);
//        mService.enqueue(new Callback<Checkout>() {
//            @Override
//            public void onResponse(@NonNull Call<Checkout> call, @NonNull Response<Checkout> response) {
//                dialog.dismiss();
//                try {
//                    Checkout mcheckoutObject = response.body();
//                    assert mcheckoutObject != null;
//                    String status = mcheckoutObject.getStatus();
//                    String message=mcheckoutObject.getMessage();
//                    String orderId = mcheckoutObject.getOrder_id();
//                    String order_key = mcheckoutObject.getOrder_key();
//                    Log.e("checkoutstatus", "" + status);
//                    if (status.equals("1")) {
//
//                        if(alertDialog!=null)
//                        {
//                            alertDialog.dismiss();
//                        }
//
//
//
//
//                        if(ProductListActivity.mainfinish!=null)
//                        {
//                            ProductListActivity.mainfinish.finish();
//                        }
//                        if(CartListActivity.mainfinish!=null)
//                        {
//                            CartListActivity.mainfinish.finish();
//                        }
//                        if(DealerMenuScreenActivity.mainfinish!=null)
//                        {
//                            DealerMenuScreenActivity.mainfinish.finish();
//                        }
//
//                        if(ProductListActivityNew.mainfinish!=null)
//                        {
//                            ProductListActivityNew.mainfinish.finish();
//                        }
//                      //  globalShare.setStoreMenuSelectedPos("3");
//                        Intent intent = new Intent(mmContext, OrderSummaryActivity.class);
//                        intent.putExtra("orderId",orderId);
//                        intent.putExtra("order_key",order_key);
//                        mmContext.startActivity(intent);
//                    }
//                    //String message = mcheckoutObject.getMessage();
//                    Toast.makeText(mmContext, message, Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//
//                }
//            }
//            @Override
//            public void onFailure(Call<Checkout> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(mmContext, R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    public void checkoutCart(String address, String flat_no, String landmark, double latitude,
                             double longitude, String creditdate, String chequenumber) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        String lat = String.valueOf(latitude);
        String lon = String.valueOf(longitude);
        String REMARKS_VAL = remarkstxt.getText().toString().trim();

        Call<Checkout> mService = mApiService.checkoutCart(PRIMARYID,
                ADDRESSIDVAL, "", "", "", "",
                FROMVAL, creditdate, DEALERIDVAL, BRANCHID, chequenumber, TRANSPORT_ID, REMARKS_VAL);
        mService.enqueue(new Callback<Checkout>() {
            @Override
            public void onResponse(@NonNull Call<Checkout> call, @NonNull Response<Checkout> response) {
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    Checkout mcheckoutObject = response.body();
                    assert mcheckoutObject != null;
                    String status = mcheckoutObject.getStatus();
                    String message = mcheckoutObject.getMessage();
                    String orderId = mcheckoutObject.getOrder_id();
                    String order_key = mcheckoutObject.getOrder_key();

                    if (status.equals("1")) {
                        List<OrdersListDTO> orderListDTOS = mcheckoutObject.getOrdersListDTOList();
                        if (orderListDTOS != null) {
                            if (orderListDTOS.size() > 0) {
                                ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
                                ArrayList<String> order_idList = new ArrayList<>();
                                ArrayList<String> order_keyList = new ArrayList<>();
                                for (int i = 0; i < orderListDTOS.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String order_id = orderListDTOS.get(i).getOrder_id();
                                    String order_key1 = orderListDTOS.get(i).getOrder_key();
                                    order_idList.add(order_id);
                                    order_keyList.add(order_key1);

                                    hashMap.put("invoice_key", order_key1);
                                    hashMap.put("invoice_id", order_id);
                                    hashMapArrayList.add(hashMap);
                                }
                                if (order_idList != null) {
                                    if (order_idList.size() > 0) {
                                        if (order_idList.size() == 1) {
                                            if (SHORTFORM.equals("SE")) {
                                                SharedPreferences dealerpreferences = getSharedPreferences(
                                                        SharedDB.DEALERPREFERENCES, Context.MODE_PRIVATE);
                                                SharedPreferences.Editor dealereditor = dealerpreferences.edit();
                                                dealereditor.clear();
                                                dealereditor.commit();
                                                SharedDB.dealerclearAuthentication(UserPaymentActivity.this);
                                            }

                                            if (alertDialog != null) {
                                                alertDialog.dismiss();
                                            }

                                            if (ProductCategoriesActivity.mainfinish != null) {
                                                ProductCategoriesActivity.mainfinish.finish();
                                            }

                                            if (ProductListActivity.mainfinish != null) {
                                                ProductListActivity.mainfinish.finish();
                                            }
                                            if (CartListActivity.mainfinish != null) {
                                                CartListActivity.mainfinish.finish();
                                            }
                                            if (DealerMenuScreenActivity.mainfinish != null) {
                                                DealerMenuScreenActivity.mainfinish.finish();
                                            }
                                            if (DealerScreenActivity.mainfinish != null) {
                                                DealerScreenActivity.mainfinish.finish();
                                            }

                                            if (ProductListActivityNew.mainfinish != null) {
                                                ProductListActivityNew.mainfinish.finish();
                                            }

                                            globalShare.setInvoicesList(hashMapArrayList);
                                            Intent intent = new Intent(mmContext, OrderSummaryActivity.class);
                                            intent.putExtra("orderId", order_idList.get(0));
                                            intent.putExtra("order_key", order_keyList.get(0));
                                            mmContext.startActivity(intent);
                                        } else {
                                            if (SHORTFORM.equals("SE")) {
                                                SharedPreferences dealerpreferences = getSharedPreferences(
                                                        SharedDB.DEALERPREFERENCES, Context.MODE_PRIVATE);
                                                SharedPreferences.Editor dealereditor = dealerpreferences.edit();
                                                dealereditor.clear();
                                                dealereditor.commit();
                                                SharedDB.dealerclearAuthentication(UserPaymentActivity.this);
                                            }

                                            if (alertDialog != null) {
                                                alertDialog.dismiss();
                                            }

                                            if (ProductCategoriesActivity.mainfinish != null) {
                                                ProductCategoriesActivity.mainfinish.finish();
                                            }

                                            if (ProductListActivity.mainfinish != null) {
                                                ProductListActivity.mainfinish.finish();
                                            }
                                            if (CartListActivity.mainfinish != null) {
                                                CartListActivity.mainfinish.finish();
                                            }
                                            if (DealerMenuScreenActivity.mainfinish != null) {
                                                DealerMenuScreenActivity.mainfinish.finish();
                                            }
                                            if (DealerScreenActivity.mainfinish != null) {
                                                DealerScreenActivity.mainfinish.finish();
                                            }

                                            if (ProductListActivityNew.mainfinish != null) {
                                                ProductListActivityNew.mainfinish.finish();
                                            }
                                            //  globalShare.setStoreMenuSelectedPos("3");

                                            globalShare.setInvoicesList(hashMapArrayList);
                                            Intent intent = new Intent(mmContext, CheckoutInvoiceReportActivity.class);
                                            mmContext.startActivity(intent);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //String message = mcheckoutObject.getMessage();
                    Toast.makeText(mmContext, message, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Checkout> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(mmContext, R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        if (CartListActivity.mainfinish != null) {
            CartListActivity.mainfinish.finish();
        }
        Intent intent = new Intent(getApplicationContext(), CartListActivity.class);
        startActivity(intent);
    }

    public void getTrasportsList() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<TransportDTO> mService = mApiService.getTransportsList(BRANCHID);
        mService.enqueue(new Callback<TransportDTO>() {
            @Override
            public void onResponse(Call<TransportDTO> call, Response<TransportDTO> response) {
                Log.e("response", "" + response);
                TransportDTO mtransportObject = response.body();
                dialog.dismiss();
                try {
                    String status = mtransportObject.getStatus();
                    Log.e("status", "" + status);
                    if (status.equals("1")) {

                        List<TransportList> transportDTOLIST = mtransportObject.getTransportDTOS();
                        if (transportDTOLIST != null) {
                            if (transportDTOLIST.size() > 0) {
                                ArrayList<String> transport_NamesList = new ArrayList<>();
                                ArrayList<String> transport_IdsList = new ArrayList<>();

                                transport_NamesList.add("Select Transport");
                                ArrayList<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
                                for (int i = 0; i < transportDTOLIST.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<>();
                                    String id = transportDTOLIST.get(i).getId();
                                    String name = transportDTOLIST.get(i).getName();
                                    hashMap.put("id", id);
                                    hashMap.put("name", name);

                                    transport_NamesList.add(name);
                                    transport_IdsList.add(id);
                                    hashMapArrayList.add(hashMap);
                                }
                                transport_NamesList.add("Private");
                                transport_IdsList.add("0");
                                getTransportNames(hashMapArrayList, transport_NamesList, transport_IdsList);
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<TransportDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getTransportNames(ArrayList<HashMap<String, String>> hashMapArrayList,
                                  final ArrayList<String> transport_NamesList, final ArrayList<String> transport_IdsList) {
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, transport_NamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        transport_spinner.setAdapter(spinnerClass);
        transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    TRANSPORT_ID = "";
                } else {
                    TRANSPORT_ID = transport_IdsList.get(position - 1);
                    TRANSPORT_NAME = transport_NamesList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
}