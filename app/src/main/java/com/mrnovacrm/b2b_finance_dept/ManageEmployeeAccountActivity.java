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
import com.mrnovacrm.model.EmployeeRegistrationDTO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageEmployeeAccountActivity extends AppCompatActivity implements View.OnClickListener {

    public static Activity mainfinish;
    private Button nextbtn;
    private EmployeeRegistrationDTO empregdto;

    EditText pannumber_txt,pfnumber_txt,esinumber_txt,
            bankname_txt,accountnumber_txt,ifsc_txt;
    private String fromval;
    private String title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainfinish = this;
        setTheme(R.style.AppTheme);


        setContentView(R.layout.layout_manageemployeeaccount);

        Intent intent = getIntent();
        empregdto = (EmployeeRegistrationDTO) intent.getSerializableExtra("empregdto");

        Bundle bundle=getIntent().getExtras();
        fromval=bundle.getString("fromval");
        title=bundle.getString("title");
        setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pannumber_txt=findViewById(R.id.pannumber_txt);
        pfnumber_txt=findViewById(R.id.pfnumber_txt);
        esinumber_txt=findViewById(R.id.esinumber_txt);
        bankname_txt=findViewById(R.id.bankname_txt);
        accountnumber_txt=findViewById(R.id.accountnumber_txt);
        ifsc_txt=findViewById(R.id.ifsc_txt);

        nextbtn=findViewById(R.id.nextbtn);
        nextbtn.setOnClickListener(ManageEmployeeAccountActivity.this);

        pannumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        pfnumber_txt.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.nextbtn:
                String PAN_NUMBER=pannumber_txt.getText().toString().trim();
                String PF_NUMBER=pfnumber_txt.getText().toString().trim();
                String ESI_NUMBER=esinumber_txt.getText().toString().trim();
                String BANK_NAME=bankname_txt.getText().toString().trim();
                String ACCOUNT_NUMER=accountnumber_txt.getText().toString().trim();
                String IFSCCODE=ifsc_txt.getText().toString().trim();
                if (PAN_NUMBER == null || "".equalsIgnoreCase(PAN_NUMBER)|| PAN_NUMBER.equals("")
                        || ESI_NUMBER == null || "".equalsIgnoreCase(ESI_NUMBER) || ESI_NUMBER.equals("")
                        || BANK_NAME == null || "".equalsIgnoreCase(BANK_NAME) || BANK_NAME.equals("")
                        || ACCOUNT_NUMER == null || "".equalsIgnoreCase(ACCOUNT_NUMER)|| ACCOUNT_NUMER.equals("")
                        || IFSCCODE == null || "".equalsIgnoreCase(IFSCCODE)|| IFSCCODE.equals("")) {
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                } else {

                    Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
                    Matcher matcher = pattern.matcher(PAN_NUMBER);
                    // Check if pattern matches
                   if (matcher.matches()) {
                       empregdto.setPANnumber(PAN_NUMBER);
                       empregdto.setPFNumber(PF_NUMBER);
                       empregdto.setESINumber(ESI_NUMBER);
                       empregdto.setBankName(BANK_NAME);
                       empregdto.setAccountnumber(ACCOUNT_NUMER);
                       empregdto.setIFSCcode(IFSCCODE);

                       Intent intent =new Intent(getApplicationContext(),ManageEmployeeCompanyActivity.class);
                       intent.putExtra("empregdto",empregdto);
                       intent.putExtra("fromval",fromval);
                       intent.putExtra("title",title);
                       startActivity(intent);
 //                       Pattern pfPattern = Pattern.compile("[A-Z]{5}[0-9]{17}");
//                       Matcher pfMatcher = pfPattern.matcher(PF_NUMBER);
//                       if(pfMatcher.matches())
//                       {
//                           empregdto.setPANnumber(PAN_NUMBER);
//                           empregdto.setPFNumber(PF_NUMBER);
//                           empregdto.setESINumber(ESI_NUMBER);
//                           empregdto.setBankName(BANK_NAME);
//                           empregdto.setAccountnumber(ACCOUNT_NUMER);
//                           empregdto.setIFSCcode(IFSCCODE);
//
//                           Intent intent =new Intent(getApplicationContext(),ManageEmployeeCompanyActivity.class);
//                           intent.putExtra("empregdto",empregdto);
//                           intent.putExtra("fromval",fromval);
//                           intent.putExtra("title",title);
//                           startActivity(intent);
//                       }else{
//                            Toast.makeText(getApplicationContext(), "Please enter valid pf number", Toast.LENGTH_SHORT);
//                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Please enter valid pan number", Toast.LENGTH_SHORT).show();
                    }
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
