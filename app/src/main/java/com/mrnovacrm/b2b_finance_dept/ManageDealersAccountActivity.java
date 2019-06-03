package com.mrnovacrm.b2b_finance_dept;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.model.DealerRegisrationDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageDealersAccountActivity extends AppCompatActivity implements View.OnClickListener {

    Button nextbtn;
    public static Activity mainfinish;
    private DealerRegisrationDTO dealerregdto;
    EditText pannumber_txt, gstinnumber_txt, registraitontype_txt,
            bankname_txt, accountnumber_txt, ifsc_txt,licencennumber_txt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish = this;
        setTheme(R.style.AppTheme);
        setTitle("Manage Dealer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.layout_managedealeraccount);

        // 1. get passed intent
        Intent intent = getIntent();
        dealerregdto = (DealerRegisrationDTO) intent.getSerializableExtra("dealerregdto");

        String dealerName = dealerregdto.getDealerName();
        String companyname = dealerregdto.getCompanyName();


        pannumber_txt = findViewById(R.id.pannumber_txt);
        licencennumber_txt = findViewById(R.id.licencennumber_txt);
        gstinnumber_txt = findViewById(R.id.gstinnumber_txt);
        registraitontype_txt = findViewById(R.id.registraitontype_txt);
        bankname_txt = findViewById(R.id.bankname_txt);
        accountnumber_txt = findViewById(R.id.accountnumber_txt);
        ifsc_txt = findViewById(R.id.ifsc_txt);

        pannumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        nextbtn = findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(ManageDealersAccountActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextbtn:

//                EditText pannumber_txt,gstinnumber_txt,registraitontype_txt,
//                        bankname_txt,accountnumber_txt,ifsc_txt;

                String PAN_NUMBER = pannumber_txt.getText().toString().trim();
                String GSTIN_NUMBER = gstinnumber_txt.getText().toString().trim();
                String REGISTRATION_TYPE = registraitontype_txt.getText().toString().trim();
                String BANK_NAME = bankname_txt.getText().toString().trim();
                String ACCOUNT_NUMER = accountnumber_txt.getText().toString().trim();
                String IFSCCODE = ifsc_txt.getText().toString().trim();
                String LICENCENO = licencennumber_txt.getText().toString().trim();


                if (PAN_NUMBER == null || "".equalsIgnoreCase(PAN_NUMBER) || PAN_NUMBER.equals("")
                        || GSTIN_NUMBER == null || "".equalsIgnoreCase(GSTIN_NUMBER) || GSTIN_NUMBER.equals("")
                        || LICENCENO == null || "".equalsIgnoreCase(LICENCENO) || LICENCENO.equals("")
                        || REGISTRATION_TYPE == null || "".equalsIgnoreCase(REGISTRATION_TYPE) || REGISTRATION_TYPE.equals("")
                        || BANK_NAME == null || "".equalsIgnoreCase(BANK_NAME) || BANK_NAME.equals("")
                        || ACCOUNT_NUMER == null || "".equalsIgnoreCase(ACCOUNT_NUMER) || ACCOUNT_NUMER.equals("")
                        || IFSCCODE == null || "".equalsIgnoreCase(IFSCCODE) || IFSCCODE.equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {
                    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
                    Matcher matcher = pattern.matcher(PAN_NUMBER);
                    // Check if pattern matches
                    if (matcher.matches()) {
                        String GSTINPattern = "^([0]{1}[1-9]{1}|[1-2]{1}[0-9]{1}|[3]{1}[0-7]{1})([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$";
                        if (GSTIN_NUMBER.matches(GSTINPattern)) {
                            dealerregdto.setPANnumber(PAN_NUMBER);
                            dealerregdto.setGSTINnumber(GSTIN_NUMBER);
                            dealerregdto.setRegastrtiontype(REGISTRATION_TYPE);
                            dealerregdto.setBankName(BANK_NAME);
                            dealerregdto.setAccountnumber(ACCOUNT_NUMER);
                            dealerregdto.setIFSCcode(IFSCCODE);
                            dealerregdto.setLicencenumber(LICENCENO);


                            Intent intent = new Intent(getApplicationContext(), ManageDealersContactsActivity.class);
                            intent.putExtra("dealerregdto", dealerregdto);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter valid gstin number", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid pan number", Toast.LENGTH_SHORT).show();
                    }
                }
//                Intent intent =new Intent(getApplicationContext(),ManageDealersContactsActivity.class);
//                startActivity(intent);
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