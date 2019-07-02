package com.mrnovacrm.b2b_dealer;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_admin.AdminMenuScreenActivity;
import com.mrnovacrm.b2b_delivery_dept.DeliveryMenuScreenActivity;
import com.mrnovacrm.b2b_dispatch_dept.DispatchMenuScreenActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceDeptMenuScreenActivity;
import com.mrnovacrm.b2b_superadmin.SuperAdminMenuScrenActivity;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DiscountRequestRecordsDTO;
import com.mrnovacrm.model.ResultsDTO;

import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDiscountRequestActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    GlobalShare globalShare;
    private String SHORTFORM = "";
    private HashMap<String, String> values;

    TextView textCompany, textProduct, textDealer, textRequestBy, textHeader;
    Spinner statusSpinner;
    public static EditText fromDate, toDate, discountEditText;
    Button buttonSubmit, buttonReset;
    public static DiscountRequestRecordsDTO discountRequest;

    String[] statusData = {"Approved", "Rejected", "New", "Pending"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalShare = (GlobalShare) getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_edit_discount_request);

        View includedLayout = findViewById(R.id.include_actionbar);
        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        textHeader = includedLayout.findViewById(R.id.actionbarheadertxt);
        backimg.setOnClickListener(EditDiscountRequestActivity.this);

        textHeader.setText("Edit Discount Request");


        textCompany = findViewById(R.id.textCompanyName);
        textDealer = findViewById(R.id.textDealer);
        textProduct = findViewById(R.id.textProduct);
        textRequestBy = findViewById(R.id.textRequestBy);
        statusSpinner = findViewById(R.id.spinnerStatus);
        fromDate = findViewById(R.id.edtxt_fromdate);
        toDate = findViewById(R.id.edtxt_todate);
        discountEditText = findViewById(R.id.discount_txt);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonReset = findViewById(R.id.buttonReset);

        fromDate.setOnTouchListener(EditDiscountRequestActivity.this);
        toDate.setOnTouchListener(EditDiscountRequestActivity.this);

        buttonSubmit.setOnClickListener(this);
        buttonReset.setOnClickListener(this);

        try {
            if (SharedDB.isLoggedIn(getApplicationContext())) {
                values = SharedDB.getUserDetails(getApplicationContext());
                SHORTFORM = values.get(SharedDB.SHORTFORM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, statusData);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinnerArrayAdapter);


        if (discountRequest != null) {
            textCompany.setText(discountRequest.getCompanyName());
            textDealer.setText(discountRequest.getDealer());
            textProduct.setText(discountRequest.getItemName());
            textRequestBy.setText(discountRequest.getRequestBy());
            fromDate.setText(discountRequest.getStartDate());
            toDate.setText(discountRequest.getEndDate());
            discountEditText.setText(discountRequest.getDiscount());


            for (int i = 0; i < statusData.length; i++) {
                if (statusData[i].toLowerCase().equalsIgnoreCase(discountRequest.getStatus().toLowerCase())) {
                    statusSpinner.setSelection(i);
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_fromdate) {
            Bundle bundle = new Bundle();
            bundle.putString("DateType", "fromDate");
            DialogFragment fromfragment = new DiscountsFragment.DatePickerFragment();
            fromfragment.setArguments(bundle);
            assert getFragmentManager() != null;
            fromfragment.show(getSupportFragmentManager(), "Date Picker");
        } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && view.getId() == R.id.edtxt_todate) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("DateType", "toDate");
            DialogFragment tofragment = new DiscountsFragment.DatePickerFragment();
            tofragment.setArguments(bundle2);
            assert getFragmentManager() != null;
            tofragment.show(getSupportFragmentManager(), "Date Picker");
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backimg:
                finish();
                break;
            case R.id.buttonSubmit:
                sendDiscountRequest();
                break;
            case R.id.buttonReset:
                finish();
                break;
        }
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NonNull
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
                assert type != null;
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
                        fromDate.setText(year + "-" + month + "-" + dayOfMonth);

                    }
                };
        private DatePickerDialog.OnDateSetListener to_dateListener =
                new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        toDate.setText(year + "-" + month + "-" + dayOfMonth);
                    }
                };


        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        }
    }


    private void sendDiscountRequest() {

        final TransparentProgressDialog dialog = new TransparentProgressDialog(EditDiscountRequestActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();


        Call<ResultsDTO> mService = mApiService.editDiscountRequest(discountRequest.getCompany()
                , discountRequest.getDealer()
                , discountRequest.getProduct()
                , discountRequest.getRequestTo(), discountRequest.getRequestBy()
                , fromDate.getText().toString(), toDate.getText().toString()
                , discountEditText.getText().toString(), statusSpinner.getSelectedItem().toString());

        mService.enqueue(new Callback<ResultsDTO>() {
            @Override
            public void onResponse(@NonNull Call<ResultsDTO> call, @NonNull Response<ResultsDTO> response) {
                ResultsDTO mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        Toast.makeText(getApplicationContext(), mLoginObject.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultsDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                Log.e("Throwable", " :" + t.getMessage());
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void recallMethod() {
        if (DealerMenuScreenActivity.mainfinish != null) {
            DealerMenuScreenActivity.mainfinish.finish();
        }
        if (SHORTFORM.equals("DEALER") || SHORTFORM.equals("SE")) {
            globalShare.setDeliverymenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
            startActivity(intent);
        }

        if (FinanceDeptMenuScreenActivity.mainfinish != null) {
            FinanceDeptMenuScreenActivity.mainfinish.finish();
        }
        if (SHORTFORM.equals("FM")) {
            globalShare.setFinancemenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
            startActivity(intent);
        }

        if (DispatchMenuScreenActivity.mainfinish != null) {
            DispatchMenuScreenActivity.mainfinish.finish();
        }
        if (SHORTFORM.equals("PACKER")) {
            globalShare.setDispatchmenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), DispatchMenuScreenActivity.class);
            startActivity(intent);
        }

        if (DeliveryMenuScreenActivity.mainfinish != null) {
            DeliveryMenuScreenActivity.mainfinish.finish();
        }

        if (SHORTFORM.equals("DB")) {
            globalShare.setDeliverymenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), DeliveryMenuScreenActivity.class);
            startActivity(intent);
        }

        if (AdminMenuScreenActivity.mainfinish != null) {
            AdminMenuScreenActivity.mainfinish.finish();
        }

        if (SHORTFORM.equals("ADMIN")) {
            globalShare.setAdminmenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), AdminMenuScreenActivity.class);
            startActivity(intent);
        }

        if (SuperAdminMenuScrenActivity.mainfinish != null) {
            SuperAdminMenuScrenActivity.mainfinish.finish();
        }

        if (SHORTFORM.equals("SA")) {
            globalShare.setSuperadminmenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), SuperAdminMenuScrenActivity.class);
            startActivity(intent);
        }
        finish();
    }

}