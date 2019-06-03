package com.mrnovacrm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_dealer.FinanceTransportOrderDetailsActivity;
import com.mrnovacrm.b2b_dealer.InvoiceDetailsActivity;
import com.mrnovacrm.b2b_dealer.NotificationStatementActivity;
import com.mrnovacrm.b2b_dealer.OrderDetailsActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceOrderDetailsActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.NotificatonDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prasad on 4/19/2018.
 */

public class NotificationFragment extends Fragment {

    Context context;
    private String PRIMARYID;
    private String SHORTFORM;
    private String BRANCHID;
    private RecyclerView recyclerView;
    TextView nodatafound_txt;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_notifications, container, false);
        context = getActivity();

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            SHORTFORM = values.get(SharedDB.SHORTFORM);
            BRANCHID = values.get(SharedDB.BRANCHID);
        }

        recyclerView = rootView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        nodatafound_txt = rootView.findViewById(R.id.nodatafound_txt);
        getNotificationsWithRetrofit();
        return rootView;
    }

    private void getNotificationsWithRetrofit() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(context);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.getNotificationList(PRIMARYID, BRANCHID, SHORTFORM);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Log.e("response", "" + response);
                Log.e("response body", "" + response.body());
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    if (status.equals("1")) {
                        String message = mLoginObject.getMessage();
                        List<NotificatonDTO> notificationList = mLoginObject.getNotificatonDTOList();
                        if (notificationList != null) {
                            if (notificationList.size() > 0) {
                                nodatafound_txt.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                ArrayList<HashMap<String, String>> hashmaplist = new ArrayList<>();
                                for (int i = 0; i < notificationList.size(); i++) {

                                    HashMap<String, String> hashMap = new HashMap<>();
                                    String id = notificationList.get(i).getId();
                                    String notification = notificationList.get(i).getNotification();
                                    String user_role = notificationList.get(i).getUser_role();
                                    String user_id = notificationList.get(i).getUser_id();
                                    String branch = notificationList.get(i).getBranch();
                                    String is_seen = notificationList.get(i).getIs_seen();
                                    String notifiy_on = notificationList.get(i).getNotifiy_on();
                                    String notify_type = notificationList.get(i).getNotify_type();
                                    String related_id = notificationList.get(i).getRelated_id();
                                    String notifiy_date = notificationList.get(i).getNotifiy_date();

                                    hashMap.put("id", id);
                                    hashMap.put("notification", notification);
                                    hashMap.put("user_role", user_role);
                                    hashMap.put("user_id", user_id);
                                    hashMap.put("branch", branch);
                                    hashMap.put("is_seen", is_seen);
                                    hashMap.put("notifiy_on", notifiy_on);
                                    hashMap.put("notify_type", notify_type);
                                    hashMap.put("related_id", related_id);
                                    hashMap.put("notifiy_date", notifiy_date);

                                    hashmaplist.add(hashMap);
                                }
                                showNotificationsList(hashmaplist);
                            } else {
                                nodatafound_txt.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        String message = mLoginObject.getMessage();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showNotificationsList(ArrayList<HashMap<String, String>> hashmaplist) {
        NotificationRecyclerAdapter adapter = new NotificationRecyclerAdapter(getActivity(), hashmaplist);
        recyclerView.setAdapter(adapter);
    }

    public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder> {
        Context context;
        ArrayList<HashMap<String, String>> hashmapList;

        public NotificationRecyclerAdapter(Context context, ArrayList<HashMap<String, String>> hashmapList) {
            this.context = context;
            this.hashmapList = hashmapList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notificaitonadapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            //    String title=hashmapList.get(position).get("title");
            String notification = hashmapList.get(position).get("notification");
            String notifiy_on = hashmapList.get(position).get("notifiy_on");

            // holder.notificationtxt.setText(title);
            holder.greviencedesc.setText(notification);
            holder.notificationontxt.setText(notifiy_on);

        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView notificationtxt, greviencedesc, notificationontxt;

            public MyViewHolder(View itemView) {
                super(itemView);
                notificationtxt = itemView.findViewById(R.id.notificationtxt);
                greviencedesc = itemView.findViewById(R.id.greviencedesc);
                notificationontxt = itemView.findViewById(R.id.notificationontxt);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                int pos = getAdapterPosition();
                String id = hashmapList.get(pos).get("id");
                String orderId = hashmapList.get(pos).get("orderId");
                //String status = hashmapList.get(pos).get("status");
                String related_id = hashmapList.get(pos).get("related_id");
                String notify_type = hashmapList.get(pos).get("notify_type");
                String user_role = hashmapList.get(pos).get("user_role");
                String user_id = hashmapList.get(pos).get("user_id");
                String notifiy_date = hashmapList.get(pos).get("notifiy_date");
                String branch = hashmapList.get(pos).get("branch");

                Log.e("notify_type",notify_type);

                boolean isConnectedToInternet = CheckNetWork
                        .isConnectedToInternet(getActivity());
                if (isConnectedToInternet) {
                    if(SHORTFORM.equals("DEALER") || SHORTFORM.equals("SE"))
                    {
                        try {
                            if (notify_type.equals("dispatched")) {
                                Intent intent = new Intent(getActivity(), InvoiceDetailsActivity.class);
                                intent.putExtra("id", related_id);
                                intent.putExtra("invoice_id", "noinvoiceid");
                                startActivity(intent);
                            }else if (notify_type.equals("credited")) {
                                Intent intent3=new Intent(getActivity(), NotificationStatementActivity.class);
                                intent3.putExtra("title","Statement");
                                if(user_role.equals("SE"))
                                {
                                    intent3.putExtra("user",related_id);
                                    intent3.putExtra("fromdate",notifiy_date);
                                    intent3.putExtra("user_role",user_role);
                                    intent3.putExtra("branch",branch);
                                }else{
                                    intent3.putExtra("user",user_id);
                                    intent3.putExtra("fromdate",notifiy_date);
                                    intent3.putExtra("user_role",user_role);
                                    intent3.putExtra("branch",branch);
                                }
                                startActivity(intent3);
                            }else {

                                String SCREENFROM="";
                                if(notify_type.equals("transfered"))
                                {
                                    SCREENFROM="Transfered";
                                }else if(notify_type.equals("cancelled"))
                                {
                                    SCREENFROM="Cancelled";
                                }else  if(notify_type.equals("approved"))
                                {
                                    SCREENFROM="Approved";
                                }else
                                {
                                    SCREENFROM="Placed";
                                }

                                if(SCREENFROM.equals("Transfered"))
                                {
                                    Intent intent = new Intent(getActivity(), FinanceTransportOrderDetailsActivity.class);
                                    intent.putExtra("id", related_id);
                                    intent.putExtra("orderId", orderId);
                                    intent.putExtra("status", "notification");
                                    intent.putExtra("SCREENFROM","Transferred");
                                    startActivity(intent);
                                }else{
                                    Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
                                    intent.putExtra("id", related_id);
                                    intent.putExtra("orderId", orderId);
                                    intent.putExtra("status", "");
                                    intent.putExtra("SCREENFROM", SCREENFROM);
                                    startActivity(intent);
                                }


                            }
                        } catch (Exception e) {
                        }
                    }else if(SHORTFORM.equals("FM")){
                        String SCREENFROM="";
                        if(notify_type.equals("placed"))
                        {
                            SCREENFROM="Placed";
                        }else  if(notify_type.equals("cancelled"))
                        {
                            SCREENFROM="Cancelled";
                        }else  if(notify_type.equals("approved"))
                        {
                            SCREENFROM="Approved";
                        }else  if(notify_type.equals("rejected"))
                        {
                            SCREENFROM="Rejected";
                        }
                        String status = "";
                        Intent intent = new Intent(getActivity(), FinanceOrderDetailsActivity.class);
                        intent.putExtra("id", related_id);
                        intent.putExtra("orderId", orderId);
                        intent.putExtra("status", "notification");
                        intent.putExtra("SCREENFROM",SCREENFROM);
                        startActivity(intent);
                    }

                } else {
                    Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }


//                Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
//                intent.putExtra("id", related_id);
//                intent.putExtra("orderId", orderId);
//                intent.putExtra("status", status);
//                intent.putExtra("SCREENFROM", "Placed");
//                startActivity(intent);
            }
        }
    }
}
