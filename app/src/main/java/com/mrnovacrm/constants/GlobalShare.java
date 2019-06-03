package com.mrnovacrm.constants;

import android.app.Application;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mrnovacrm.model.ContactsModelDTO;
import com.mrnovacrm.model.EmpDetailsDTO;
import com.mrnovacrm.model.RolesModelDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import android.support.multidex.MultiDex;

public class GlobalShare extends Application {

    private RequestQueue mRequestQueue;
    private static GlobalShare mInstance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    String branchidval;

    String notificationfrom;



    public String getNotificationfrom() {
        return notificationfrom;
    }

    public void setNotificationfrom(String notificationfrom) {
        this.notificationfrom = notificationfrom;
    }

    String pickMenuSelectedPos;

    public String getPickMenuSelectedPos() {
        return pickMenuSelectedPos;
    }

    public void setPickMenuSelectedPos(String pickMenuSelectedPos) {
        this.pickMenuSelectedPos = pickMenuSelectedPos;
    }

    public static synchronized GlobalShare getInstance() {
        return mInstance;
    }

    //private static GlobalShare mInstance;

    public static final String TAG = GlobalShare.class
            .getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    List<String> imagesList;

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    String btfrom;

    public String getBtfrom() {
        return btfrom;
    }

    public void setBtfrom(String btfrom) {
        this.btfrom = btfrom;
    }

    String dealerMenuSelectedPos;

    public String getDealerMenuSelectedPos() {
        return dealerMenuSelectedPos;
    }

    public void setDealerMenuSelectedPos(String dealerMenuSelectedPos) {
        this.dealerMenuSelectedPos = dealerMenuSelectedPos;
    }

    String selectMenuFromPacker;

    public String getSelectMenuFromPacker() {
        return selectMenuFromPacker;
    }

    public void setSelectMenuFromPacker(String selectMenuFromPacker) {
        this.selectMenuFromPacker = selectMenuFromPacker;
    }

    String selectMenuFromSeller;
    String deliverymenuselectpos;

    String financemenuselectpos;


    public String getSelectMenuFromSeller() {
        return selectMenuFromSeller;
    }

    public void setSelectMenuFromSeller(String selectMenuFromSeller) {
        this.selectMenuFromSeller = selectMenuFromSeller;
    }

    public String getDeliverymenuselectpos() {
        return deliverymenuselectpos;
    }

    public void setDeliverymenuselectpos(String deliverymenuselectpos) {
        this.deliverymenuselectpos = deliverymenuselectpos;
    }

    String cancelorderfrom;

    public String getCancelorderfrom() {
        return cancelorderfrom;
    }

    public void setCancelorderfrom(String cancelorderfrom) {
        this.cancelorderfrom = cancelorderfrom;
    }

    public String getFinancemenuselectpos() {
        return financemenuselectpos;
    }

    public void setFinancemenuselectpos(String financemenuselectpos) {
        this.financemenuselectpos = financemenuselectpos;
    }

    String dispatchmenuselectpos;
    String dispatch_menuselectpos_test;

    public String getDispatchmenuselectpos() {
        return dispatchmenuselectpos;
    }

    public void setDispatchmenuselectpos(String dispatchmenuselectpos) {
        this.dispatchmenuselectpos = dispatchmenuselectpos;
    }

    String salesmenuselectpos;

    public String getSalesmenuselectpos() {
        return salesmenuselectpos;
    }

    public void setSalesmenuselectpos(String salesmenuselectpos) {
        this.salesmenuselectpos = salesmenuselectpos;
    }

    String adminmenuselectpos;
    String superadminmenuselectpos;

    public String getAdminmenuselectpos() {
        return adminmenuselectpos;
    }

    public void setAdminmenuselectpos(String adminmenuselectpos) {
        this.adminmenuselectpos = adminmenuselectpos;
    }

    public String getSuperadminmenuselectpos() {
        return superadminmenuselectpos;
    }

