package com.mrnovacrm.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.WalletCreditDetailsDTO;
import com.mrnovacrm.userprofile.EmployeeFullProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditDetailsActivity extends AppCompatActivity {

    TextView transctionid_txt,transactiondate_txt,creditamount_txt,creditdate_txt,creditdays_txt,delay_txt,
            follownername_txt,followerid_txt,orderid_txt;
    RecyclerView remarksrecyclerview;
    ImageView sendbtn;
    EditText remarksedittext;
    private String PRIMARYID;
    private String SHORTFORM;
    private String idval;
    LinearLayout followerlinearlayout;
    String FOLLOWERPRIMARYKEY="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }

        Bundle bundle=getIntent().getExtras();
        idval=bundle.getString("id");
        String transaction_no=bundle.getString("transaction_no");
        String transaction_date=bundle.getString("transaction_date");
        String order_id=bundle.getString("order_id");

        setTitle("Debit Details");
        setContentView(R.layout.activity_credit_details);

        orderid_txt=findViewById(R.id.orderid_txt);
        transctionid_txt=findViewById(R.id.transctionid_txt);
        transactiondate_txt=findViewById(R.id.transactiondate_txt);
        creditamount_txt=findViewById(R.id.creditamount_txt);
        creditdate_txt=findViewById(R.id.creditdate_txt);
        creditdays_txt=findViewById(R.id.creditdays_txt);
        delay_txt=findViewById(R.id.delay_txt);

        follownername_txt=findViewById(R.id.follownername_txt);
        followerid_txt=findViewById(R.id.followerid_txt);

        remarksrecyclerview=findViewById(R.id.remarksrecyclerview);
        sendbtn=findViewById(R.id.sendbtn);
        remarksedittext=findViewById(R.id.remarksedittext);
        followerlinearlayout=findViewById(R.id.followerlinearlayout);

        orderid_txt.setText(order_id);
        transctionid_txt.setText(transaction_no);
        transactiondate_txt.setText(transaction_date);

        followerlinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!FOLLOWERPRIMARYKEY.equals(""))
                {
                    Intent intent=new Intent(getApplicationContext(), EmployeeFullProfileActivity.class);
                    intent.putExtra("id",FOLLOWERPRIMARYKEY);
                    startActivity(intent);
                }
            }
        });

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(CreditDetailsActivity.this);
        if(isConnectedToInternet)
        {
            getOrdersDetailsList();
        }else{
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
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

    private void getOrdersDetailsList() {
        String from_date = "";
        String to_date = "";
        final TransparentProgressDialog dialog = new TransparentProgressDialog(CreditDetailsActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Order> mService = null;
        mService = mApiService.getWalletCreditDetails(PRIMARYID,idval);
        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);

                Order mOrderObject = response.body();
                String status = mOrderObject.getStatus();
                Log.e("ordersstatus", "" + status);
                try {
                    if (Integer.parseInt(status) == 1) {
                        List<WalletCreditDetailsDTO> ordersList = mOrderObject.getWalletCreditDetailsDTOList();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {
                                //  nodata_found_txt.setVisibility(View.INVISIBLE);
                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();
                                    String id = ordersList.get(i).getId();
                                    String user_id = ordersList.get(i).getUser_id();
                                    String user_role = ordersList.get(i).getUser_role();
                                    String prev_balance = ordersList.get(i).getPrev_balance();
                                    String amount = ordersList.get(i).getAmount();
                                    String reference_no = ordersList.get(i).getReference_no();
                                    String transaction_no = ordersList.get(i).getTransaction_no();
                                    String transaction_date = ordersList.get(i).getTransaction_date();
                                    String transaction_mode = ordersList.get(i).getTransaction_mode();
                                    String payment_mode = ordersList.get(i).getPayment_mode();
                                    String action_by = ordersList.get(i).getAction_by();
                                    String action_date = ordersList.get(i).getAction_date();
                                    String status1= ordersList.get(i).getStatus();
                                    String follower_id= ordersList.get(i).getFollower_id();
                                    String follower_name= ordersList.get(i).getFollower_name();
                                    String follower_pkey= ordersList.get(i).getFollower_pkey();

                                    String days_delay= ordersList.get(i).getDays_delay();
                                    String days_left= ordersList.get(i).getDays_left();

                                   // creditdays_txt.
                                    creditdays_txt.setText(days_left);
                                    delay_txt.setText(days_delay);

                                    creditamount_txt.setText(amount);
                                    creditdate_txt.setText(action_date);

                                    follownername_txt.setText(follower_name);
                                    followerid_txt.setText(follower_id);
                                    FOLLOWERPRIMARYKEY=follower_pkey;
                                }
                                showprofile(FOLLOWERPRIMARYKEY);
                            } else {

                                Toast.makeText(getApplicationContext(),"NO data found",Toast.LENGTH_SHORT).show();
                            }
                        }else {

                            Toast.makeText(getApplicationContext(),"NO data found",Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),"NO data found",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //     Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showprofile(final String FOLLOWERPRIMARYKEY)
    {

    }
}
