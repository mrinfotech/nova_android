package com.mrnovacrm.b2b_dealer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Track;
import com.mrnovacrm.model.TransportDetailsDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackDeliveryDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity mainfinish;
    String ID, ORDERID;

    TextView date;
    TextView lpnumber, vehiclenumber, drivernameedittxt, paid, topay,
            phonenumberedittext, torouteedittext, fromrouteedittext, drivermobileedittxt,transporttypetxt;
    LinearLayout drivernamelinear, drivermobilelinear, vehiclnumberlinear, lpnumberlinear;
    TextView ordernumbertxt,storenametxtval;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainfinish = this;
        setTitle("Transport details");
        setContentView(R.layout.layout_delivertransportdetails);

        storenametxtval=findViewById(R.id.storenametxtval);

        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getString("id");
            ORDERID = getIntent().getExtras().getString("orderId");
        }

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
        trackOrderDetails();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backimg:
                finish();
                break;
            default:
                break;
        }
    }


    private void trackOrderDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(TrackDeliveryDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Track> mService = mApiService.trackDeliveryDetails(ID);

        mService.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                dialog.dismiss();
                Log.e("response", "" + response);

                Track mTrackObject = response.body();
                Log.e("mOrderObject", "" + mTrackObject);
                String status = mTrackObject.getStatus();
                Log.e("status", "" + status);
                String order_status = mTrackObject.getOrder_status();
                Log.e("orderstatus", " :" + order_status);
                try {
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
                                        lpnumberlinear.setVisibility(View.GONE);
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
                                }
                            } else {
                            }
                        }
                    } else {

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
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


}