    public void setSuperadminmenuselectpos(String superadminmenuselectpos) {
        this.superadminmenuselectpos = superadminmenuselectpos;
    }

    String selectedBranchid;

    public String getSelectedBranchid() {
        return selectedBranchid;
    }

    public void setSelectedBranchid(String selectedBranchid) {
        this.selectedBranchid = selectedBranchid;
    }

  //  String dealersFrom;

//    public String getDealersFrom() {
//        return dealersFrom;
//    }
//
//    public void setDealersFrom(String dealersFrom) {
//        this.dealersFrom = dealersFrom;
//    }

    String selectCategoryId;

    public String getSelectCategoryId() {
        return selectCategoryId;
    }

    public void setSelectCategoryId(String selectCategoryId) {
        this.selectCategoryId = selectCategoryId;
    }

    ArrayList<HashMap<String, String>> dealershashmapList;

    public ArrayList<HashMap<String, String>> getDealershashmapList() {
        return dealershashmapList;
    }

    public void setDealershashmapList(ArrayList<HashMap<String, String>> dealershashmapList) {
        this.dealershashmapList = dealershashmapList;
    }


    List<EmpDetailsDTO> dealerDetailsDTOS;

    public List<EmpDetailsDTO> getDealerDetailsDTOS() {
        return dealerDetailsDTOS;
    }

    public void setDealerDetailsDTOS(List<EmpDetailsDTO> dealerDetailsDTOS) {
        this.dealerDetailsDTOS = dealerDetailsDTOS;
    }

    List<ContactsModelDTO> contactsModelDTOS;

    public List<ContactsModelDTO> getContactsModelDTOS() {
        return contactsModelDTOS;
    }

    public void setContactsModelDTOS(List<ContactsModelDTO> contactsModelDTOS) {
        this.contactsModelDTOS = contactsModelDTOS;
    }

    String countryid;
    String stateid;

    public String getCountryid() {
        return countryid;
    }

    public void setCountryid(String countryid) {
        this.countryid = countryid;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    List<ContactsModelDTO> companiesList;

    List<RolesModelDTO> rolesList;

    public List<ContactsModelDTO> getCompaniesList() {
        return companiesList;
    }

    public void setCompaniesList(List<ContactsModelDTO> companiesList) {
        this.companiesList = companiesList;
    }

    ArrayList<HashMap<String,String>> invoicesList;

    public ArrayList<HashMap<String, String>> getInvoicesList() {
        return invoicesList;
    }

    public void setInvoicesList(ArrayList<HashMap<String, String>> invoicesList) {
        this.invoicesList = invoicesList;
    }

    public String getBranchidval() {
        return branchidval;
    }

    public void setBranchidval(String branchidval) {
        this.branchidval = branchidval;
    }

    public List<RolesModelDTO> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<RolesModelDTO> rolesList) {
        this.rolesList = rolesList;
    }

    String loginselectedfromval;

    public String getLoginselectedfromval() {
        return loginselectedfromval;
    }

    public void setLoginselectedfromval(String loginselectedfromval) {
        this.loginselectedfromval = loginselectedfromval;
    }

    public String getDispatch_menuselectpos_test() {
        return dispatch_menuselectpos_test;
    }

    public void setDispatch_menuselectpos_test(String dispatch_menuselectpos_test) {
        this.dispatch_menuselectpos_test = dispatch_menuselectpos_test;
    }

    public void clearApplicationData() {

        try {
            File cache = getCacheDir();
            File appDir = new File(cache.getParent());
            if (appDir.exists()) {
                String[] children = appDir.list();
                for (String s : children) {
                    if (!s.equals("lib")) {
                        deleteDir(new File(appDir, s));
                        //	Log.e("TAG", "File /data/data/com.hyderabadcnd/" + s +" DELETED");
                    }
                }
            }
        }catch (Exception e)
        {

        }
    }

    public static boolean deleteDir(File dir) {
        try {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        }catch (Exception e)
        {

        }
        return dir.delete();
    }
}