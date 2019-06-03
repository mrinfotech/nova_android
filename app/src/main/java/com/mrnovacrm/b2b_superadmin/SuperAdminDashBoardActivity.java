package com.mrnovacrm.b2b_superadmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_admin.AdminOrdersTabActivity;
import com.mrnovacrm.b2b_admin.AdminPaymentsActivity;
import com.mrnovacrm.b2b_admin.EmpListActivity;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.TransparentProgressDialog;

import java.util.ArrayList;

public class SuperAdminDashBoardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> menuNameList = new ArrayList<String>();
    ArrayList<String> menuColorList = new ArrayList<String>();
    private TransparentProgressDialog progress;
    private String openURL = "";
    private int newsfeedstatus;
    int Pending, Open, Escalated, In_Progress, Closed, Not_Related, Reopen, Other_Department;
    String message;
    GlobalShare globalShare;
    String BRANCHNAMEVAL="";
    String BRANCHIDVAL="";
    public static Activity mainfinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.layout_dashboard);

        Bundle bundle=getIntent().getExtras();
        BRANCHIDVAL=bundle.getString("id");
        BRANCHNAMEVAL=bundle.getString("branchname");

        setTitle(BRANCHNAMEVAL);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        globalShare=(GlobalShare)getApplicationContext();

        menuNameList.clear();
        menuColorList.clear();

        menuNameList.add("Orders");
        menuNameList.add("Payments");
        menuNameList.add("Dispatchers");
        menuNameList.add("Dealers");
        menuNameList.add("Sales Executives");

        menuColorList.add("#5867c4");
        menuColorList.add("#00c4dc");
        menuColorList.add("#a146d1");
        menuColorList.add("#b21878");
        menuColorList.add("#f9a20c");

        int[] menuicons = {R.drawable.menu_orders,
                R.drawable.menu_orders,R.drawable.menu_orders,R.drawable.menu_orders,R.drawable.menu_orders
        };

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,
                menuNameList, menuColorList,menuicons);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
                String Status = menuNameList.get(getAdapterPosition());
                String title="";
                if (Status.equals("Orders")) {
                    globalShare.setSelectedBranchid(BRANCHIDVAL);
                    Intent intent=new Intent(getApplicationContext(),AdminOrdersTabActivity.class);
                    startActivity(intent);
                }else if (Status.equals("Payments")) {
                    Intent intent=new Intent(getApplicationContext(),AdminPaymentsActivity.class);
                    intent.putExtra("BRANCH_ID",BRANCHIDVAL);
                    startActivity(intent);
                }else if (Status.equals("Dispatchers")) {
//                    Intent intent=new Intent(getApplicationContext(),EmployeeDataListActivity.class);
//                    intent.putExtra("title",Status);
//                    intent.putExtra("branch",BRANCHIDVAL);
//                    intent.putExtra("shortform","PACKER");
//                    startActivity(intent);
                    Intent intent = new Intent(getApplicationContext(), EmpListActivity.class);
                    intent.putExtra("title", Status);
                    intent.putExtra("branch", BRANCHIDVAL);
                    intent.putExtra("shortform", "PACKER");
                    startActivity(intent);



                }else if (Status.equals("Dealers")) {


//                Intent intent=new Intent(getApplicationContext(),EmployeeDataListActivity.class);
//                intent.putExtra("title",Status);
//                intent.putExtra("branch",BRANCHIDVAL);
//                intent.putExtra("shortform","DEALER");
//                startActivity(intent);

                    Intent intent = new Intent(getApplicationContext(), EmpListActivity.class);
                    intent.putExtra("title", Status);
                    intent.putExtra("branch", BRANCHIDVAL);
                    intent.putExtra("shortform", "DEALER");
                    startActivity(intent);


                 }
//                else if (Status.equals("Delivery Boys")) {
//                    Intent intent=new Intent(getApplicationContext(),EmployeeDataListActivity.class);
//                    intent.putExtra("title",Status);
//                    intent.putExtra("branch",BRANCHIDVAL);
//                    intent.putExtra("shortform","DB");
//                    startActivity(intent);
//                }
                else if (Status.equals("Sales Executives")) {


//                    Intent intent=new Intent(getApplicationContext(),EmpListActivity.class);
//                    intent.putExtra("title",Status);
//                    intent.putExtra("branch",BRANCHIDVAL);
//                    intent.putExtra("shortform","SE");
//                    startActivity(intent);

                    Intent intent = new Intent(getApplicationContext(), EmpListActivity.class);
                    intent.putExtra("title", Status);
                    intent.putExtra("branch", BRANCHIDVAL);
                    intent.putExtra("shortform", "SE");
                    startActivity(intent);
                }
            }
        }
    }
}
