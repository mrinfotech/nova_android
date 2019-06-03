package com.mrnovacrm.wallet;

//public class ChequeDetailsActivity {
//}

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Order;
import com.mrnovacrm.model.WalletCreditDetailsDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChequeDetailsActivity extends AppCompatActivity {

    TextView chequenumber_txt, chequeamount_txt, chequedepositdate_txt, chequepass_txt, bankname_txt, ifscccode_txt,
            branch_txt, dealercode_txt, companyname_txt,bankaccountname_txt,bankaccountnumber_txt,orderid_txt;
    RecyclerView remarksrecyclerview;
    EditText remarksedittext;
    ImageView sendbtn;
    Context mContext;
    private String PRIMARYID;
    private String SHORTFORM;
    private String idval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        idval = bundle.getString("id");
        String order_id = bundle.getString("order_id");

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }

        setTitle("Cheque details");
        setContentView(R.layout.activity_cheque_details);

        orderid_txt = findViewById(R.id.orderid_txt);
        chequenumber_txt = findViewById(R.id.chequenumber_txt);
        chequeamount_txt = findViewById(R.id.chequeamount_txt);
        chequedepositdate_txt = findViewById(R.id.chequedepositdate_txt);
        chequepass_txt = findViewById(R.id.chequepass_txt);

        bankname_txt = findViewById(R.id.bankname_txt);
        ifscccode_txt = findViewById(R.id.ifscccode_txt);
        branch_txt = findViewById(R.id.branch_txt);
        bankaccountname_txt = findViewById(R.id.bankaccountname_txt);
        bankaccountnumber_txt = findViewById(R.id.bankaccountnumber_txt);

        dealercode_txt = findViewById(R.id.dealercode_txt);
        companyname_txt = findViewById(R.id.companyname_txt);

        remarksrecyclerview = findViewById(R.id.remarksrecyclerview);
        remarksedittext = findViewById(R.id.remarksedittext);
        sendbtn = findViewById(R.id.sendbtn);

        orderid_txt.setText(order_id);
        getChequeDetais();
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

    private void getChequeDetais() {
        String from_date = "";
        String to_date = "";
        final TransparentProgressDialog dialog = new TransparentProgressDialog(mContext);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();

        Call<Order> mService = null;
        mService = mApiService.getWalletChequeDetails(PRIMARYID,idval);
        mService.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Order mOrderObject = response.body();
                    String status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);
                    if (Integer.parseInt(status) == 1) {
                        List<WalletCreditDetailsDTO> ordersList = mOrderObject.getWalletCreditDetailsDTOList();
                        if (ordersList != null) {
                            if (ordersList.size() > 0) {

                                ArrayList<HashMap<String, String>> hashmapList = new ArrayList<HashMap<String, String>>();
                                for (int i = 0; i < ordersList.size(); i++) {
                                    HashMap<String, String> hashMap = new HashMap<String, String>();

                                    String cheque_no = ordersList.get(i).getCheque_no();
                                    String amount = ordersList.get(i).getAmount();
                                    String cheque_status = ordersList.get(i).getCheque_status();
                                    String bank_name = ordersList.get(i).getBank_name();
                                    String account_name = ordersList.get(i).getAccount_name();
                                    String account_number = ordersList.get(i).getAccount_number();
                                    String dealer_code = ordersList.get(i).getDealer_code();
                                    String company_name = ordersList.get(i).getCompany_name();
                                    String cheque_deposit_date = ordersList.get(i).getCheque_deposit_date();


                                    chequenumber_txt.setText(cheque_no);
                                    chequeamount_txt.setText("Rs. "+amount+" /-");
                                    chequepass_txt.setText(cheque_status);

                                    bankname_txt.setText(bank_name);
                                    bankaccountnumber_txt.setText(account_number);
                                    bankaccountname_txt.setText(account_name);
                                    dealercode_txt.setText(dealer_code);
                                    companyname_txt.setText(company_name);
                                    chequedepositdate_txt.setText(cheque_deposit_date);

//                                    hashMap.put("cheque_no", cheque_no);
//                                    hashMap.put("amount", amount);
//                                    hashMap.put("cheque_status", cheque_status);
//                                    hashMap.put("bank_name", bank_name);
//                                    hashMap.put("account_name", account_name);
//                                    hashMap.put("account_number", account_number);
//                                    hashMap.put("dealer_code", dealer_code);
//                                    hashMap.put("company_name", company_name);
//                                    hashmapList.add(hashMap);
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
            public void onFailure(Call<Order> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //     Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

