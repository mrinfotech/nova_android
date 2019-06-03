package com.mrnovacrm.b2b_dealer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.Track;
import com.mrnovacrm.model.TrackOrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 3/7/2018.
 */

public class TrackOrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Button cancel_order, view_map;
    private Dialog alertDialog;
    private View layout;
    private TextView submittxt, status_ordered,status_transferred, status_accepted, status_shipped, status_delivered,status_picked;
    private ImageView closeicon;
    EditText cancelorder_txt;
    String ID, ORDERID, reason;
    String orderstatus, changedon, comments;
    LinearLayout order_details;
    TextView text_orderid, text_cancelled, cancelled_reason;
    ImageView orderimageview,transferredImageview;
    RelativeLayout trackrelative;
    private ImageView acceptedimageview,deliveredimageview,shiftedimageview,pickedimageview;
    private String STATUSVAL="";
    GlobalShare globalShare;
    private Button view_deliverydetails;
    public static Activity mainfinish;
    LinearLayout transferredlinear;

    private String SHORTFORMVAL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_track_order_details);
        globalShare=(GlobalShare)getApplicationContext();
        if (getIntent().getExtras() != null) {
            ID = getIntent().getExtras().getString("id");
            ORDERID = getIntent().getExtras().getString("orderId");
            STATUSVAL = getIntent().getExtras().getString("status");
        }
        View includedLayout = findViewById(R.id.include_actionbar);
        TextView actionbarheadertxt = includedLayout.findViewById(R.id.actionbarheadertxt);
        actionbarheadertxt.setText("Track ID: " + ORDERID);
        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        backimg.setOnClickListener(TrackOrderDetailsActivity.this);
        cancel_order = findViewById(R.id.cancel_order);
        view_map = findViewById(R.id.view_map);
    //    view_map.setVisibility(View.GONE);
        view_map.setOnClickListener(this);

//        view_deliverydetails = findViewById(R.id.view_deliverydetails);
//        view_deliverydetails.setOnClickListener(this);

        cancel_order.setOnClickListener(TrackOrderDetailsActivity.this);

        transferredlinear = findViewById(R.id.transferredlinear);

        status_ordered = findViewById(R.id.status_ordered);
        status_transferred = findViewById(R.id.status_transferred);
        status_accepted = findViewById(R.id.status_accepted);
        status_picked = findViewById(R.id.status_picked);
        status_shipped = findViewById(R.id.status_shipped);
        status_delivered = findViewById(R.id.status_delivered);

        order_details = findViewById(R.id.order_details);
        text_orderid = findViewById(R.id.text_orderid);
        text_orderid.setText("Track ID: " + ORDERID);
        text_cancelled = findViewById(R.id.text_cancelled);
        cancelled_reason = findViewById(R.id.cancelled_reason);

        orderimageview=findViewById(R.id.orderimageview);
        transferredImageview=findViewById(R.id.transferredImageview);
        acceptedimageview=findViewById(R.id.acceptedimageview);
        shiftedimageview=findViewById(R.id.shiftedimageview);
        pickedimageview=findViewById(R.id.pickedimageview);
        deliveredimageview=findViewById(R.id.deliveredimageview);
        trackrelative=findViewById(R.id.trackrelative);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            SHORTFORMVAL = values.get(SharedDB.SHORTFORM);
        }
