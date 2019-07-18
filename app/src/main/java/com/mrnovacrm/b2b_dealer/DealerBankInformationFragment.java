package com.mrnovacrm.b2b_dealer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.BankAccountRecordsDTO;
import com.mrnovacrm.model.BankAccountRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Harish on 9/07/2019.
 */

public class DealerBankInformationFragment extends Fragment {


    TextView textDealerCode, textAccountName, textAccountNumber;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_dealer_bank, container, false);

        textDealerCode = rootView.findViewById(R.id.textDealerCode);
        textAccountName = rootView.findViewById(R.id.textAccountName);
        textAccountNumber = rootView.findViewById(R.id.textAccountNumber);

        getBankData();

        return rootView;
    }

    private void getBankData() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<BankAccountRequestDTO> mService = mApiService.getAccountDetails(SharedDB.getUserName(getActivity()));

        mService.enqueue(new Callback<BankAccountRequestDTO>() {
            @Override
            public void onResponse(@NonNull Call<BankAccountRequestDTO> call, @NonNull Response<BankAccountRequestDTO> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                BankAccountRequestDTO mOrderObject = response.body();
                try {
                    assert mOrderObject != null;
                    int status = mOrderObject.getStatus();
                    Log.e("ordersstatus", "" + status);
                    if (status == 1) {
                        List<BankAccountRecordsDTO> ordersList = mOrderObject.getRecordList();
                        if (ordersList != null) {

                            textDealerCode.setText(ordersList.get(0).getCustId());
                            textAccountName.setText(ordersList.get(0).getUserId());
                            textAccountNumber.setText(ordersList.get(0).getAccNum());

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BankAccountRequestDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
            }
        });
    }

}