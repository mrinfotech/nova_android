//package com.nova.userprofile;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.ComponentName;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.provider.MediaStore;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Base64;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nova.R;
//import com.nova.b2b_admin.AdminMenuScreenActivity;
//import com.nova.b2b_dealer.DealerMenuScreenActivity;
//import com.nova.b2b_delivery_dept.DeliveryMenuScreenActivity;
//import com.nova.b2b_dispatch_dept.DispatchMenuScreenActivity;
//import com.nova.b2b_finance_dept.FinanceDeptMenuScreenActivity;
//import com.nova.b2b_superadmin.SuperAdminMenuScrenActivity;
//import com.nova.constants.CheckNetWork;
//import com.nova.constants.GlobalShare;
//import com.nova.constants.RetrofitAPI;
//import com.nova.constants.TransparentProgressDialog;
//import com.nova.db.SharedDB;
//import com.nova.model.ContactsModelDTO;
//import com.nova.model.EmpDetailsDTO;
//import com.nova.model.Login;
//import com.squareup.picasso.MemoryPolicy;
//import com.squareup.picasso.Picasso;
//import com.theartofdev.edmodo.cropper.CropImageView;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import de.hdodenhof.circleimageview.CircleImageView;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import static android.content.ContentValues.TAG;
//
//public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private CircleImageView profileimage;
//    Context context;
//    private String base64 = "";
//    public static final int RequestPermissionCode = 1;
//    private HashMap<String, String> values;
//    private String PRIMARYID = "";
//    private String USERTYPE = "";
//    private String SHORTFORM = "";
//    String USERNAME = "", MOBILE = "", DELIVERY_ADDRESS = "";
//
//    //private TextView profilenametxt, mobilenumbertxt, emailtxt, addresstxt;
//
//    GlobalShare globalShare;
//    ImageView camimg;
//    private boolean result;
//    private static final int OPENGALLERY = 1;
//    private static final int CAMERA_REQUEST = 2;
//    private Uri selectedImageUri;
//    private String selectedPath1;
//    private Bitmap selectedImageBitmap;
//    Uri mCropImageUri;
//
//    private Dialog cropalertDialog;
//    private View croplayout;
//    private CropImageView mCropImageView;
//
//    LinearLayout doblinear,
//            sexlinear,
//            fathernamelinear,
//            mothernamelinear,
//            martialllinear,
//            companylinear,
//            keypersonnamelinear,
//            gstinlinear,
//            regtypelinear,
//            esilinear, empidlinear, dealeridlinear, dojlinear, departmentlinear, designationlinear, companyemaillinear,
//            companycontactlinear, totalexplinear, reportingmanagerlinear, ofcemaillinear, officialcontactlinear,
//            officialwhatsapplinear, areasalesmanagerlinear, areamanagercontactlinear,pflinear,licencelinear;
//
//    TextView dobtxt, sextxt, fathernametxt, mothernametxt, martialtxt, addresstxt,
//            countrytxt, statetxt, towncitytxt, pincodetxt, pannumbertxt, pfnumbertxt, esinumbertxt,
//            banknametxt, accountnumbertxt, ifsccodetxt, employeeidtxt, dobjoiningtxt, departmenttxt, designationtxt,
//            companyemailtxt, companycontacttxt, totalexptxt, reportingmanagertxt, profilenametxt,
//            companynametxt,
//            key_persontxt,
//            dealeridtxt,
//            officialemail_txt,
//            officialcontact_txt,
//            officialwhatsapp_txt,
//            areasalesmanager_txt,
//            areamanagercontact_txt,
//            personalmobile_txt,
//            personalemail_txt,regtypetxt,gsttxt,licencenumbertxt;
//    private TransparentProgressDialog dialog;
//    LinearLayout editlinear;
//
//    public static Activity mainfinish;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        context = this;
//        globalShare = (GlobalShare) getApplicationContext();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_user_profile);
//        View includedLayout = findViewById(R.id.include_actionbar);
//        ImageView backimg = includedLayout.findViewById(R.id.backimg);
//        backimg.setOnClickListener(UserProfileActivity.this);
//        mainfinish=this;
//        editlinear = findViewById(R.id.editlinear);
//
//        profileimage = findViewById(R.id.profileimage);
//        licencenumbertxt = findViewById(R.id.licencenumbertxt);
//        doblinear = findViewById(R.id.doblinear);
//        sexlinear = findViewById(R.id.sexlinear);
//        pflinear = findViewById(R.id.pflinear);
//        licencelinear = findViewById(R.id.licencelinear);
//
//        fathernamelinear = findViewById(R.id.fathernamelinear);
//        mothernamelinear = findViewById(R.id.mothernamelinear);
//        martialllinear = findViewById(R.id.martialllinear);
//        companylinear = findViewById(R.id.companylinear);
//        keypersonnamelinear = findViewById(R.id.keypersonnamelinear);
//        gstinlinear = findViewById(R.id.gstinlinear);
//        regtypelinear = findViewById(R.id.regtypelinear);
//        esilinear = findViewById(R.id.esilinear);
//        empidlinear = findViewById(R.id.empidlinear);
//        dealeridlinear = findViewById(R.id.dealeridlinear);
//        dojlinear = findViewById(R.id.dojlinear);
//        departmentlinear = findViewById(R.id.departmentlinear);
//        designationlinear = findViewById(R.id.designationlinear);
//        companyemaillinear = findViewById(R.id.companyemaillinear);
//        companycontactlinear = findViewById(R.id.companycontactlinear);
//        totalexplinear = findViewById(R.id.totalexplinear);
//        reportingmanagerlinear = findViewById(R.id.reportingmanagerlinear);
//        ofcemaillinear = findViewById(R.id.ofcemaillinear);
//        officialcontactlinear = findViewById(R.id.officialcontactlinear);
//        officialwhatsapplinear = findViewById(R.id.officialwhatsapplinear);
//        areasalesmanagerlinear = findViewById(R.id.areasalesmanagerlinear);
//        areamanagercontactlinear = findViewById(R.id.areamanagercontactlinear);
//
//
//        gsttxt = findViewById(R.id.gsttxt);
//        regtypetxt = findViewById(R.id.regtypetxt);
//        companynametxt = findViewById(R.id.companynametxt);
//        key_persontxt = findViewById(R.id.key_persontxt);
//        dealeridtxt = findViewById(R.id.dealeridtxt);
//        officialemail_txt = findViewById(R.id.officialemail_txt);
//        officialcontact_txt = findViewById(R.id.officialcontact_txt);
//        officialwhatsapp_txt = findViewById(R.id.officialwhatsapp_txt);
//        areasalesmanager_txt = findViewById(R.id.areasalesmanager_txt);
//        areamanagercontact_txt = findViewById(R.id.areamanagercontact_txt);
//        personalmobile_txt = findViewById(R.id.personalmobile_txt);
//        personalemail_txt = findViewById(R.id.personalemail_txt);
//
//        dobtxt = findViewById(R.id.dobtxt);
//        sextxt = findViewById(R.id.sextxt);
//        fathernametxt = findViewById(R.id.fathernametxt);
//        mothernametxt = findViewById(R.id.mothernametxt);
//        martialtxt = findViewById(R.id.martialtxt);
//        addresstxt = findViewById(R.id.addresstxt);
//        countrytxt = findViewById(R.id.countrytxt);
//        statetxt = findViewById(R.id.statetxt);
//        towncitytxt = findViewById(R.id.towncitytxt);
//        pincodetxt = findViewById(R.id.pincodetxt);
//        pannumbertxt = findViewById(R.id.pannumbertxt);
//        pfnumbertxt = findViewById(R.id.pfnumbertxt);
//        esinumbertxt = findViewById(R.id.esinumbertxt);
//        banknametxt = findViewById(R.id.banknametxt);
//        accountnumbertxt = findViewById(R.id.accountnumbertxt);
//        ifsccodetxt = findViewById(R.id.ifsccodetxt);
//        employeeidtxt = findViewById(R.id.employeeidtxt);
//        dobjoiningtxt = findViewById(R.id.dobjoiningtxt);
//        departmenttxt = findViewById(R.id.departmenttxt);
//        designationtxt = findViewById(R.id.designationtxt);
//        companyemailtxt = findViewById(R.id.companyemailtxt);
//        companycontacttxt = findViewById(R.id.companycontacttxt);
//        totalexptxt = findViewById(R.id.totalexptxt);
//        reportingmanagertxt = findViewById(R.id.reportingmanagertxt);
//        profilenametxt = findViewById(R.id.profilenametxt);
//
//        addresstxt = findViewById(R.id.addresstxt);
//        camimg = findViewById(R.id.camimg);
//
//
//        try {
//            if (SharedDB.isLoggedIn(getApplicationContext())) {
//                values = SharedDB.getUserDetails(getApplicationContext());
//                PRIMARYID = values.get(SharedDB.PRIMARYID);
//                USERTYPE = values.get(SharedDB.USERTYPE);
//                SHORTFORM = values.get(SharedDB.SHORTFORM);
//                USERNAME = values.get(SharedDB.USERNAME);
//                MOBILE = values.get(SharedDB.MOBILE);
//                DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
//                String IMAGEURL = values.get(SharedDB.IMAGEURL);
//
//                if (!IMAGEURL.equals("")) {
//                    Picasso.with(context).load(IMAGEURL).memoryPolicy(MemoryPolicy.NO_STORE).into(profileimage);
//                } else {
//                    profileimage.setImageResource(R.drawable.noprofile);
//                }
//                profilenametxt.setText(USERNAME);
//                //mobilenumbertxt.setText(MOBILE);
//                addresstxt.setText(DELIVERY_ADDRESS);
//            }
//        } catch (Exception e) {
//        }
//
//        if(SHORTFORM.equals("DEALER"))
//        {
//            companylinear.setVisibility(View.VISIBLE);
//            keypersonnamelinear.setVisibility(View.VISIBLE);
//            gstinlinear.setVisibility(View.VISIBLE);
//            regtypelinear.setVisibility(View.VISIBLE);
//            doblinear.setVisibility(View.GONE);
//            sexlinear.setVisibility(View.GONE);
//            fathernamelinear.setVisibility(View.GONE);
//            mothernamelinear.setVisibility(View.GONE);
//            martialllinear.setVisibility(View.GONE);
//            pflinear.setVisibility(View.GONE);
//            esilinear.setVisibility(View.GONE);
//            empidlinear.setVisibility(View.GONE);
//            dojlinear.setVisibility(View.GONE);
//            departmentlinear.setVisibility(View.GONE);
//            designationlinear.setVisibility(View.GONE);
//            companyemaillinear.setVisibility(View.GONE);
//            companycontactlinear.setVisibility(View.GONE);
//            totalexplinear.setVisibility(View.GONE);
//            reportingmanagerlinear.setVisibility(View.GONE);
//
//            ofcemaillinear.setVisibility(View.VISIBLE);
//            officialcontactlinear.setVisibility(View.VISIBLE);
//            officialwhatsapplinear.setVisibility(View.VISIBLE);
//            areasalesmanagerlinear.setVisibility(View.VISIBLE);
//            areamanagercontactlinear.setVisibility(View.VISIBLE);
//
//            licencelinear.setVisibility(View.VISIBLE);
//        }else{
//
//            companylinear.setVisibility(View.GONE);
//            keypersonnamelinear.setVisibility(View.GONE);
//            doblinear.setVisibility(View.VISIBLE);
//            sexlinear.setVisibility(View.VISIBLE);
//            fathernamelinear.setVisibility(View.VISIBLE);
//            mothernamelinear.setVisibility(View.VISIBLE);
//            martialllinear.setVisibility(View.VISIBLE);
//            licencelinear.setVisibility(View.GONE);
//            gstinlinear.setVisibility(View.GONE);
//            regtypelinear.setVisibility(View.GONE);
//            pflinear.setVisibility(View.VISIBLE);
//            esilinear.setVisibility(View.VISIBLE);
//            empidlinear.setVisibility(View.VISIBLE);
//            dojlinear.setVisibility(View.VISIBLE);
//            departmentlinear.setVisibility(View.VISIBLE);
//            designationlinear.setVisibility(View.VISIBLE);
//            companyemaillinear.setVisibility(View.VISIBLE);
//            companycontactlinear.setVisibility(View.VISIBLE);
//            totalexplinear.setVisibility(View.VISIBLE);
//            reportingmanagerlinear.setVisibility(View.VISIBLE);
//
//            dealeridlinear.setVisibility(View.GONE);
//            ofcemaillinear.setVisibility(View.GONE);
//            officialcontactlinear.setVisibility(View.GONE);
//            officialwhatsapplinear.setVisibility(View.GONE);
//            areasalesmanagerlinear.setVisibility(View.GONE);
//            areamanagercontactlinear.setVisibility(View.GONE);
//        }
//
//
//        camimg.setOnClickListener(UserProfileActivity.this);
//        profileimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ProfileFullScreenActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        boolean isConnectedToInternet = CheckNetWork
//                .isConnectedToInternet(getApplicationContext());
//        if(isConnectedToInternet)
//        {
//            getEMPListDetails();
//        }else {
//            Toast.makeText(getApplicationContext(),R.string.networkerror,Toast.LENGTH_SHORT);
//        }
//        editlinear.setOnClickListener(UserProfileActivity.this);
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.camimg:
//               // selectImage();
//                startActivityForResult(getPickImageChooserIntent(), 200);
//
//                break;
//            case R.id.backimg:
//                recallMethod();
//                break;
//            case R.id.editlinear:
//                if(SHORTFORM.equals("DEALER")) {
//                    Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
//                    startActivity(intent);
//                }else{
//                    Intent intent = new Intent(getApplicationContext(), UpdateEmployeeProfileActivity.class);
//                    startActivity(intent);
//                }
//
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void getEMPListDetails() {
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(UserProfileActivity.this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//        Call<Login> mService = null;
//        if(SHORTFORM.equals("DEALER"))
//        {
//            mService = mApiService.getDealerDetails(PRIMARYID);
//        }else{
//            mService = mApiService.getEMPDetails(PRIMARYID);
//        }
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                dialog.dismiss();
//                Log.e("response", "" + response);
//                try {
//                    Login mOrderObject = response.body();
//                    String status = mOrderObject.getStatus();
//
//                    Log.e("status", status);
//
//                    if (Integer.parseInt(status) == 1) {
//                        List<EmpDetailsDTO> empDetailsDTOS = mOrderObject.getEmpDetailsDTOS();
//                        if(SHORTFORM.equals("DEALER"))
//                        {
//                            List<ContactsModelDTO> contactsModelDTOS = mOrderObject.getContactsModelDTOList();
//                            if(contactsModelDTOS!=null)
//                            {
//                                if (contactsModelDTOS.size() > 0) {
//                                    globalShare.setContactsModelDTOS(contactsModelDTOS);
//
//                                    for (int i = 0; i < contactsModelDTOS.size(); i++) {
//                                        String key_person = contactsModelDTOS.get(i).getKey_person();
//                                        String ofc_contact = contactsModelDTOS.get(i).getOfc_contact();
//                                        String ofc_email = contactsModelDTOS.get(i).getOfc_email();
//                                        String ofc_whatsapp = contactsModelDTOS.get(i).getOfc_whatsapp();
//
//                                        key_persontxt.setText(key_person);
//                                        officialemail_txt.setText(ofc_email);
//                                        officialcontact_txt.setText(ofc_contact);
//                                        officialwhatsapp_txt.setText(ofc_whatsapp);
//                                    }
//                                }
//                            }
//
//                        }
//
//                        if (empDetailsDTOS != null) {
//                            if (empDetailsDTOS.size() > 0) {
//                                globalShare.setDealerDetailsDTOS(empDetailsDTOS);
//
//                                for (int i = 0; i < empDetailsDTOS.size(); i++) {
//                                    String firstname = empDetailsDTOS.get(i).getFirst_name();
//                                    String last_name = empDetailsDTOS.get(i).getLast_name();
//                                    String uniq_id = empDetailsDTOS.get(i).getUniq_id();
//                                    String branch = empDetailsDTOS.get(i).getBranch();
//                                    String dept = empDetailsDTOS.get(i).getDept();
//                                    String mobile = empDetailsDTOS.get(i).getMobile();
//                                    String email = empDetailsDTOS.get(i).getEmail();
//                                    String dob = empDetailsDTOS.get(i).getDob();
//                                    String gender = empDetailsDTOS.get(i).getGender();
//                                    String location = empDetailsDTOS.get(i).getLocation();
//                                    String blood_group = empDetailsDTOS.get(i).getBlood_group();
//                                    String father = empDetailsDTOS.get(i).getFather();
//                                    String mother = empDetailsDTOS.get(i).getMother();
//                                    String marital = empDetailsDTOS.get(i).getMarital();
//                                    String expeience = empDetailsDTOS.get(i).getExpeience();
//                                    String report_to = empDetailsDTOS.get(i).getReport_to();
//                                    String doj = empDetailsDTOS.get(i).getDoj();
//                                    String pan = empDetailsDTOS.get(i).getPan();
//                                    String pf = empDetailsDTOS.get(i).getPf();
//                                    String esi = empDetailsDTOS.get(i).getEsi();
//                                    String bank_name = empDetailsDTOS.get(i).getBank_name();
//                                    String bank_account = empDetailsDTOS.get(i).getBank_account();
//                                    String ifsc = empDetailsDTOS.get(i).getIfsc();
//                                    String address = empDetailsDTOS.get(i).getAddress();
//                                    String addressproof1 = empDetailsDTOS.get(i).getAddressproof1();
//                                    String addressproof2 = empDetailsDTOS.get(i).getAddressproof2();
//                                    String role_id = empDetailsDTOS.get(i).getRole_id();
//                                    String ofc_email = empDetailsDTOS.get(i).getOfc_email();
//                                    String ofc_contact = empDetailsDTOS.get(i).getOfc_contact();
//                                    String createdon = empDetailsDTOS.get(i).getCreatedon();
//                                    String createdby = empDetailsDTOS.get(i).getCreatedby();
//                                    String modifiedon = empDetailsDTOS.get(i).getModifiedon();
//                                    String modifiedby = empDetailsDTOS.get(i).getModifiedby();
//                                    String country = empDetailsDTOS.get(i).getCountry();
//                                    String state = empDetailsDTOS.get(i).getState();
//                                    String city = empDetailsDTOS.get(i).getCity();
//                                    String pincode = empDetailsDTOS.get(i).getPincode();
//                                    String gstin = empDetailsDTOS.get(i).getGstin();
//                                    String company_name = empDetailsDTOS.get(i).getCompany_name();
//                                    //  String key_person = empDetailsDTOS.get(i).getKey_person();
//                                    String reg_type = empDetailsDTOS.get(i).getReg_type();
//                                    //   String ofc_whatsapp = empDetailsDTOS.get(i).getOfc_whatsapp();
//                                    String sales_manager = empDetailsDTOS.get(i).getSm();
//                                    String am_contact = empDetailsDTOS.get(i).getAm_contact();
//                                    String dealer_code = empDetailsDTOS.get(i).getDealer_code();
//                                    String country_id = empDetailsDTOS.get(i).getCountry_id();
//                                    String state_id = empDetailsDTOS.get(i).getState_id();
//                                    String licence = empDetailsDTOS.get(i).getLicence();
//                                    String area_sm = empDetailsDTOS.get(i).getSm();
//                                    String designation = empDetailsDTOS.get(i).getDesignation();
//
//                                    globalShare.setCountryid(country_id);
//                                    globalShare.setStateid(state_id);
//
//                                    if (SHORTFORM.equals("DEALER")) {
//                                        companynametxt.setText(company_name);
//                                        gsttxt.setText(gstin);
//                                        regtypetxt.setText(reg_type);
//                                        dealeridtxt.setText(dealer_code);
//
//                                        areasalesmanager_txt.setText(area_sm);
//                                        areamanagercontact_txt.setText(am_contact);
//                                        licencenumbertxt.setText(licence);
//
//                                    } else {
//                                        dobtxt.setText(dob);
//                                        sextxt.setText(gender);
//                                        fathernametxt.setText(father);
//                                        mothernametxt.setText(mother);
//                                        martialtxt.setText(marital);
//                                        pfnumbertxt.setText(pf);
//                                        esinumbertxt.setText(esi);
//
//                                        employeeidtxt.setText(uniq_id);
//                                        dobjoiningtxt.setText(doj);
//                                        departmenttxt.setText(dept);
//                                        designationtxt.setText(USERTYPE);
//                                        companyemailtxt.setText(ofc_email);
//                                        companycontacttxt.setText(ofc_contact);
//                                        totalexptxt.setText(expeience);
//                                        reportingmanagertxt.setText(report_to);
//                                    }
//                                    addresstxt.setText(address);
//                                    countrytxt.setText(country);
//                                    statetxt.setText(state);
//                                    towncitytxt.setText(city);
//                                    pincodetxt.setText(pincode);
//                                    pannumbertxt.setText(pan);
//                                    banknametxt.setText(bank_name);
//                                    accountnumbertxt.setText(bank_account);
//                                    ifsccodetxt.setText(ifsc);
//                                    personalmobile_txt.setText(mobile);
//                                    personalemail_txt.setText(email);
//
//                                }
//                            }
//                        }
//
//                    } else {
//                        String messge = mOrderObject.getMessage();
//                        Toast.makeText(getApplicationContext(), messge, Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        recallMethod();
//    }
//
//    public void recallMethod() {
//        if (DealerMenuScreenActivity.mainfinish != null) {
//            DealerMenuScreenActivity.mainfinish.finish();
//        }
//        if (SHORTFORM.equals("DEALER") || SHORTFORM.equals("SE")) {
//            globalShare.setDeliverymenuselectpos("1");
//            Intent intent = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
//            startActivity(intent);
//        }
//
//        if (FinanceDeptMenuScreenActivity.mainfinish != null) {
//            FinanceDeptMenuScreenActivity.mainfinish.finish();
//        }
//        if (SHORTFORM.equals("FM")) {
//            globalShare.setFinancemenuselectpos("1");
//            Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
//            startActivity(intent);
//        }
//
//        if (DispatchMenuScreenActivity.mainfinish != null) {
//            DispatchMenuScreenActivity.mainfinish.finish();
//        }
//        if (SHORTFORM.equals("PACKER")) {
//            globalShare.setDispatchmenuselectpos("1");
//            Intent intent = new Intent(getApplicationContext(), DispatchMenuScreenActivity.class);
//            startActivity(intent);
//        }
//
//        if (DeliveryMenuScreenActivity.mainfinish != null) {
//            DeliveryMenuScreenActivity.mainfinish.finish();
//        }
//
//        if (SHORTFORM.equals("DB")) {
//            globalShare.setDeliverymenuselectpos("1");
//            Intent intent = new Intent(getApplicationContext(), DeliveryMenuScreenActivity.class);
//            startActivity(intent);
//        }
//
//        if (AdminMenuScreenActivity.mainfinish != null) {
//            AdminMenuScreenActivity.mainfinish.finish();
//        }
//
//        if (SHORTFORM.equals("ADMIN")) {
//            globalShare.setAdminmenuselectpos("1");
//            Intent intent = new Intent(getApplicationContext(), AdminMenuScreenActivity.class);
//            startActivity(intent);
//        }
//
//        if (SuperAdminMenuScrenActivity.mainfinish != null) {
//            SuperAdminMenuScrenActivity.mainfinish.finish();
//        }
//
//        if (SHORTFORM.equals("SA")) {
//            globalShare.setSuperadminmenuselectpos("1");
//            Intent intent = new Intent(getApplicationContext(), SuperAdminMenuScrenActivity.class);
//            startActivity(intent);
//        }
//        finish();
//    }
//
////    private void selectImage() {
////        final Utility Utility = new Utility();
////        result = Utility.checkPermission(UserProfileActivity.this);
////        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};
////        final AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
////        builder.setTitle("Add Photo!");
////        builder.setItems(items, new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int item) {
////                if (items[item].equals("Take Photo")) {
////                    boolean cam = Utility.checkCameraPermission(UserProfileActivity.this);
////                    if (cam && result) {
////                        openCamera(CAMERA_REQUEST);
////                    } else {
////                        SharedDB.showCustomAlert("Camera Permission in necessary", UserProfileActivity.this);
////                    }
////                } else if (items[item].equals("Choose from Gallery")) {
////                    if (result) {
////                        openGallery(OPENGALLERY);
////                    }
////                }
////            }
////        });
////        builder.show();
////    }
////
////    public void openGallery(int req_code) {
////        Intent i = new Intent(
////                Intent.ACTION_PICK,
////                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////        startActivityForResult(Intent.createChooser(i, "Select file to upload "), req_code);
////    }
////
////    public void openCamera(int req_code) {
////        String fileName = System.currentTimeMillis() + ".jpg";
////        ContentValues values = new ContentValues();
////        values.put(MediaStore.Images.Media.TITLE, fileName);
////        selectedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
////        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
////        startActivityForResult(cameraIntent, req_code);
////    }
////
////    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////
////        if (resultCode == RESULT_OK) {
////            if (requestCode == OPENGALLERY) {
////                selectedImageUri = data.getData();
////                String[] filePathColumn = {MediaStore.Images.Media.DATA};
////                Cursor cursor = getContentResolver()
////                        .query(selectedImageUri, filePathColumn, null,
////                                null, null);
////                if (cursor != null) {
////                    cursor.moveToFirst();
////                    int columnIndex = cursor
////                            .getColumnIndex(filePathColumn[0]);
////                    selectedPath1 = cursor.getString(columnIndex);
////                    cursor.close();
////                }
////            } else if (requestCode == CAMERA_REQUEST) {
////                selectedPath1 = getRealPath(getApplicationContext(), selectedImageUri);
////
//////                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//////                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//////                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//////                File destination = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
//////                FileOutputStream fo;
//////                try {
//////                    fo = new FileOutputStream(destination);
//////                    fo.write(bytes.toByteArray());
//////                    fo.close();
//////                } catch (IOException e) {
//////                    e.printStackTrace();
//////                }
//////                new uploadFileToServerTask().execute(destination.getAbsolutePath());
////            }
////            selectedImageBitmap = decodeSampledBitmapFromFile(
////                    selectedPath1, 300, 300);
////            profileimage.setImageBitmap(selectedImageBitmap);
////            //submitDetails();
////
////            updateProfileimage();
////        }
////    }
////
////    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
////                                                     int reqHeight) { // BEST QUALITY MATCH
////
////        // First decode with inJustDecodeBounds=true to check dimensions
////        final BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inJustDecodeBounds = true;
////        BitmapFactory.decodeFile(path, options);
////
////        // Calculate inSampleSize, Raw height and width of image
////        final int height = options.outHeight;
////        final int width = options.outWidth;
////        options.inPreferredConfig = Bitmap.Config.RGB_565;
////        int inSampleSize = 1;
////
////        if (height > reqHeight) {
////            inSampleSize = Math.round((float) height / (float) reqHeight);
////        }
////        int expectedWidth = width / inSampleSize;
////
////        if (expectedWidth > reqWidth) {
////            // if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
////            // If bigger SampSize..
////            inSampleSize = Math.round((float) width / (float) reqWidth);
////        }
////
////        options.inSampleSize = inSampleSize;
////
////        // Decode bitmap with inSampleSize set
////        options.inJustDecodeBounds = false;
////
////        return BitmapFactory.decodeFile(path, options);
////    }
////
////    //For Camera
////    public static String getRealPath(Context context, Uri contentUri) {
////        Cursor cursor = null;
////        try {
////            String[] proj = {MediaStore.Images.Media.DATA};
////            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
////            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
////            cursor.moveToFirst();
////            return cursor.getString(column_index);
////        } finally {
////            if (cursor != null) {
////                cursor.close();
////            }
////        }
////    }
////
////    private class Utility {
////        static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
////        static final int MY_PERMISSIONS_REQUEST_CAMERA = 124;
////
////        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
////        boolean checkPermission(final Context context) {
////            int currentAPIVersion = Build.VERSION.SDK_INT;
////            if (currentAPIVersion >= Build.VERSION_CODES.M) {
////                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
////                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
////                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
////                        alertBuilder.setCancelable(true);
////                        alertBuilder.setTitle("Permission necessary");
////                        alertBuilder.setMessage("External storage permission is necessary");
////                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
////                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
////                            public void onClick(DialogInterface dialog, int which) {
////                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
////                            }
////                        });
////                        AlertDialog alert = alertBuilder.create();
////                        alert.show();
////                    } else {
////                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
////                    }
////                    return false;
////                } else {
////                    return true;
////                }
////            } else {
////                return true;
////            }
////        }
////
////        /**
////         * Check if this device has a camera permission
////         */
////        private boolean checkCameraPermission(Context context) {
////            int currentAPIVersion = Build.VERSION.SDK_INT;
////            if (currentAPIVersion >= Build.VERSION_CODES.M) {
////                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
////                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
////                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
////                    } else {
////                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
////                    }
////                    return false;
////                } else {
////                    return true;
////                }
////            } else {
////                return true;
////            }
////        }
////    }
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
////        switch (requestCode) {
////            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
////                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    //code for allow
////                } else {
////                    //code for deny
////                    SharedDB.showCustomAlert("External storage permission is necessary", UserProfileActivity.this);
////
////                }
////                break;
////            }
////            case Utility.MY_PERMISSIONS_REQUEST_CAMERA: {
////                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
////                    SharedDB.showCustomAlert("To capture image camera permission is necessary", UserProfileActivity.this);
////                }
////                break;
////            }
////        }
////    }
//
//    public void submitDetails() {
//        String action = "edit";
//        String encodedImage = null;
//        if (selectedImageBitmap != null) {
//            int width = selectedImageBitmap.getWidth();
//            int height = selectedImageBitmap.getHeight();
//            float xScale = ((float) 400) / width;
//            float yScale = ((float) 300) / height;
//            float scale = (xScale <= yScale) ? xScale : yScale;
//            Matrix matrix = new Matrix();
//            matrix.postScale(scale, scale);
//            Bitmap scaledBitmap = Bitmap.createBitmap(selectedImageBitmap, 0, 0,
//                    width, height, matrix, true);
//
//            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
//            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90,
//                    byteArray);
//            byte[] byte_arr = byteArray.toByteArray();
//
//            encodedImage = Base64
//                    .encodeToString(byte_arr, Base64.DEFAULT);
//        }
//
//        File file = new File(selectedPath1);
//        final TransparentProgressDialog dialog = new TransparentProgressDialog(UserProfileActivity.this);
//        dialog.show();
//        RetrofitAPI mApiService = SharedDB.getInterfaceService();
//
//
//        Call<Login> mService = mApiService.updateUserProfile(PRIMARYID, SHORTFORM, encodedImage);
//        mService.enqueue(new Callback<Login>() {
//            @Override
//            public void onResponse(Call<Login> call, Response<Login> response) {
//                Login mLoginObject = response.body();
//                dialog.dismiss();
//                try {
//                    String status = mLoginObject.getStatus();
//                    String message = mLoginObject.getMessage();
//                    if (status.equals("1")) {
//                        String dp = mLoginObject.getDp();
//
//                        String ADDRESSID = values.get(SharedDB.ADDRESSID);
//                        USERTYPE = values.get(SharedDB.USERTYPE);
//                        SHORTFORM = values.get(SharedDB.SHORTFORM);
//                        USERNAME = values.get(SharedDB.USERNAME);
//                        MOBILE = values.get(SharedDB.MOBILE);
//                        DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
//                        String COMPANY = values.get(SharedDB.COMPANY);
//                        String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
//                        String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
//                        String BRANCHID = values.get(SharedDB.BRANCHID);
//                        String PINCODE = values.get(SharedDB.PINCODE);
//                        String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
//
//                        SharedDB.loginSahred(getApplicationContext(), MOBILE, DELIVERY_ADDRESS, USERTYPE, "",
//                                "", 0.0, 0.0, PRIMARYID, PINCODE, USERNAME, dp, ADDRESSID, BRANCHID,
//                                BRANCHNAME, SHORTFORM, COMPANY, BRANCHCONTACT,COMPANIESLIST);
//                    } else {
//                        // Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                call.cancel();
//                dialog.dismiss();
//                //  Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void updateProfileimage()
//    {
//        dialog = new TransparentProgressDialog(UserProfileActivity.this);
//        dialog.show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    uploadFile();
//
//                } catch (OutOfMemoryError e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
//                            if( dialog!=null)
//                            {
//                                dialog.dismiss();
//                            }
//                        }
//                    });
//                }
//            }
//        }).start();
//    }
//
//    public int uploadFile() {
//
//        int serverResponseCode = 0;
//        final HttpURLConnection connection;
//        DataOutputStream dataOutputStream;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//
//        Log.e("selectedPath1",selectedPath1);
//
//        File selectedFile = new File(selectedPath1);
//        String[] parts = selectedPath1.split("/");
//        final String fileName = parts[parts.length - 1];
//
//        if (!selectedFile.isFile()) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
//                }
//            });
//            return 0;
//        } else {
//            try {
//                FileInputStream fileInputStream = new FileInputStream(selectedFile);
//                URL url = new URL(SharedDB.URL+"master/dp");
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);//Allow Inputs
//                connection.setDoOutput(true);//Allow Outputs
//                connection.setUseCaches(false);//Don't use a cached Copy
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Connection", "Keep-Alive");
//                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
//                connection.setRequestProperty(
//                        "Content-Type", "multipart/form-data;boundary=" + boundary);
//                connection.setRequestProperty("dp", selectedPath1);
//                dataOutputStream = new DataOutputStream(connection.getOutputStream());
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(PRIMARYID);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"role\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(SHORTFORM);
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"dp\";filename=\""
//                        + fileName + "\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);
//
////                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"addressproof2\";filename=\""
////                        + fileName1 + "\"" + lineEnd);
////                dataOutputStream.writeBytes(lineEnd);
//
//                //returns no. of bytes present in fileInputStream
//                bytesAvailable = fileInputStream.available();
//                //selecting the buffer size as minimum of available bytes or 1 MB
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                //setting the buffer as byte array of size of bufferSize
//                buffer = new byte[bufferSize];
//
//                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
//                while (bytesRead > 0) {
//                    try {
//                        //write the bytes read from inputstream
//                        dataOutputStream.write(buffer, 0, bufferSize);
//                    } catch (OutOfMemoryError e) {
//                        Toast.makeText(getApplicationContext(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
//                    }
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                }
//
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//
//                try {
//                    serverResponseCode = connection.getResponseCode();
//                } catch (OutOfMemoryError e) {
//                    Toast.makeText(getApplicationContext(), "Memory Insufficient!", Toast.LENGTH_SHORT).show();
//                }
//                String serverResponseMessage = connection.getResponseMessage();
//
//                Log.e(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
//
//                //response code of 200 indicates the server status OK
//                if (serverResponseCode == 200) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            StringBuilder sb = new StringBuilder();
//                            try {
//                                BufferedReader rd = new BufferedReader(new InputStreamReader(connection
//                                        .getInputStream()));
//                                String line;
//                                while ((line = rd.readLine()) != null) {
//                                    sb.append(line);
//                                }
//                                rd.close();
//                            } catch (IOException ioex) {
//                            }
//                            //      Log.e("response", "" + sb.toString());
//
//                            String response = sb.toString();
//
//                            int statusValue = 0;
//                            String messageValue = "";
//                            try {
//                                JSONObject res_object = new JSONObject(response);
//
//                                if (res_object.has("status")) {
//                                    statusValue = res_object.getInt("status");
//                                    Log.e("statusValue", "" + statusValue);
//                                }
//
//                                if (res_object.has("message")) {
//                                    messageValue = res_object.getString("message");
//                                    Log.e("messageValue", messageValue);
//                                }
//                                if (statusValue == 1) {
//                                    Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();
//                                    String dp = res_object.getString("dp");
//
//                                    Log.e("dp",dp);
//
//                                    String ADDRESSID = values.get(SharedDB.ADDRESSID);
//                                    USERTYPE = values.get(SharedDB.USERTYPE);
//                                    SHORTFORM = values.get(SharedDB.SHORTFORM);
//                                    USERNAME = values.get(SharedDB.USERNAME);
//                                    MOBILE = values.get(SharedDB.MOBILE);
//                                    DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
//                                    String COMPANY = values.get(SharedDB.COMPANY);
//                                    String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
//                                    String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
//                                    String BRANCHID = values.get(SharedDB.BRANCHID);
//                                    String PINCODE = values.get(SharedDB.PINCODE);
//                                    String LATITUDE = values.get(SharedDB.LATITUDE);
//                                    String LONGITIDE = values.get(SharedDB.LONGITIDE);
//                                    String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
//
//                                    SharedDB.loginSahred(getApplicationContext(), MOBILE, DELIVERY_ADDRESS, USERTYPE, "",
//                                            "", Double.parseDouble(LATITUDE), Double.parseDouble(LONGITIDE), PRIMARYID, PINCODE, USERNAME, dp, ADDRESSID, BRANCHID,
//                                            BRANCHNAME, SHORTFORM, COMPANY, BRANCHCONTACT,COMPANIESLIST);
//                                } else {
//                                    Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();
//                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//                //closing the input and output streams
//                fileInputStream.close();
//                dataOutputStream.flush();
//                dataOutputStream.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(), "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//            if(dialog!=null)
//            {
//                dialog.dismiss();
//            }
//            return serverResponseCode;
//        }
//    }
//
//
//    public Intent getPickImageChooserIntent() {
//        // Determine Uri of camera image to save.
//        Uri outputFileUri = getCaptureImageOutputUri();
//
//        List<Intent> allIntents = new ArrayList<>();
//        PackageManager packageManager = getPackageManager();
//
//        // collect all camera intents
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }
//        // collect all gallery intents
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//        for (ResolveInfo res : listGallery) {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            allIntents.add(intent);
//        }
//        // the main intent is the last in the list (fucking android) so pickup the useless one
//        Intent mainIntent = allIntents.get(allIntents.size() - 1);
//        for (Intent intent : allIntents) {
//            if (intent.getComponent().getClassName().equals("com.nova.userprofile.UserProfileActivity")) {
//                mainIntent = intent;
//                break;
//            }
//        }
//        allIntents.remove(mainIntent);
//        // Create a chooser from the main intent
//        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
//        // Add all other intents
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
//        return chooserIntent;
//    }
//
//    /**
//     * Get URI to image received from capture by camera.
//     */
//    private Uri getCaptureImageOutputUri() {
//        Uri outputFileUri = null;
//        File getImage = getExternalCacheDir();
//        if (getImage != null) {
//            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
//        }
//        return outputFileUri;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == Activity.RESULT_OK) {
//            showPopup();
//
//            Uri imageUri = getPickImageResultUri(data);
//            // For API >= 23 we need to check specifically that we have permissions to read external storage,
//            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
//            boolean requirePermissions = false;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    isUriRequiresPermissions(imageUri)) {
//                // request permissions and handle the result in onRequestPermissionsResult()
//                requirePermissions = true;
//                mCropImageUri = imageUri;
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//            }
//            if (!requirePermissions) {
//                mCropImageView.setImageUriAsync(imageUri);
//            }
//        }
//    }
//
//    /**
//     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
//     */
//    public boolean isUriRequiresPermissions(Uri uri) {
//        try {
//            ContentResolver resolver = getContentResolver();
//            InputStream stream = resolver.openInputStream(uri);
//            stream.close();
//            return false;
//        } catch (FileNotFoundException e) {
////            if (e.getCause() instanceof ErrnoException) {
////                return true;
////            }
//        } catch (Exception e) {
//        }
//        return false;
//    }
//
//
//    public Uri getPickImageResultUri(Intent data) {
//        boolean isCamera = true;
//        if (data != null && data.getData() != null) {
//            String action = data.getAction();
//            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
//        }
//        return isCamera ? getCaptureImageOutputUri() : data.getData();
//    }
//
//    public void showPopup() {
//        /** Used for Show Disclaimer Pop up screen */
//        cropalertDialog = new Dialog(this, R.style.MY_DIALOG);
//        cropalertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        cropalertDialog.getWindow().setBackgroundDrawableResource(
//                android.R.color.transparent);
//        LayoutInflater inflater = this.getLayoutInflater();
//        croplayout = inflater.inflate(R.layout.layout_updateprofilecorp, null);
//        cropalertDialog.setContentView(croplayout);
//        cropalertDialog.setCancelable(true);
//        if (!cropalertDialog.isShowing()) {
//            cropalertDialog.show();
//        }
//        mCropImageView = (CropImageView) croplayout.findViewById(R.id.CropImageView);
//    }
//
//    public void onCropImageClick(View view) {
//        Bitmap cropped = mCropImageView.getCroppedImage(200, 200);
//        cropalertDialog.dismiss();
//        if (cropped != null) {
//            // mCropImageView.setImageBitmap(cropped);
//            // mCropImageView.setVisibility(View.GONE);
//            cropalertDialog.dismiss();
//            profileimage.setImageBitmap(cropped);
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            cropped.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//            byte[] byte_arr = bytes.toByteArray();
//            base64 = Base64
//                    .encodeToString(byte_arr, Base64.DEFAULT);
//           // uploadCaptureImageWithRetrofit();
//        }
//    }
//
//    public void onCacelClick(View view) {
//        cropalertDialog.dismiss();
//    }
//
//    public void onRotationClick(View view) {
//        mCropImageView.rotateImage(-90);
//    }
//
//
//}












































