package com.mrnovacrm.b2b_dealer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.WalletCreditDetailsDTO;
import com.mrnovacrm.model.WalletDTO;
import com.mrnovacrm.wallet.ChequestatusListActivity;
import com.mrnovacrm.wallet.StatementListActivity;
import com.mrnovacrm.wallet.WalletCreditListActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerWalletFragment extends Fragment implements View.OnClickListener {
    LinearLayout credit_linear, debit_linear, checquestaus_linear, statement_linear;
    String PRIMARYID = "";
    String SHORTFORM;
    TextView availablebalace_txt;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_dealerwallet, container, false);
        availablebalace_txt = rootView.findViewById(R.id.availablebalace_txt);
        credit_linear = rootView.findViewById(R.id.credit_linear);
        debit_linear = rootView.findViewById(R.id.debit_linear);
        checquestaus_linear = rootView.findViewById(R.id.checquestaus_linear);
        statement_linear = rootView.findViewById(R.id.statement_linear);

        credit_linear.setOnClickListener(DealerWalletFragment.this);
        debit_linear.setOnClickListener(DealerWalletFragment.this);
        checquestaus_linear.setOnClickListener(DealerWalletFragment.this);
        statement_linear.setOnClickListener(DealerWalletFragment.this);
        statement_linear.setOnClickListener(DealerWalletFragment.this);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
        }

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getActivity());
        if(isConnectedToInternet)
        {
            getWalletBalance();
        }else {
             Toast.makeText(getActivity(),R.string.networkerror,Toast.LENGTH_SHORT);
        }
        deleteInvoices();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.credit_linear:
                Intent intent=new Intent(getActivity(), WalletCreditListActivity.class);
                intent.putExtra("title","Credit");
                startActivity(intent);
                break;
            case R.id.debit_linear:
                Intent intent1=new Intent(getActivity(), WalletCreditListActivity.class);
                intent1.putExtra("title","Debit");
                startActivity(intent1);
                break;
            case R.id.checquestaus_linear:
                Intent intent2=new Intent(getActivity(), ChequestatusListActivity.class);
                intent2.putExtra("title","Cheque List");
                startActivity(intent2);
                break;
            case R.id.statement_linear:
                Intent intent3=new Intent(getActivity(), StatementListActivity.class);
                intent3.putExtra("title","Statement");
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    private void getWalletBalance() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<WalletDTO> mService = null;
            mService = mApiService.getWalletBalance(PRIMARYID);
        mService.enqueue(new Callback<WalletDTO>() {
            @Override
            public void onResponse(Call<WalletDTO> call, Response<WalletDTO> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                WalletDTO mOrderObject = response.body();
                try {
                    String status = mOrderObject.getStatus();
                    if (Integer.parseInt(status) == 1) {
                        List<WalletCreditDetailsDTO> walletCreditDetailsDTOList=mOrderObject.getWalletCreditDetailsDTOList();
                        if(walletCreditDetailsDTOList!=null)
                        {
                            if(walletCreditDetailsDTOList.size()>0)
                            {
                                for(int i=0;i<walletCreditDetailsDTOList.size();i++)
                                {
                                    String grade_amount=walletCreditDetailsDTOList.get(i).getGrade_amount();
                                    availablebalace_txt.setText("Rs. "+grade_amount+" /-");
                                }
                            }
                        }

                    } else {
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<WalletDTO> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
            }
        });
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