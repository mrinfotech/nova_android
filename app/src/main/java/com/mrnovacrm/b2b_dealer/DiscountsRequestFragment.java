package com.mrnovacrm.b2b_dealer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.adapter.DiscountRequestAdapter;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DiscountRequestRecordsDTO;
import com.mrnovacrm.model.DiscountRquestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by harish on 6/24/2019.
 */

public class DiscountsRequestFragment extends Fragment {

    RecyclerView recyclerView;

    List<DiscountRequestRecordsDTO> discountRecordList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_disocunts_request, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
/*        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),  ((LinearLayoutManager) mLayoutManager).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);*/

        getDiscountRequest();


        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getDiscountRequest();
        }
    }

    private void getDiscountRequest() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(getActivity());
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<DiscountRquestDTO> mService = mApiService.getDiscountsRequest(SharedDB.getUserName(getActivity()));

        mService.enqueue(new Callback<DiscountRquestDTO>() {
            @Override
            public void onResponse(@NonNull Call<DiscountRquestDTO> call, @NonNull Response<DiscountRquestDTO> response) {
                DiscountRquestDTO mstoresObject = response.body();
                dialog.dismiss();
                try {
                    Log.e("response", "" + response);
                    assert mstoresObject != null;
                    int status = mstoresObject.getStatus();
                    Log.e("status", "" + status);
                    if (status == 1) {

                        discountRecordList = mstoresObject.getRecordList();

                        DiscountRequestAdapter adapter = new DiscountRequestAdapter(getActivity(), discountRecordList);
                        recyclerView.setAdapter(adapter);


                    } else {

                        String message = mstoresObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DiscountRquestDTO> call, @NonNull Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}