package com.mrnovacrm.userprofile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrnovacrm.R;
import com.mrnovacrm.b2b_admin.AdminMenuScreenActivity;
import com.mrnovacrm.b2b_dealer.DealerMenuScreenActivity;
import com.mrnovacrm.b2b_delivery_dept.DeliveryMenuScreenActivity;
import com.mrnovacrm.b2b_dispatch_dept.DispatchMenuScreenActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceDeptMenuScreenActivity;
import com.mrnovacrm.b2b_superadmin.SuperAdminMenuScrenActivity;
import com.mrnovacrm.constants.CheckNetWork;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.ContactsModelDTO;
import com.mrnovacrm.model.EmpDetailsDTO;
import com.mrnovacrm.model.Login;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView profileimage;
    Context context;
    private String base64 = "";
    public static final int RequestPermissionCode = 1;
    private HashMap<String, String> values;
    private String PRIMARYID = "";
    private String USERTYPE = "";
    private String SHORTFORM = "";
    String USERNAME = "", MOBILE = "", DELIVERY_ADDRESS = "";

    //private TextView profilenametxt, mobilenumbertxt, emailtxt, addresstxt;

    GlobalShare globalShare;
    ImageView camimg;
    private boolean result;
    private static final int OPENGALLERY = 1;
    private static final int CAMERA_REQUEST = 2;
    private Uri selectedImageUri;
    private String selectedPath1;
    private Bitmap selectedImageBitmap;

    LinearLayout doblinear,
            sexlinear,
            fathernamelinear,
            mothernamelinear,
            martialllinear,
            companylinear,
            keypersonnamelinear,
            gstinlinear,
            regtypelinear,
            esilinear, empidlinear, dealeridlinear, dojlinear, departmentlinear, designationlinear, companyemaillinear,
            companycontactlinear, totalexplinear, reportingmanagerlinear, ofcemaillinear, officialcontactlinear,
            officialwhatsapplinear, areasalesmanagerlinear, areamanagercontactlinear,pflinear,licencelinear;

    TextView dobtxt, sextxt, fathernametxt, mothernametxt, martialtxt, addresstxt,
            countrytxt, statetxt, towncitytxt, pincodetxt, pannumbertxt, pfnumbertxt, esinumbertxt,
            banknametxt, accountnumbertxt, ifsccodetxt, employeeidtxt, dobjoiningtxt, departmenttxt, designationtxt,
            companyemailtxt, companycontacttxt, totalexptxt, reportingmanagertxt, profilenametxt,
            companynametxt,
            key_persontxt,
            dealeridtxt,
            officialemail_txt,
            officialcontact_txt,
            officialwhatsapp_txt,
            areasalesmanager_txt,
            areamanagercontact_txt,
            personalmobile_txt,
            personalemail_txt,regtypetxt,gsttxt,licencenumbertxt;
    private TransparentProgressDialog dialog;
    LinearLayout editlinear;

    public static Activity mainfinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        globalShare = (GlobalShare) getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);
        View includedLayout = findViewById(R.id.include_actionbar);
        ImageView backimg = includedLayout.findViewById(R.id.backimg);
        backimg.setOnClickListener(UserProfileActivity.this);
        mainfinish=this;
        editlinear = findViewById(R.id.editlinear);

        profileimage = findViewById(R.id.profileimage);
        licencenumbertxt = findViewById(R.id.licencenumbertxt);
        doblinear = findViewById(R.id.doblinear);
        sexlinear = findViewById(R.id.sexlinear);
        pflinear = findViewById(R.id.pflinear);
        licencelinear = findViewById(R.id.licencelinear);

        fathernamelinear = findViewById(R.id.fathernamelinear);
        mothernamelinear = findViewById(R.id.mothernamelinear);
        martialllinear = findViewById(R.id.martialllinear);
        companylinear = findViewById(R.id.companylinear);
        keypersonnamelinear = findViewById(R.id.keypersonnamelinear);
        gstinlinear = findViewById(R.id.gstinlinear);
        regtypelinear = findViewById(R.id.regtypelinear);
        esilinear = findViewById(R.id.esilinear);
        empidlinear = findViewById(R.id.empidlinear);
        dealeridlinear = findViewById(R.id.dealeridlinear);
        dojlinear = findViewById(R.id.dojlinear);
        departmentlinear = findViewById(R.id.departmentlinear);
        designationlinear = findViewById(R.id.designationlinear);
        companyemaillinear = findViewById(R.id.companyemaillinear);
        companycontactlinear = findViewById(R.id.companycontactlinear);
        totalexplinear = findViewById(R.id.totalexplinear);
        reportingmanagerlinear = findViewById(R.id.reportingmanagerlinear);
        ofcemaillinear = findViewById(R.id.ofcemaillinear);
        officialcontactlinear = findViewById(R.id.officialcontactlinear);
        officialwhatsapplinear = findViewById(R.id.officialwhatsapplinear);
        areasalesmanagerlinear = findViewById(R.id.areasalesmanagerlinear);
        areamanagercontactlinear = findViewById(R.id.areamanagercontactlinear);


        gsttxt = findViewById(R.id.gsttxt);
        regtypetxt = findViewById(R.id.regtypetxt);
        companynametxt = findViewById(R.id.companynametxt);
        key_persontxt = findViewById(R.id.key_persontxt);
        dealeridtxt = findViewById(R.id.dealeridtxt);
        officialemail_txt = findViewById(R.id.officialemail_txt);
        officialcontact_txt = findViewById(R.id.officialcontact_txt);
        officialwhatsapp_txt = findViewById(R.id.officialwhatsapp_txt);
        areasalesmanager_txt = findViewById(R.id.areasalesmanager_txt);
        areamanagercontact_txt = findViewById(R.id.areamanagercontact_txt);
        personalmobile_txt = findViewById(R.id.personalmobile_txt);
        personalemail_txt = findViewById(R.id.personalemail_txt);

        dobtxt = findViewById(R.id.dobtxt);
        sextxt = findViewById(R.id.sextxt);
        fathernametxt = findViewById(R.id.fathernametxt);
        mothernametxt = findViewById(R.id.mothernametxt);
        martialtxt = findViewById(R.id.martialtxt);
        addresstxt = findViewById(R.id.addresstxt);
        countrytxt = findViewById(R.id.countrytxt);
        statetxt = findViewById(R.id.statetxt);
        towncitytxt = findViewById(R.id.towncitytxt);
        pincodetxt = findViewById(R.id.pincodetxt);
        pannumbertxt = findViewById(R.id.pannumbertxt);
        pfnumbertxt = findViewById(R.id.pfnumbertxt);
        esinumbertxt = findViewById(R.id.esinumbertxt);
        banknametxt = findViewById(R.id.banknametxt);
        accountnumbertxt = findViewById(R.id.accountnumbertxt);
        ifsccodetxt = findViewById(R.id.ifsccodetxt);
        employeeidtxt = findViewById(R.id.employeeidtxt);
        dobjoiningtxt = findViewById(R.id.dobjoiningtxt);
        departmenttxt = findViewById(R.id.departmenttxt);
        designationtxt = findViewById(R.id.designationtxt);
        companyemailtxt = findViewById(R.id.companyemailtxt);
        companycontacttxt = findViewById(R.id.companycontacttxt);
        totalexptxt = findViewById(R.id.totalexptxt);
        reportingmanagertxt = findViewById(R.id.reportingmanagertxt);
        profilenametxt = findViewById(R.id.profilenametxt);

        addresstxt = findViewById(R.id.addresstxt);
        camimg = findViewById(R.id.camimg);

        try {
            if (SharedDB.isLoggedIn(getApplicationContext())) {
                values = SharedDB.getUserDetails(getApplicationContext());
                PRIMARYID = values.get(SharedDB.PRIMARYID);
                USERTYPE = values.get(SharedDB.USERTYPE);
                SHORTFORM = values.get(SharedDB.SHORTFORM);
                USERNAME = values.get(SharedDB.USERNAME);
                MOBILE = values.get(SharedDB.MOBILE);
                DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                String IMAGEURL = values.get(SharedDB.IMAGEURL);

                if (!IMAGEURL.equals("")) {
                    Picasso.with(context).load(IMAGEURL).memoryPolicy(MemoryPolicy.NO_STORE).into(profileimage);
                } else {
                    profileimage.setImageResource(R.drawable.noprofile);
                }
                profilenametxt.setText(USERNAME);
                //mobilenumbertxt.setText(MOBILE);
                addresstxt.setText(DELIVERY_ADDRESS);
            }
        } catch (Exception e) {
        }

        if(SHORTFORM.equals("DEALER"))
        {
            companylinear.setVisibility(View.VISIBLE);
            keypersonnamelinear.setVisibility(View.VISIBLE);
            gstinlinear.setVisibility(View.VISIBLE);
            regtypelinear.setVisibility(View.VISIBLE);
            doblinear.setVisibility(View.GONE);
            sexlinear.setVisibility(View.GONE);
            fathernamelinear.setVisibility(View.GONE);
            mothernamelinear.setVisibility(View.GONE);
            martialllinear.setVisibility(View.GONE);
            pflinear.setVisibility(View.GONE);
            esilinear.setVisibility(View.GONE);
            empidlinear.setVisibility(View.GONE);
            dojlinear.setVisibility(View.GONE);
            departmentlinear.setVisibility(View.GONE);
            designationlinear.setVisibility(View.GONE);
            companyemaillinear.setVisibility(View.GONE);
            companycontactlinear.setVisibility(View.GONE);
            totalexplinear.setVisibility(View.GONE);
            reportingmanagerlinear.setVisibility(View.GONE);

            ofcemaillinear.setVisibility(View.VISIBLE);
            officialcontactlinear.setVisibility(View.VISIBLE);
            officialwhatsapplinear.setVisibility(View.VISIBLE);
            areasalesmanagerlinear.setVisibility(View.VISIBLE);
            areamanagercontactlinear.setVisibility(View.VISIBLE);

            licencelinear.setVisibility(View.VISIBLE);
        }else{

            companylinear.setVisibility(View.GONE);
            keypersonnamelinear.setVisibility(View.GONE);
            doblinear.setVisibility(View.VISIBLE);
            sexlinear.setVisibility(View.VISIBLE);
            fathernamelinear.setVisibility(View.VISIBLE);
            mothernamelinear.setVisibility(View.VISIBLE);
            martialllinear.setVisibility(View.VISIBLE);
            licencelinear.setVisibility(View.GONE);
            gstinlinear.setVisibility(View.GONE);
            regtypelinear.setVisibility(View.GONE);
            pflinear.setVisibility(View.VISIBLE);
            esilinear.setVisibility(View.VISIBLE);
            empidlinear.setVisibility(View.VISIBLE);
            dojlinear.setVisibility(View.VISIBLE);
            departmentlinear.setVisibility(View.VISIBLE);
            designationlinear.setVisibility(View.VISIBLE);
            companyemaillinear.setVisibility(View.VISIBLE);
            companycontactlinear.setVisibility(View.VISIBLE);
            totalexplinear.setVisibility(View.VISIBLE);
            reportingmanagerlinear.setVisibility(View.VISIBLE);

            dealeridlinear.setVisibility(View.GONE);
            ofcemaillinear.setVisibility(View.GONE);
            officialcontactlinear.setVisibility(View.GONE);
            officialwhatsapplinear.setVisibility(View.GONE);
            areasalesmanagerlinear.setVisibility(View.GONE);
            areamanagercontactlinear.setVisibility(View.GONE);
        }


        camimg.setOnClickListener(UserProfileActivity.this);
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileFullScreenActivity.class);
                startActivity(intent);
            }
        });

        boolean isConnectedToInternet = CheckNetWork
                .isConnectedToInternet(getApplicationContext());
        if(isConnectedToInternet)
        {
            getEMPListDetails();
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        editlinear.setOnClickListener(UserProfileActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.camimg:
                selectImage();
                break;
            case R.id.backimg:
                recallMethod();
                break;
                case R.id.editlinear:
                    if(SHORTFORM.equals("DEALER")) {
                        Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), UpdateEmployeeProfileActivity.class);
                        startActivity(intent);
                    }
                break;
            default:
                break;
        }
    }

    private void getEMPListDetails() {
        final TransparentProgressDialog dialog = new TransparentProgressDialog(UserProfileActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = null;

        if(SHORTFORM.equals("DEALER"))
        {
            mService = mApiService.getDealerDetails(PRIMARYID);
        }else{
            mService = mApiService.getEMPDetails(PRIMARYID);
        }

        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dialog.dismiss();
                Log.e("response", "" + response);
                try {
                    Login mOrderObject = response.body();
                    String status = mOrderObject.getStatus();

                    Log.e("status", status);

                    if (Integer.parseInt(status) == 1) {
                             List<EmpDetailsDTO> empDetailsDTOS = mOrderObject.getEmpDetailsDTOS();
                             if(SHORTFORM.equals("DEALER"))
                             {
                                 List<ContactsModelDTO> contactsModelDTOS = mOrderObject.getContactsModelDTOList();
                                 if(contactsModelDTOS!=null)
                                 {
                                     if (contactsModelDTOS.size() > 0) {
                                         globalShare.setContactsModelDTOS(contactsModelDTOS);

                                         for (int i = 0; i < contactsModelDTOS.size(); i++) {
                                             String key_person = contactsModelDTOS.get(i).getKey_person();
                                             String ofc_contact = contactsModelDTOS.get(i).getOfc_contact();
                                             String ofc_email = contactsModelDTOS.get(i).getOfc_email();
                                             String ofc_whatsapp = contactsModelDTOS.get(i).getOfc_whatsapp();

                                             key_persontxt.setText(key_person);
                                             officialemail_txt.setText(ofc_email);
                                             officialcontact_txt.setText(ofc_contact);
                                             officialwhatsapp_txt.setText(ofc_whatsapp);
                                         }
                                     }
                                 }

                             }

                            if (empDetailsDTOS != null) {
                                if (empDetailsDTOS.size() > 0) {
                                    globalShare.setDealerDetailsDTOS(empDetailsDTOS);

                                    for (int i = 0; i < empDetailsDTOS.size(); i++) {
                                        String firstname = empDetailsDTOS.get(i).getFirst_name();
                                        String last_name = empDetailsDTOS.get(i).getLast_name();
                                        String uniq_id = empDetailsDTOS.get(i).getUniq_id();
                                        String branch = empDetailsDTOS.get(i).getBranch();
                                        String dept = empDetailsDTOS.get(i).getDept();
                                        String mobile = empDetailsDTOS.get(i).getMobile();
                                        String email = empDetailsDTOS.get(i).getEmail();
                                        String dob = empDetailsDTOS.get(i).getDob();
                                        String gender = empDetailsDTOS.get(i).getGender();
                                        String location = empDetailsDTOS.get(i).getLocation();
                                        String blood_group = empDetailsDTOS.get(i).getBlood_group();
                                        String father = empDetailsDTOS.get(i).getFather();
                                        String mother = empDetailsDTOS.get(i).getMother();
                                        String marital = empDetailsDTOS.get(i).getMarital();
                                        String expeience = empDetailsDTOS.get(i).getExpeience();
                                        String report_to = empDetailsDTOS.get(i).getReport_to();
                                        String doj = empDetailsDTOS.get(i).getDoj();
                                        String pan = empDetailsDTOS.get(i).getPan();
                                        String pf = empDetailsDTOS.get(i).getPf();
                                        String esi = empDetailsDTOS.get(i).getEsi();
                                        String bank_name = empDetailsDTOS.get(i).getBank_name();
                                        String bank_account = empDetailsDTOS.get(i).getBank_account();
                                        String ifsc = empDetailsDTOS.get(i).getIfsc();
                                        String address = empDetailsDTOS.get(i).getAddress();
                                        String addressproof1 = empDetailsDTOS.get(i).getAddressproof1();
                                        String addressproof2 = empDetailsDTOS.get(i).getAddressproof2();
                                        String role_id = empDetailsDTOS.get(i).getRole_id();
                                        String ofc_email = empDetailsDTOS.get(i).getOfc_email();
                                        String ofc_contact = empDetailsDTOS.get(i).getOfc_contact();
                                        String createdon = empDetailsDTOS.get(i).getCreatedon();
                                        String createdby = empDetailsDTOS.get(i).getCreatedby();
                                        String modifiedon = empDetailsDTOS.get(i).getModifiedon();
                                        String modifiedby = empDetailsDTOS.get(i).getModifiedby();
                                        String country = empDetailsDTOS.get(i).getCountry();
                                        String state = empDetailsDTOS.get(i).getState();
                                        String city = empDetailsDTOS.get(i).getCity();
                                        String pincode = empDetailsDTOS.get(i).getPincode();
                                        String gstin = empDetailsDTOS.get(i).getGstin();
                                        String company_name = empDetailsDTOS.get(i).getCompany_name();
                                      //  String key_person = empDetailsDTOS.get(i).getKey_person();
                                        String reg_type = empDetailsDTOS.get(i).getReg_type();
                                     //   String ofc_whatsapp = empDetailsDTOS.get(i).getOfc_whatsapp();
                                        String sales_manager = empDetailsDTOS.get(i).getSm();
                                        String am_contact = empDetailsDTOS.get(i).getAm_contact();
                                        String dealer_code = empDetailsDTOS.get(i).getDealer_code();
                                        String country_id = empDetailsDTOS.get(i).getCountry_id();
                                        String state_id = empDetailsDTOS.get(i).getState_id();
                                        String licence = empDetailsDTOS.get(i).getLicence();
                                        String area_sm = empDetailsDTOS.get(i).getSm();
                                        String designation = empDetailsDTOS.get(i).getDesignation();

                                          globalShare.setCountryid(country_id);
                                          globalShare.setStateid(state_id);

                                        if (SHORTFORM.equals("DEALER")) {
                                            companynametxt.setText(company_name);
                                            gsttxt.setText(gstin);
                                            regtypetxt.setText(reg_type);
                                            dealeridtxt.setText(dealer_code);

                                            areasalesmanager_txt.setText(area_sm);
                                            areamanagercontact_txt.setText(am_contact);
                                            licencenumbertxt.setText(licence);

                                        } else {
                                            dobtxt.setText(dob);
                                            sextxt.setText(gender);
                                            fathernametxt.setText(father);
                                            mothernametxt.setText(mother);
                                            martialtxt.setText(marital);
                                            pfnumbertxt.setText(pf);
                                            esinumbertxt.setText(esi);

                                            employeeidtxt.setText(uniq_id);
                                            dobjoiningtxt.setText(doj);
                                            departmenttxt.setText(dept);
                                            designationtxt.setText(USERTYPE);
                                            companyemailtxt.setText(ofc_email);
                                            companycontacttxt.setText(ofc_contact);
                                            totalexptxt.setText(expeience);
                                            reportingmanagertxt.setText(report_to);
                                        }
                                        addresstxt.setText(address);
                                        countrytxt.setText(country);
                                        statetxt.setText(state);
                                        towncitytxt.setText(city);
                                        pincodetxt.setText(pincode);
                                        pannumbertxt.setText(pan);
                                        banknametxt.setText(bank_name);
                                        accountnumbertxt.setText(bank_account);
                                        ifsccodetxt.setText(ifsc);
                                        personalmobile_txt.setText(mobile);
                                        personalemail_txt.setText(email);

                                    }
                                }
                            }

                    } else {
                        String messge = mOrderObject.getMessage();
                        Toast.makeText(getApplicationContext(), messge, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        recallMethod();
    }

    public void recallMethod() {
        if (DealerMenuScreenActivity.mainfinish != null) {
            DealerMenuScreenActivity.mainfinish.finish();
        }
        if (SHORTFORM.equals("DEALER") || SHORTFORM.equals("SE")) {
            globalShare.setDeliverymenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), DealerMenuScreenActivity.class);
            startActivity(intent);
        }

        if (FinanceDeptMenuScreenActivity.mainfinish != null) {
            FinanceDeptMenuScreenActivity.mainfinish.finish();
        }
        if (SHORTFORM.equals("FM")) {
            globalShare.setFinancemenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), FinanceDeptMenuScreenActivity.class);
            startActivity(intent);
        }

        if (DispatchMenuScreenActivity.mainfinish != null) {
            DispatchMenuScreenActivity.mainfinish.finish();
        }
        if (SHORTFORM.equals("PACKER")) {
            globalShare.setDispatchmenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), DispatchMenuScreenActivity.class);
            startActivity(intent);
        }

        if (DeliveryMenuScreenActivity.mainfinish != null) {
            DeliveryMenuScreenActivity.mainfinish.finish();
        }

        if (SHORTFORM.equals("DB")) {
            globalShare.setDeliverymenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), DeliveryMenuScreenActivity.class);
            startActivity(intent);
        }

        if (AdminMenuScreenActivity.mainfinish != null) {
            AdminMenuScreenActivity.mainfinish.finish();
        }

        if (SHORTFORM.equals("ADMIN")) {
            globalShare.setAdminmenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), AdminMenuScreenActivity.class);
            startActivity(intent);
        }

        if (SuperAdminMenuScrenActivity.mainfinish != null) {
            SuperAdminMenuScrenActivity.mainfinish.finish();
        }

        if (SHORTFORM.equals("SA")) {
            globalShare.setSuperadminmenuselectpos("1");
            Intent intent = new Intent(getApplicationContext(), SuperAdminMenuScrenActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private void selectImage() {
        final Utility Utility = new Utility();
        result = Utility.checkPermission(UserProfileActivity.this);
        final CharSequence[] items = {"Take Photo", "Choose from Gallery"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    boolean cam = Utility.checkCameraPermission(UserProfileActivity.this);
                    if (cam && result) {
                        openCamera(CAMERA_REQUEST);
                    } else {
                        SharedDB.showCustomAlert("Camera Permission in necessary", UserProfileActivity.this);
                    }
                } else if (items[item].equals("Choose from Gallery")) {
                    if (result) {
                        openGallery(OPENGALLERY);
                    }
                }
            }
        });
        builder.show();
    }

    public void openGallery(int req_code) {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(i, "Select file to upload "), req_code);
    }

    public void openCamera(int req_code) {
//        String fileName = System.currentTimeMillis() + ".jpg";
////        ContentValues values = new ContentValues();
////        values.put(MediaStore.Images.Media.TITLE, fileName);
////        selectedImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
////        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri);
////        startActivityForResult(cameraIntent, req_code);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, req_code);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == OPENGALLERY) {
                    selectedImageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver()
                            .query(selectedImageUri, filePathColumn, null,
                                    null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor
                                .getColumnIndex(filePathColumn[0]);
                        selectedPath1 = cursor.getString(columnIndex);
                        cursor.close();
                    }
                    selectedImageBitmap = decodeSampledBitmapFromFile(
                            selectedPath1, 300, 300);
                    profileimage.setImageBitmap(selectedImageBitmap);
                    updateProfileimage();
                } else if (requestCode == CAMERA_REQUEST) {
                    //     selectedPath1 = getRealPath(getApplicationContext(), selectedImageUri);

                    // Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Bundle extras = data.getExtras();
                    Bitmap photo = (Bitmap) extras.get("data");

                    profileimage.setImageBitmap(photo);
                    // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                    Uri tempUri = getImageUri(getApplicationContext(), photo);
                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    selectedPath1 = getRealPathFromURI(tempUri);
                    Log.e("selectedPath1", selectedPath1);
                    updateProfileimage();
                }
            }
        }catch (Exception e)
        {

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        try {
            if (getContentResolver() != null) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cursor.getString(idx);
                    cursor.close();
                }
            }
        }catch (Exception e)
        {
        }
        return path;
    }



    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                                     int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            // if(Math.round((float)width / (float)reqWidth) > inSampleSize) //
            // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    //For Camera
    public static String getRealPath(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private class Utility {
        static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        static final int MY_PERMISSIONS_REQUEST_CAMERA = 124;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }

        /**
         * Check if this device has a camera permission
         */
        private boolean checkCameraPermission(Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //code for allow
                } else {
                    //code for deny
                    SharedDB.showCustomAlert("External storage permission is necessary", UserProfileActivity.this);

                }
                break;
            }
            case Utility.MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    SharedDB.showCustomAlert("To capture image camera permission is necessary", UserProfileActivity.this);
                }
                break;
            }
        }
    }

    public void submitDetails() {
        String action = "edit";
        String encodedImage = null;
        if (selectedImageBitmap != null) {
            int width = selectedImageBitmap.getWidth();
            int height = selectedImageBitmap.getHeight();
            float xScale = ((float) 400) / width;
            float yScale = ((float) 300) / height;
            float scale = (xScale <= yScale) ? xScale : yScale;
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap scaledBitmap = Bitmap.createBitmap(selectedImageBitmap, 0, 0,
                    width, height, matrix, true);

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 90,
                    byteArray);
            byte[] byte_arr = byteArray.toByteArray();

            encodedImage = Base64
                    .encodeToString(byte_arr, Base64.DEFAULT);
        }

        File file = new File(selectedPath1);
        final TransparentProgressDialog dialog = new TransparentProgressDialog(UserProfileActivity.this);
        dialog.show();
        RetrofitAPI mApiService = SharedDB.getInterfaceService();


        Call<Login> mService = mApiService.updateUserProfile(PRIMARYID, SHORTFORM, encodedImage);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login mLoginObject = response.body();
                dialog.dismiss();
                try {
                    String status = mLoginObject.getStatus();
                    String message = mLoginObject.getMessage();
                    if (status.equals("1")) {
                        String dp = mLoginObject.getDp();

                        String ADDRESSID = values.get(SharedDB.ADDRESSID);
                        USERTYPE = values.get(SharedDB.USERTYPE);
                        SHORTFORM = values.get(SharedDB.SHORTFORM);
                        USERNAME = values.get(SharedDB.USERNAME);
                        MOBILE = values.get(SharedDB.MOBILE);
                        DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                        String COMPANY = values.get(SharedDB.COMPANY);
                        String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
                        String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
                        String BRANCHID = values.get(SharedDB.BRANCHID);
                        String PINCODE = values.get(SharedDB.PINCODE);
                        String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
                        String BRANCHCOUNT = values.get(SharedDB.BRANCHCOUNT);
                        String ROLECOUNT = values.get(SharedDB.ROLECOUNT);
                        String ROLEPKEY = values.get(SharedDB.ROLEPKEY);

                        SharedDB.loginSahred(getApplicationContext(), MOBILE, DELIVERY_ADDRESS, USERTYPE, "",
                                "", 0.0, 0.0, PRIMARYID, PINCODE, USERNAME, dp, ADDRESSID, BRANCHID,
                                BRANCHNAME, SHORTFORM, COMPANY, BRANCHCONTACT,COMPANIESLIST,BRANCHCOUNT,ROLECOUNT,ROLEPKEY);
                    } else {
                        // Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                dialog.dismiss();
                //  Toast.makeText(getApplicationContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateProfileimage()
    {
        dialog = new TransparentProgressDialog(UserProfileActivity.this);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    uploadFile();

                } catch (OutOfMemoryError e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                            if( dialog!=null)
                            {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public int uploadFile() {

        int serverResponseCode = 0;
        final HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        Log.e("selectedPath1",selectedPath1);

        File selectedFile = new File(selectedPath1);
        String[] parts = selectedPath1.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
                }
            });
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SharedDB.URL+"master/dp");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("dp", selectedPath1);
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"user\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(PRIMARYID);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"role\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(SHORTFORM);
                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"dp\";filename=\""
                        + fileName + "\"" + lineEnd);
                dataOutputStream.writeBytes(lineEnd);

