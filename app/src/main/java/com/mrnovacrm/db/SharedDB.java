package com.mrnovacrm.db;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.mrnovacrm.R;
import com.mrnovacrm.constants.RetrofitAPI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/**
 * Created by prasad on 2/27/2018.
 */
public class SharedDB {
    // public static final String URL = "http://mrinfotechs.in/nova_v1/";
//   public static final String URL = "http://mrinfotechs.in/nova_test/";
   public static final String URL = "http://jujube.co.in/nova_v1/";
   // public static final String URL = "http://mitrayainfo.com/nova_v1/";
    public static final String defaultVal = "NO VALUE";
    private static final String IS_VALID = "IsVerified";
    public static final String Name = "name";
    public static final String USERID = "user_id";
    public static final String MOBILE = "mobileno";
    public static final String EMAILID = "emailid";
    public static final String IMAGE = "image";
    public static final String INTERESTSTATUS = "intereststatus";
    //public static final String CALLNEWSFEED = "newsfeed";
    public static final String USERTYPE = "userType";
    public static SharedPreferences loginsharedpreferences;
    public static final String LOGINPREFERENCES = "loginPrefs";
    static SharedPreferences.Editor logineditor;
    public static SharedPreferences dealersharedpreferences;
    public static final String DEALERPREFERENCES = "dealerPrefs";
    static SharedPreferences.Editor dealereditor;
    private static final String IS_DEALERVALID = "IsVerified";
    private static final String IS_MULTIROLEVALID = "IsVerified";
    public static final String DELIVERY_ADDRESS = "deliveryaddress";
    public static final String HOUSENO = "houseno";
    public static final String LANDMARK = "landmark";
    public static final String LATITUDE = "latitude";
    public static final String LONGITIDE = "longitude";
    public static final String PRIMARYID = "primaryId";
    public static final String PINCODE = "pincode";
    public static final String USERNAME = "userName";
    public static final String IMAGEURL = "imageurl";
    public static final String ADDRESSID = "address_id";
    public static final String BRANCHID = "branch_id";
    public static final String BRANCHNAME = "branch_name";
    public static final String SHORTFORM = "short_form";
    public static final String DEALERID = "dealerid";
    public static final String DEALERNAME = "dealername";
    public static final String DEALERMOBILE = "dealermobile";
    public static final String COMPANY = "company";
    public static final String COMPANYID = "company_id";
    public static final String BRANCHCONTACT = "branch_contact";
    public static final String COMPANIESLIST = "companieslist";
    public static final String ROLECOUNT = "rolecount";
    public static final String ROLEPKEY = "rolepkey";
    public static final String BRANCHCOUNT = "branchcount";
    public static final String ROLE_COUNT = "rolecount";
    public static final String BRANCH_COUNT = "branchcount";
    public static SharedPreferences multirolesharedpreferences;
    public static final String MULTIPREFERENCES = "multirolePrefs";
    static SharedPreferences.Editor multiroleeditor;