//        if(STATUSVAL.equals("Ordered"))
//        {
//            cancel_order.setVisibility(View.VISIBLE);
//        }else{
//            cancel_order.setVisibility(View.GONE);
//        }

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getApplicationContext());
        if(isConnectedToInternet)
        {
            trackOrderDetails();
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       // trackOrderDetails();
    }

    public void showCancelOrderPopup() {
        /** Used for Show Disclaimer Pop up screen */
        alertDialog = new Dialog(TrackOrderDetailsActivity.this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        LayoutInflater inflater = this.getLayoutInflater();
        layout = inflater.inflate(R.layout.layout_cancelorderpopup, null);
        alertDialog.setContentView(layout);
        alertDialog.setCancelable(true);
        if (!alertDialog.isShowing()) {
            alertDialog.show();
        }
        submittxt = layout.findViewById(R.id.submittxt);
        cancelorder_txt = layout.findViewById(R.id.cancelorder_txt);

        closeicon = layout.findViewById(R.id.closeicon);
        closeicon.setOnClickListener(TrackOrderDetailsActivity.this);
        submittxt.setOnClickListener(TrackOrderDetailsActivity.this);
    }

    private void trackOrderDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(TrackOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
     //   Log.e("ID", "123 :" + ID);
        Call<Track> mService = mApiService.trackOrderDetails(ID);

        mService.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    Track mTrackObject = response.body();
                    String status = mTrackObject.getStatus();

                    if (Integer.parseInt(status) == 1) {
                        Log.e("status", "" + status);
                        String order_status = mTrackObject.getOrder_status();
                        String is_transfered = mTrackObject.getIs_transfered();

//                        if(is_transfered.equals("0"))
//                        {
//                            cancel_order.setVisibility(View.VISIBLE);
//                        }else{
//                            cancel_order.setVisibility(View.GONE);
//                        }

                        if(SHORTFORMVAL.equals("SE"))
                        {
                            cancel_order.setVisibility(View.GONE);
                        }else {
                            if (order_status.equals("Ordered") && is_transfered.equals("0")) {
                                cancel_order.setVisibility(View.VISIBLE);
                            } else {
                                cancel_order.setVisibility(View.GONE);
                            }
                        }

                        List<TrackOrderStatus> ordersList = mTrackObject.getTrackOrderStatus();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    orderstatus = ordersList.get(i).getOrder_status();
                                    changedon = ordersList.get(i).getChanged_on();
                                    comments = ordersList.get(i).getComments();
                                    trackrelative.setVisibility(View.VISIBLE);

                                    switch (orderstatus) {
                                        case "Ordered":
                                            status_ordered.setText("Ordered : " + (changedon));
                                            status_ordered.setTextColor(getResources().getColor(R.color.black));
                                            orderimageview.setImageResource(R.drawable.trackorder_greencircle);
                                            break;

                                        case "Transferred":
                                            transferredlinear.setVisibility(View.VISIBLE);
                                            status_transferred.setText("Transferred : " + (changedon));
                                            status_transferred.setTextColor(getResources().getColor(R.color.black));
                                            transferredImageview.setImageResource(R.drawable.trackorder_greencircle);
                                            break;

                                        case "Cancelled":
                                            text_cancelled.setText("Cancelled on : " + changedon);
                                            cancelled_reason.setText("Reason : " + comments);
                                            text_cancelled.setTextColor(getResources().getColor(R.color.black));
                                            cancelled_reason.setTextColor(getResources().getColor(R.color.black));
                                            break;

                                        case "Packed":
                                            status_picked.setText("Packed : " + (changedon));
                                            status_picked.setTextColor(getResources().getColor(R.color.black));
                                            pickedimageview.setImageResource(R.drawable.trackorder_greencircle);
                                            break;

                                        case "Accepted":
                                            status_accepted.setText("Accepted : " + (changedon));
                                            status_accepted.setTextColor(getResources().getColor(R.color.black));
                                            acceptedimageview.setImageResource(R.drawable.trackorder_greencircle);
                                            break;

                                        case "Dispatched":
                                            status_shipped.setText("Dispatched : " + (changedon));
                                            status_shipped.setTextColor(getResources().getColor(R.color.black));
                                            shiftedimageview.setImageResource(R.drawable.trackorder_greencircle);
                                            view_map.setVisibility(View.VISIBLE);
                                            break;

                                        case "Delivered":
                                            status_delivered.setText("Delivered : " + (changedon));
                                            status_delivered.setTextColor(getResources().getColor(R.color.black));
                                            deliveredimageview.setImageResource(R.drawable.trackorder_greencircle);
                                            view_map.setVisibility(View.VISIBLE);
                                            break;

                                        case "Rejected":
                                            status_delivered.setText("Rejected : " + (changedon));
                                            status_delivered.setTextColor(getResources().getColor(R.color.black));
                                            deliveredimageview.setImageResource(R.drawable.trackorder_greencircle);
                                            view_map.setVisibility(View.VISIBLE);
                                            break;

                                            default:
                                                break;
                                    }
                                    hashmapList.add(hashMap);
                                }
                            } else {
                                status_ordered.setTextColor(getResources().getColor(R.color.black));
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
              //  Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.backimg:
                finish();
                break;
            case R.id.cancel_order:
                showCancelOrderPopup();
                break;
            case R.id.closeicon:
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                break;
            case R.id.submittxt:
                reason = cancelorder_txt.getText().toString().trim();
                if (reason == null || "".equalsIgnoreCase(reason)
                        || reason.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter reason",
                            Toast.LENGTH_SHORT).show();
                } else {
                    boolean isConnectedToInternet = CheckNetWork
                            .isConnectedToInternet(TrackOrderDetailsActivity.this);
                    if (isConnectedToInternet) {
                        cancelOrder(reason);
                    } else {
                        Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.view_map:
                boolean isConnectedToInternet = CheckNetWork
                        .isConnectedToInternet(getApplicationContext());
                if(isConnectedToInternet)
                {
                    Intent intent = new Intent(getApplicationContext(), TrackDeliveryDetailsActivity.class);
                    intent.putExtra("orderId", ORDERID);
                    intent.putExtra("id", ID);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    private void cancelOrder(String reason) {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(TrackOrderDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Log.e("ID", "123 :" + ID);
        Call<Login> mService = mApiService.cancelOrder(ID, reason);

        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dialog.dismiss();
                try {
                    Login mObject = response.body();
                    Log.e("mOrderObject", "" + mObject);
                    String status = mObject.getStatus();
                    Log.e("status", "" + status);
                    String message = mObject.getMessage();
                    if (Integer.parseInt(status) == 1) {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                     if (alertDialog != null) {
                         alertDialog.dismiss();
                         }
                         globalShare.setCancelorderfrom("track");
                        finish();

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
               // Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
