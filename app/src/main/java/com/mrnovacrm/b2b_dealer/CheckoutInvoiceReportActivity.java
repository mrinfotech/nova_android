package com.mrnovacrm.b2b_dealer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrnovacrm.R;
import com.mrnovacrm.constants.GlobalShare;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutInvoiceReportActivity extends AppCompatActivity{

    private GlobalShare globalShare;
    Context context;
    public static Activity mainfinish;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalShare = (GlobalShare) getApplicationContext();
        setTheme(R.style.AppTheme);
        context = this;
        mainfinish = this;
        setTitle("Invoices List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.layout_checkoutinvoice);

        if(UserPaymentActivity.mainfinish!=null)
        {
            UserPaymentActivity.mainfinish.finish();
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        if(globalShare.getInvoicesList()!=null)
        {
            if(globalShare.getInvoicesList().size()>0)
            {
                ArrayList<HashMap<String, String>> invoiceslist=globalShare.getInvoicesList();
                showSellersList(invoiceslist);
            }
        }
    }

    public void showSellersList(ArrayList<HashMap<String, String>> hashmapList)
    {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this,hashmapList);
        recyclerView.setAdapter(adapter);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private Context mContext;

        ArrayList<HashMap<String, String>> hashmapList;
        public RecyclerViewAdapter(Context mContext,ArrayList<HashMap<String, String>> hashmapList) {
            this.mContext = mContext;
            this.hashmapList=hashmapList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.invoicereport_cardview, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            String invoice_id=hashmapList.get(position).get("invoice_id");
        //    String order_date=hashmapList.get(position).get("order_date");

            holder.orderidtxt.setText(invoice_id);
        }

        @Override
        public int getItemCount() {
            return hashmapList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView orderidtxt;
            MyViewHolder(View view) {
                super(view);
                orderidtxt=view.findViewById(R.id.orderidtxt);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int pos=getAdapterPosition();
                String order_key = hashmapList.get(pos).get("invoice_key");
                String orderId=hashmapList.get(pos).get("invoice_id");

                Intent intent = new Intent(getApplicationContext(), OrderSummaryActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("order_key",order_key);
                startActivity(intent);
//                Intent intent = new Intent(getActivity(), InvoiceDetailsActivity.class);
//                intent.putExtra("id",id);
//                intent.putExtra("invoice_id", invoice_id);
//                startActivity(intent);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                globalShare.setDeliverymenuselectpos("1");
                if(DealerMenuScreenActivity.mainfinish!=null)
                {
                    DealerMenuScreenActivity.mainfinish.finish();
                }
                if(ProductCategoriesActivity.mainfinish!=null)
                {
                    ProductCategoriesActivity.mainfinish.finish();
                }
                Intent intent = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
                startActivity(intent);

                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        globalShare.setDeliverymenuselectpos("1");
        if(DealerMenuScreenActivity.mainfinish!=null)
        {
            DealerMenuScreenActivity.mainfinish.finish();
        }
        if(ProductCategoriesActivity.mainfinish!=null)
        {
            ProductCategoriesActivity.mainfinish.finish();
        }
        Intent intent = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
        startActivity(intent);
        finish();

    }
}
