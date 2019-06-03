package com.mrnovacrm.b2b_finance_dept;

//public class FinanceDashBoardFragment {
//}

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by prasad on 5/9/2018.
 */

public class FinanceDashBoardFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> menuNameList = new ArrayList<String>();
    ArrayList<String> menuColorList = new ArrayList<String>();
    private TransparentProgressDialog progress;
    private String openURL = "";
    private int newsfeedstatus;
    int Pending, Open, Escalated, In_Progress, Closed, Not_Related, Reopen, Other_Department;
    String message;
    GlobalShare globalShare;
    String BRANCHIDVAL;
    private String PRIMARYID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_dashboard, container, false);
//        View mCustomView = inflater.inflate(R.layout.regactionbarheader, null);
//        //((ActionBarActivity)getActivity()).getSupportActionBar()
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(mCustomView);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (SharedDB.isLoggedIn(getActivity())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getActivity());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            BRANCHIDVAL = values.get(SharedDB.BRANCHID);
        }

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        globalShare=(GlobalShare)getActivity().getApplicationContext();

        menuNameList.clear();
        menuColorList.clear();

        menuNameList.add("Orders");
        menuNameList.add("Dispatchers");
//        menuNameList.add("Delivery Boys");
       // menuNameList.add("Sales Executives");
    //    menuNameList.add("Reports");

        menuColorList.add("#5867c4");
        menuColorList.add("#00c4dc");
  //      menuColorList.add("#a146d1");
       // menuColorList.add("#b21878");
      //  menuColorList.add("#f9a20c");

        int[] menuicons = {R.drawable.menu_orders,
                R.drawable.menu_orders,R.drawable.menu_orders,R.drawable.menu_orders,R.drawable.menu_orders
        };

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(),
                menuNameList, menuColorList,menuicons);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context mContext;
        ArrayList<String> menuNameList;
        ArrayList<String> menuColorList;
        int[] menuicons;

        public RecyclerViewAdapter(Context mContext, ArrayList<String> NameList, ArrayList<String> colorsList,int[] menuicons) {
            this.mContext = mContext;
            this.menuNameList = NameList;
            this.menuColorList = colorsList;
            this.menuicons=menuicons;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_userdashboard, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.text_count.setText(menuNameList.get(position));
            holder.linearcardview.setBackgroundColor(Color.parseColor(menuColorList.get(position)));
            holder.dashimag.setImageResource(menuicons[position]);
//            holder.text_count.setTextColor(Color.parseColor(menuColorList.get(position)));
//            holder.text_count.setText(countList.get(position));
        }

        @Override
        public int getItemCount() {
            return menuNameList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView text_count;
            LinearLayout linearcardview;
            ImageView dashimag;
            MyViewHolder(View view) {
                super(view);
                linearcardview = view.findViewById(R.id.linearcardview);
                text_count = view.findViewById(R.id.text_count);
                dashimag = view.findViewById(R.id.dashimag);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                boolean isConnectedToInternet = CheckNetWork
                        .isConnectedToInternet(getActivity());
                if(isConnectedToInternet)
                {
                    try{
                        String Status = menuNameList.get(getAdapterPosition());
                        String title="";
                        if (Status.equals("Orders")) {
                            Intent intent=new Intent(getActivity(),FinanceOrdersTabActivity.class);
                            startActivity(intent);
                        }else if (Status.equals("Payments")) {

                        }else if (Status.equals("Dispatchers")) {
                            Intent intent = new Intent(getActivity(), FinanceEmployeesListActivity.class);
                            intent.putExtra("SHORTFROM","PACKER");
                            intent.putExtra("TITLE","Dispachers List");
                            startActivity(intent);
                        }
//                else if (Status.equals("Delivery Boys")) {
//                    Intent intent = new Intent(getActivity(), FinanceEmployeesListActivity.class);
//                    intent.putExtra("SHORTFROM","DB");
//                    intent.putExtra("TITLE","Devlivery Boys List");
//                    startActivity(intent);
//                }
//                else if (Status.equals("Sales Executives")) {
//                    Intent intent = new Intent(getActivity(), FinanceEmployeesListActivity.class);
//                    intent.putExtra("SHORTFROM","SE");
//                    intent.putExtra("TITLE","Sales Executives");
//                    startActivity(intent);
//                }
                        else if (Status.equals("Reports")) {
                            Intent intent = new Intent(getActivity(), FinanceReportActivity.class);
                            startActivity(intent);
                        }
                    }catch(Exception e)
                    {
                    }
                }else{
                    Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}