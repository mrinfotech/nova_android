package com.mrnovacrm.b2b_finance_dept;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.DealerRegisrationDTO;

import java.util.HashMap;

public class ManageDealersContactsActivity extends AppCompatActivity implements View.OnClickListener {

    Button nextbtn;
    public static Activity mainfinish;
    private DealerRegisrationDTO dealerregdto;
    EditText dealerid_txt,officialemail_txt,officialcontact_txt,officialwhatsapp,areasalesmanager_txt,areamanagercontact_txt;
    private String USERNAME;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish=this;
        setTheme(R.style.AppTheme);
        setTitle("Manage Dealer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.layout_managedealerscontact);

        if (SharedDB.isLoggedIn(getApplicationContext())) {
            HashMap<String, String> values = SharedDB.getUserDetails(getApplicationContext());
            USERNAME = values.get(SharedDB.USERNAME);
        }

        Intent intent = getIntent();
        dealerregdto = (DealerRegisrationDTO) intent.getSerializableExtra("dealerregdto");

        nextbtn=findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(ManageDealersContactsActivity.this);

        dealerid_txt=findViewById(R.id.dealerid_txt);
        officialemail_txt=findViewById(R.id.officialemail_txt);
        officialcontact_txt=findViewById(R.id.officialcontact_txt);
        officialwhatsapp=findViewById(R.id.officialwhatsapp);
        areasalesmanager_txt=findViewById(R.id.areasalesmanager_txt);
        areamanagercontact_txt=findViewById(R.id.areamanagercontact_txt);

        areasalesmanager_txt.setText(USERNAME);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.nextbtn:

                String DEALER_ID=dealerid_txt.getText().toString().trim();
                String OFFICIALEMAIL=officialemail_txt.getText().toString().trim();
                String OFFICIAL_CONTACT=officialcontact_txt.getText().toString().trim();
                String OFFICIAL_WHATSAPP=officialwhatsapp.getText().toString().trim();
                String AREA_SALESMANAGER=areasalesmanager_txt.getText().toString().trim();
                String AREA_MANAGER=areamanagercontact_txt.getText().toString().trim();

                if (DEALER_ID == null || "".equalsIgnoreCase(DEALER_ID)|| DEALER_ID.equals("")
                        || OFFICIALEMAIL == null || "".equalsIgnoreCase(OFFICIALEMAIL)|| OFFICIALEMAIL.equals("")
                        || OFFICIAL_CONTACT == null || "".equalsIgnoreCase(OFFICIAL_CONTACT) || OFFICIAL_CONTACT.equals("")
                        || OFFICIAL_WHATSAPP == null || "".equalsIgnoreCase(OFFICIAL_WHATSAPP) || OFFICIAL_WHATSAPP.equals("")
                        || AREA_SALESMANAGER == null || "".equalsIgnoreCase(AREA_SALESMANAGER)|| AREA_SALESMANAGER.equals("")
                        || AREA_MANAGER == null || "".equalsIgnoreCase(AREA_MANAGER)|| AREA_MANAGER.equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    dealerregdto.setDealerID(DEALER_ID);
                    dealerregdto.setOfficialemail(OFFICIALEMAIL);
                    dealerregdto.setOfficialcontact(OFFICIAL_CONTACT);
                    dealerregdto.setOfficialwhatsapp(OFFICIAL_WHATSAPP);
                    dealerregdto.setAreaSalesManager(AREA_SALESMANAGER);
                    dealerregdto.setAreamanagercontactnumber(AREA_MANAGER);

                    Intent intent=new Intent(getApplicationContext(),ManageDealersPersonalcontact.class);
                    intent.putExtra("dealerregdto",dealerregdto);
                    startActivity(intent);
                }
                break;
            default:
                break;
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
}