//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"addressproof2\";filename=\""
//                        + fileName1 + "\"" + lineEnd);
//                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    try {
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(getApplicationContext(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                try {
                    serverResponseCode = connection.getResponseCode();
                } catch (OutOfMemoryError e) {
                    Toast.makeText(getApplicationContext(), "Memory Insufficient!", Toast.LENGTH_SHORT).show();
                }
                String serverResponseMessage = connection.getResponseMessage();

                Log.e(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder sb = new StringBuilder();
                            try {
                                BufferedReader rd = new BufferedReader(new InputStreamReader(connection
                                        .getInputStream()));
                                String line;
                                while ((line = rd.readLine()) != null) {
                                    sb.append(line);
                                }
                                rd.close();
                            } catch (IOException ioex) {
                            }
                            //      Log.e("response", "" + sb.toString());

                            String response = sb.toString();

                            int statusValue = 0;
                            String messageValue = "";
                            try {
                                JSONObject res_object = new JSONObject(response);

                                if (res_object.has("status")) {
                                    statusValue = res_object.getInt("status");
                                    Log.e("statusValue", "" + statusValue);
                                }

                                if (res_object.has("message")) {
                                    messageValue = res_object.getString("message");
                                    Log.e("messageValue", messageValue);
                                }
                                if (statusValue == 1) {
                                    Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();
                                    String dp = res_object.getString("dp");

                                    Log.e("dp",dp);

                                    String ADDRESSID = values.get(SharedDB.ADDRESSID);
                                    USERTYPE = values.get(SharedDB.USERTYPE);
                                    SHORTFORM = values.get(SharedDB.SHORTFORM);
                                    USERNAME = values.get(SharedDB.USERNAME);
                                    MOBILE = values.get(SharedDB.MOBILE);
                                    DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                                    String COMPANY = values.get(SharedDB.COMPANY);
                                    String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
                                    String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
                                    String BRANCHID = values.get(SharedDB.BRANCHID);
                                    String PINCODE = values.get(SharedDB.PINCODE);
                                    String LATITUDE = values.get(SharedDB.LATITUDE);
                                    String LONGITIDE = values.get(SharedDB.LONGITIDE);
                                    String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
                                    String BRANCHCOUNT = values.get(SharedDB.BRANCHCOUNT);
                                    String ROLECOUNT = values.get(SharedDB.ROLECOUNT);
                                    String ROLEPKEY = values.get(SharedDB.ROLEPKEY);

                                    SharedDB.loginSahred(getApplicationContext(), MOBILE, DELIVERY_ADDRESS, USERTYPE, "",
                                            "", Double.parseDouble(LATITUDE), Double.parseDouble(LONGITIDE), PRIMARYID, PINCODE, USERNAME, dp, ADDRESSID, BRANCHID,
                                            BRANCHNAME, SHORTFORM, COMPANY, BRANCHCONTACT,COMPANIESLIST,BRANCHCOUNT,ROLECOUNT,ROLEPKEY);
                                } else {
                                    Toast.makeText(getApplicationContext(), messageValue, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if(dialog!=null)
            {
                dialog.dismiss();
            }
            return serverResponseCode;
        }
    }
}