    public static RetrofitAPI getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final RetrofitAPI mInterfaceService = retrofit.create(RetrofitAPI.class);
        return mInterfaceService;
    }
    public static void loginSahred(Context context, String mobilenumber,
                                   String deliveryaddress, String user_type,
                                   String house_no, String _landmark, double latitude,
                                   double longitude, String primaryId, String pincode, String username, String imageurl,
                                   String address_id,String branch_id,String branch_name,String short_form,
                                   String company,String branch_contact,String companieslist,
                                   String branch_count,String role_count,String rolepkey) {
        try {
            loginsharedpreferences = context.getSharedPreferences(
                    LOGINPREFERENCES, Context.MODE_PRIVATE);
            logineditor = loginsharedpreferences.edit();
            logineditor.putString(MOBILE, mobilenumber);
            logineditor.putString(DELIVERY_ADDRESS, deliveryaddress);
            logineditor.putString(USERTYPE, user_type);
            logineditor.putString(HOUSENO, house_no);
            logineditor.putBoolean(IS_VALID, true);
            logineditor.putString(LANDMARK, _landmark);
            logineditor.putString(LATITUDE, String.valueOf(latitude));
            logineditor.putString(LONGITIDE, String.valueOf(longitude));
            logineditor.putString(PRIMARYID, String.valueOf(primaryId));
            logineditor.putString(PINCODE, pincode);
            logineditor.putString(USERNAME, username);
            logineditor.putString(IMAGEURL, imageurl);
            logineditor.putString(ADDRESSID, address_id);
            logineditor.putString(BRANCHID, branch_id);
            logineditor.putString(BRANCHNAME, branch_name);
            logineditor.putString(SHORTFORM, short_form);
            logineditor.putString(COMPANY, company);
            logineditor.putString(BRANCHCONTACT, branch_contact);
            logineditor.putString(COMPANIESLIST, companieslist);
            logineditor.putString(BRANCHCOUNT, branch_count);
            logineditor.putString(ROLECOUNT, role_count);
            logineditor.putString(ROLEPKEY, rolepkey);
           logineditor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> getUserDetails(Context context) {
        HashMap<String, String> user = new HashMap<String, String>();
        loginsharedpreferences = context.getSharedPreferences(LOGINPREFERENCES,
                Context.MODE_PRIVATE);
        user.put(DELIVERY_ADDRESS, loginsharedpreferences.getString(DELIVERY_ADDRESS, defaultVal));
        user.put(MOBILE, loginsharedpreferences.getString(MOBILE, defaultVal));
        user.put(USERNAME, loginsharedpreferences.getString(USERNAME, defaultVal));
        user.put(USERTYPE, loginsharedpreferences.getString(USERTYPE, defaultVal));
        user.put(HOUSENO, loginsharedpreferences.getString(HOUSENO, defaultVal));
        user.put(LANDMARK, loginsharedpreferences.getString(LANDMARK, defaultVal));
        user.put(LATITUDE, loginsharedpreferences.getString(LATITUDE, defaultVal));
        user.put(LONGITIDE, loginsharedpreferences.getString(LONGITIDE, defaultVal));
        user.put(PRIMARYID, loginsharedpreferences.getString(PRIMARYID, defaultVal));
        user.put(PINCODE, loginsharedpreferences.getString(PINCODE, defaultVal));
        user.put(IMAGEURL, loginsharedpreferences.getString(IMAGEURL, defaultVal));
        user.put(ADDRESSID, loginsharedpreferences.getString(ADDRESSID, defaultVal));
        user.put(BRANCHID, loginsharedpreferences.getString(BRANCHID, defaultVal));
        user.put(BRANCHNAME, loginsharedpreferences.getString(BRANCHNAME, defaultVal));
        user.put(SHORTFORM, loginsharedpreferences.getString(SHORTFORM, defaultVal));
        user.put(COMPANY, loginsharedpreferences.getString(COMPANY, defaultVal));
        user.put(BRANCHCONTACT, loginsharedpreferences.getString(BRANCHCONTACT, defaultVal));
        user.put(COMPANIESLIST, loginsharedpreferences.getString(COMPANIESLIST, defaultVal));
        user.put(BRANCHCOUNT, loginsharedpreferences.getString(BRANCHCOUNT, defaultVal));
        user.put(ROLECOUNT, loginsharedpreferences.getString(ROLECOUNT, defaultVal));
        user.put(ROLEPKEY, loginsharedpreferences.getString(ROLEPKEY, defaultVal));
        return user;
    }

    // Get Login State
    public static boolean isLoggedIn(Context context) {
        loginsharedpreferences = context.getSharedPreferences(LOGINPREFERENCES,
                Context.MODE_PRIVATE);
        return loginsharedpreferences.getBoolean(IS_VALID, false);
    }

    public static void clearAuthentication(Context context) {
        try {
            loginsharedpreferences = context.getSharedPreferences(
                    LOGINPREFERENCES, Context.MODE_PRIVATE);
            logineditor = loginsharedpreferences.edit();
            logineditor.clear();
            logineditor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static boolean dateComparision(String fromdate, String todate)
    {
        boolean valid=false;
        String date = fromdate;
        String dateafter =todate;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy");
        Date convertedDate = new Date();
        Date convertedDate2 = new Date();
        try {
            convertedDate = dateFormat.parse(date);
            convertedDate2 = dateFormat.parse(dateafter);
            if (convertedDate2.after(convertedDate)) {
                valid=true;
            } else {
                valid=false;
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return valid;
    }

    public static void dealerSahred(Context context, String dealerid,String dealername,String dealermobile) {
        try {
            dealersharedpreferences = context.getSharedPreferences(
                    DEALERPREFERENCES, Context.MODE_PRIVATE);
            dealereditor = dealersharedpreferences.edit();
            dealereditor.putString(DEALERID, dealerid);
            dealereditor.putString(DEALERNAME, dealername);
            dealereditor.putString(DEALERMOBILE, dealermobile);
            dealereditor.putBoolean(IS_DEALERVALID, true);
            dealereditor.commit();
        }catch (Exception e)
        {
        }
    }

    public static HashMap<String, String> getDealerDetails(Context context) {
        HashMap<String, String> user = new HashMap<String, String>();
        dealersharedpreferences = context.getSharedPreferences(DEALERPREFERENCES,
                Context.MODE_PRIVATE);
        user.put(DEALERID, dealersharedpreferences.getString(DEALERID, defaultVal));
        user.put(DEALERNAME, dealersharedpreferences.getString(DEALERNAME, defaultVal));
        user.put(DEALERMOBILE, dealersharedpreferences.getString(DEALERMOBILE, defaultVal));
        return user;
    }

    // Get Login State
    public static boolean isDealerExists(Context context) {
        dealersharedpreferences = context.getSharedPreferences(DEALERPREFERENCES,
                Context.MODE_PRIVATE);
        return dealersharedpreferences.getBoolean(IS_DEALERVALID, false);
    }

    public static void dealerclearAuthentication(Context context) {
        try {
            dealersharedpreferences = context.getSharedPreferences(
                    DEALERPREFERENCES, Context.MODE_PRIVATE);
            dealereditor = dealersharedpreferences.edit();
            dealereditor.clear();
            dealereditor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void multiroleSahred(Context context, String branch_id,String branch_name,String short_form,
                                       String company,String company_id,String rolecount,String branchcount) {
        try {
            multirolesharedpreferences = context.getSharedPreferences(
                    MULTIPREFERENCES, Context.MODE_PRIVATE);
            multiroleeditor = multirolesharedpreferences.edit();
            multiroleeditor.putString(BRANCHID, branch_id);
            multiroleeditor.putString(BRANCHNAME, branch_name);
            multiroleeditor.putString(SHORTFORM, short_form);
            multiroleeditor.putString(COMPANY, company);
            multiroleeditor.putString(COMPANYID, company_id);
            multiroleeditor.putString(ROLE_COUNT, rolecount);
            multiroleeditor.putString(BRANCH_COUNT, branchcount);
            multiroleeditor.putBoolean(IS_MULTIROLEVALID, true);
            multiroleeditor.commit();
        }catch (Exception e)
        {
        }
    }

    public static HashMap<String, String> getMultiroleDetails(Context context) {
        HashMap<String, String> user = new HashMap<String, String>();
        multirolesharedpreferences = context.getSharedPreferences(MULTIPREFERENCES,
                Context.MODE_PRIVATE);
        user.put(BRANCHID, multirolesharedpreferences.getString(BRANCHID, defaultVal));
        user.put(BRANCHNAME, multirolesharedpreferences.getString(BRANCHNAME, defaultVal));
        user.put(SHORTFORM, multirolesharedpreferences.getString(SHORTFORM, defaultVal));
        user.put(COMPANY, multirolesharedpreferences.getString(COMPANY, defaultVal));
        user.put(COMPANYID, multirolesharedpreferences.getString(COMPANYID, defaultVal));
        user.put(ROLE_COUNT, multirolesharedpreferences.getString(ROLE_COUNT, defaultVal));
        user.put(BRANCH_COUNT, multirolesharedpreferences.getString(BRANCH_COUNT, defaultVal));
        return user;
    }

    // Get Login State
    public static boolean isMultiroleExists(Context context) {
        multirolesharedpreferences = context.getSharedPreferences(MULTIPREFERENCES,
                Context.MODE_PRIVATE);
        return multirolesharedpreferences.getBoolean(IS_MULTIROLEVALID, false);
    }

    public static void clearMultiroleAuthentication(Context context) {
        try {
            multirolesharedpreferences = context.getSharedPreferences(
                    MULTIPREFERENCES, Context.MODE_PRIVATE);
            multiroleeditor = multirolesharedpreferences.edit();
            multiroleeditor.clear();
            multiroleeditor.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void showCustomAlert(String message,Activity context)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        // Call toast.xml file for toast layout
        View view = inflater.inflate(R.layout.customtoast, null);
        TextView textView=(TextView)view.findViewById(R.id.toasttext);
        textView.setText(message);
        Toast toast = new Toast(context);
        // Set layout to toast
        toast.setView(view);
        toast.setGravity(Gravity.BOTTOM,
                0, 120);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}