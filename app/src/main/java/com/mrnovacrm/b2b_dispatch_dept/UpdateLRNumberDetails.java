package com.mrnovacrm.b2b_dispatch_dept;

//public class UpdateLRNumberDetails {
//}
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Track;
import com.mrnovacrm.model.TransportDetailsDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateLRNumberDetails extends AppCompatActivity implements View.OnClickListener {

    public static Activity mainfinish;
    String ID,OID, ORDERID;

    TextView date;
    TextView lpnumber, vehiclenumber, drivernameedittxt, paid, topay,
            phonenumberedittext, torouteedittext, fromrouteedittext, drivermobileedittxt,transporttypetxt;
    LinearLayout drivernamelinear, drivermobilelinear, vehiclnumberlinear, lpnumberlinear;
    TextView ordernumbertxt,storenametxtval;
    Button submitbtn;
    EditText lrnumber_edittext;
    Context mContext;
    private String USERID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        mContext=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainfinish = this;
        setTitle("Update LR number");
        setContentView(R.layout.layout_updatelrnumber);

        storenametxtval=findViewById(R.id.storenametxtval);

        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getString("id");
            OID = getIntent().getExtras().getString("oid");
            ORDERID = getIntent().getExtras().getString("orderId");
        }

        HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
        USERID = values.get(SharedDB.PRIMARYID);


        transporttypetxt = findViewById(R.id.transporttypetxt);
        ordernumbertxt = findViewById(R.id.ordernumbertxt);
        ordernumbertxt.setText("Order id : "+ORDERID);
        lpnumber = findViewById(R.id.lpnumber);
        vehiclenumber = findViewById(R.id.vehiclenumber);
        drivernameedittxt = findViewById(R.id.drivernameedittxt);
        drivermobileedittxt = findViewById(R.id.drivermobileedittxt);
        date = findViewById(R.id.date);
        paid = findViewById(R.id.paid);
        topay = findViewById(R.id.topay);
        phonenumberedittext = findViewById(R.id.phonenumberedittext);

        drivernamelinear = findViewById(R.id.drivernamelinear);
        vehiclnumberlinear = findViewById(R.id.vehiclnumberlinear);
        lpnumberlinear = findViewById(R.id.lpnumberlinear);
        drivermobilelinear = findViewById(R.id.drivermobilelinear);
        fromrouteedittext = findViewById(R.id.fromrouteedittext);
        torouteedittext = findViewById(R.id.torouteedittext);
        submitbtn = findViewById(R.id.submitbtn);
        lrnumber_edittext = findViewById(R.id.lrnumber_edittext);
        submitbtn.setOnClickListener(UpdateLRNumberDetails.this);
        boolean isConnectedToInternet = CheckNetWork.isConnectedToInternet(UpdateLRNumberDetails.this);
        if(isConnectedToInternet) {
            trackOrderDetails();
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backimg:
                finish();
                break;
            case R.id.submitbtn:
                String LRNUMBER=lrnumber_edittext.getText().toString().trim();
                if(LRNUMBER == null || "".equalsIgnoreCase(LRNUMBER) || LRNUMBER.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter lr number",Toast.LENGTH_SHORT).show();
                }else{
                    updatelrnumber();
                }
                break;
            default:
                break;
        }
    }
    private void trackOrderDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(UpdateLRNumberDetails.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Track> mService = mApiService.trackDeliveryDetails(OID);
        mService.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {Track mTrackObject = response.body();
                    Log.e("mOrderObject", "" + mTrackObject);
                    String status = mTrackObject.getStatus();
                    Log.e("status", "" + status);
                    String order_status = mTrackObject.getOrder_status();
                    Log.e("orderstatus", " :" + order_status);

                    if (Integer.parseInt(status) == 1) {
                        List<TransportDetailsDTO> ordersList = mTrackObject.getTransportDetailsDTOS();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < ordersList.size(); i++) {
                                    String from_route1=ordersList.get(i).getFrom_route();
                                    String to_route1=ordersList.get(i).getTo_route();
                                    String paid1=ordersList.get(i).getPaid();
                                    String amount1=ordersList.get(i).getAmount();
                                    String vechicle_number1=ordersList.get(i).getVechicle_number();
                                    String driver_number1=ordersList.get(i).getDriver_number();
                                    String driver_name1=ordersList.get(i).getDriver_name();
                                    String lr_no1=ordersList.get(i).getLr_no();
                                    String contact_no1=ordersList.get(i).getContact_no();
                                    String transport_type1=ordersList.get(i).getTransport_type();
                                    String emp_name1=ordersList.get(i).getEmp_name();
                                    String transoportname1=ordersList.get(i).getTransport_name();
                                    String estimation_time1=ordersList.get(i).getEstimation_time();
                                    String contact1=ordersList.get(i).getContact();
                                    String company_name=ordersList.get(i).getCompany_name();

                                    storenametxtval.setText(company_name);
                                    if(transport_type1.equals("private"))
                                    {
                                        drivernamelinear.setVisibility(View.VISIBLE);
                                        drivermobilelinear.setVisibility(View.VISIBLE);
                                        vehiclnumberlinear.setVisibility(View.VISIBLE);
                                        lpnumberlinear.setVisibility(View.VISIBLE);
                                    }else{
                                        drivernamelinear.setVisibility(View.GONE);
                                        drivermobilelinear.setVisibility(View.GONE);
                                        vehiclnumberlinear.setVisibility(View.GONE);
                                        lpnumberlinear.setVisibility(View.VISIBLE);
                                    }

                                    transporttypetxt.setText(transport_type1);
                                    lpnumber.setText(lr_no1);
                                    vehiclenumber.setText(vechicle_number1);
                                    drivernameedittxt.setText(driver_name1);
                                    drivermobileedittxt.setText(driver_number1);
                                    fromrouteedittext.setText(from_route1);
                                    torouteedittext.setText(to_route1);
                                    date.setText(estimation_time1);
                                    paid.setText(paid1);
                                    topay.setText(amount1);
                                    phonenumberedittext.setText(contact1);

                                    if(lr_no1!=null)
                                    {
                                        if(!lr_no1.equals(""))
                                        {
                                            lrnumber_edittext.setText(lr_no1);
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "No details found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "No details found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
             //   Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.search:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
public void updatelrnumber()
{
    final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
    dialog.show();
    RetrofitAPI mApiService = SharedDB.getInterfaceService();
    String LRNUMBER=lrnumber_edittext.getText().toString().trim();

    Call<Login> mService = mApiService.updateLRnumber(USERID, LRNUMBER,ID);
    mService.enqueue(new Callback<Login>() {
        @Override
        public void onResponse(@NonNull Call<Login> call, @NonNull Response<Login> response) {
            Login mLoginObject = response.body();
            Log.e("response", " :" + response);
            dialog.dismiss();
            try {
                String status = mLoginObject.getStatus();
                String message = mLoginObject.getMessage();
                if (status.equals("1")) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    trackOrderDetails();
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onFailure(Call<Login> call, Throwable t) {
            call.cancel();
            dialog.dismiss();
           // Log.e("Throwable", " :" + t.getMessage());
         //   Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
        }
    });




}

}