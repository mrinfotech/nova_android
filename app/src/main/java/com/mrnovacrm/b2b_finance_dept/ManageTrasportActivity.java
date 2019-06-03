package com.mrnovacrm.b2b_finance_dept;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.mrnovacrm.R;
import com.mrnovacrm.adapter.SpinnerItemsAdapter;

import java.util.ArrayList;

public class ManageTrasportActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner transport_spinner;
    EditText lpnumber,vehiclenumber,drivernameedittxt,date,paid,topay,phonenumberedittext;
    Button save_btn;
    ArrayList<String> transportNamesList = new ArrayList<>();
    ArrayList<String> transportIDsList = new ArrayList<>();
    String TRANSPORT_ID="";
    LinearLayout drivernamelinear,drivermobilelinear,vehiclnumberlinear,lpnumberlinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Transport Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.layout_managetransport);

        transport_spinner=findViewById(R.id.transport_spinner);
        lpnumber=findViewById(R.id.lpnumber);
        vehiclenumber=findViewById(R.id.vehiclenumber);
        drivernameedittxt=findViewById(R.id.drivernameedittxt);
        date=findViewById(R.id.date);
        paid=findViewById(R.id.paid);
        topay=findViewById(R.id.topay);
        phonenumberedittext=findViewById(R.id.phonenumberedittext);
        save_btn=findViewById(R.id.save_btn);
        save_btn.setOnClickListener(ManageTrasportActivity.this);

        drivernamelinear=findViewById(R.id.drivernamelinear);
        vehiclnumberlinear=findViewById(R.id.vehiclnumberlinear);
        lpnumberlinear=findViewById(R.id.lpnumberlinear);
        drivermobilelinear=findViewById(R.id.drivermobilelinear);

        drivernamelinear.setVisibility(View.GONE);
        drivermobilelinear.setVisibility(View.GONE);
        vehiclnumberlinear.setVisibility(View.GONE);

        hintTransport();
        getTransportData();
      }
      public void getTransportData()
      {
          transportIDsList.clear();
          transportNamesList.add("Select Transport");
          transportNamesList.add("Navata");
          transportNamesList.add("SRMT");
          transportNamesList.add("MBPS");
          transportNamesList.add("Navya");
          transportNamesList.add("Private");

          transportIDsList.add("1");
          transportIDsList.add("2");
          transportIDsList.add("3");
          transportIDsList.add("4");
          transportIDsList.add("Private");

          loadTransportData();
      }

      public void loadTransportData()
      {
          SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                  R.layout.layout_spinneritems, transportNamesList);
          spinnerClass
                  .setDropDownViewResource(R.layout.layout_spinneritems);
          transport_spinner.setAdapter(spinnerClass);
          transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view,
                                         int position, long id) {
                  // TODO Auto-generated method stub
                  if (position == 0) {
                      TRANSPORT_ID = "";
                  } else {
                      TRANSPORT_ID=transportIDsList.get(position-1);
                      if(TRANSPORT_ID.equals("Private"))
                      {
                          drivernamelinear.setVisibility(View.VISIBLE);
                          drivermobilelinear.setVisibility(View.VISIBLE);
                          vehiclnumberlinear.setVisibility(View.VISIBLE);
                          lpnumberlinear.setVisibility(View.GONE);
                      }else{
                          drivernamelinear.setVisibility(View.GONE);
                          drivermobilelinear.setVisibility(View.GONE);
                          vehiclnumberlinear.setVisibility(View.GONE);
                          lpnumberlinear.setVisibility(View.VISIBLE);
                      }
                  }
              }
              @Override
              public void onNothingSelected(AdapterView<?> parent) {
                  // TODO Auto-generated method stub
              }
          });
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.save_btn:
                break;
            default:
                break;
        }
    }

    public void hintTransport() {
        transportNamesList.clear();
        transportNamesList.add("Select Transport");
        SpinnerItemsAdapter spinnerClass = new SpinnerItemsAdapter(this,
                R.layout.layout_spinneritems, transportNamesList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritems);
        transport_spinner.setAdapter(spinnerClass);
        transport_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    TRANSPORT_ID = "";
                } else {
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
}