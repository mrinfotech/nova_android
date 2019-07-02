package com.mrnovacrm.activity;

/**
 * Created by prasad on 2/27/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mrnovacrm.R;
import com.mrnovacrm.adapter.NavigationDrawerAdapter;
import com.mrnovacrm.adapter.SpinnerItemsAdapternew;
import com.mrnovacrm.b2b_admin.AdminMenuScreenActivity;
import com.mrnovacrm.b2b_dealer.DealerMenuScreenActivity;
import com.mrnovacrm.b2b_delivery_dept.DeliveryMenuScreenActivity;
import com.mrnovacrm.b2b_dispatch_dept.DispatchMenuScreenActivity;
import com.mrnovacrm.b2b_finance_dept.FinanceDeptMenuScreenActivity;
import com.mrnovacrm.b2b_superadmin.SuperAdminMenuScrenActivity;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.RetrofitAPI;
import com.mrnovacrm.db.SharedDB;
import com.mrnovacrm.model.Login;
import com.mrnovacrm.model.NavDrawerItem;
import com.mrnovacrm.model.RolesModelDTO;
import com.mrnovacrm.utils.Config;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    static DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private static View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private String USERTYPE = "";
    private String PRIMARYID = "";
    Context context;
    int SELECTEDPOSITION = 0;
    boolean isroleselected=true;

    int[] menuicons = {R.drawable.menu_profile,
            R.drawable.menu_home,
            R.drawable.menu_products,
            R.drawable.menu_orders,
            R.drawable.menu_receivedorders,
            R.drawable.menu_trackorders,
            R.drawable.menu_invoice,
            R.drawable.menu_wallet,
            R.drawable.menu_notification,
            R.drawable.menu_changepassword,
            R.drawable.menu_logout
    };

    int[] financedept_menuicons = {R.drawable.menu_profile,
            R.drawable.menu_dashboard,
            R.drawable.menu_managedispatch,
            R.drawable.menu_managetransport,
            R.drawable.menu_invoice,
            R.drawable.menu_notification,
            R.drawable.menu_changepassword,
            R.drawable.menu_logout
    };

    int[] dispatchdept_menuicons = {R.drawable.menu_profile,
            R.drawable.menu_packoredrs,
            R.drawable.menu_packhistory,
            R.drawable.menu_deliveryorders,
            R.drawable.menu_deliveryhistory,
            R.drawable.menu_deliveryhistory,
            R.drawable.menu_invoice,
            R.drawable.menu_managetransport,
            R.drawable.menu_managelrnumber,
       /*     R.drawable.menu_notification,*/
            R.drawable.menu_changepassword,
            R.drawable.menu_logout
    };

    int[] deliverydept_menuicons = {R.drawable.menu_profile,
            R.drawable.menu_home,
            R.drawable.menu_products,
            R.drawable.menu_products,
            // R.drawable.menu_products,
            R.drawable.menu_changepassword,
            R.drawable.menu_logout
    };

    int[] salesmenuicons = {R.drawable.menu_profile,
            R.drawable.menu_home,
            R.drawable.menu_products,
            R.drawable.menu_orders,
            R.drawable.menu_dealerslist,
            R.drawable.menu_dealerslist,
            R.drawable.menu_dealerslist,
            R.drawable.menu_delerregistration,
            R.drawable.menu_invoice,
            R.drawable.menu_invoice,
            R.drawable.menu_trackorders,
            R.drawable.menu_wallet,
            R.drawable.menu_notification,
            R.drawable.menu_changepassword,
            R.drawable.menu_logout
    };

    int[] adminmenuicons = {R.drawable.menu_profile,
            R.drawable.menu_dashboard,
            R.drawable.menu_managedepotmanager,
            R.drawable.menu_invoice,
            R.drawable.menu_grade,
            //R.drawable.menu_notification,
            R.drawable.menu_changepassword,
            R.drawable.menu_logout
    };

    int[] superadminmenuicons = {R.drawable.menu_profile,
            R.drawable.menu_dashboard,
            R.drawable.menu_managebranch,
            R.drawable.menu_managedepotmanager,
            R.drawable.menu_dealerslist,
            R.drawable.menu_invoice,
          /*  R.drawable.menu_notification,*/
            R.drawable.menu_changepassword,
            R.drawable.menu_logout
    };

    RelativeLayout nav_header_container;
    private CircleImageView profileimage;
    private TextView usernametxt, roletxt, branchtxt;
    Spinner employeesspinner;
    private String BRANCHID;
    private String ROLEPKEY;
    GlobalShare globalShare;
    private String SHORTFORM;
    private String USERNAME;
    private String BRANCHNAME;
    boolean isroleloaded=true;

    public FragmentDrawer() {
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();
        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        globalShare = (GlobalShare) getActivity().getApplicationContext();

        nav_header_container = layout.findViewById(R.id.nav_header_container);
        profileimage = layout.findViewById(R.id.profileimage);
        employeesspinner = layout.findViewById(R.id.employeesspinner);

        usernametxt = layout.findViewById(R.id.usernametxt);
        roletxt = layout.findViewById(R.id.roletxt);
        branchtxt = layout.findViewById(R.id.branchtxt);

        HashMap<String, String> hashMapValues = SharedDB.getUserDetails(getActivity());
        USERTYPE = hashMapValues.get(SharedDB.USERTYPE);
        PRIMARYID = hashMapValues.get(SharedDB.PRIMARYID);
        USERNAME = hashMapValues.get(SharedDB.USERNAME);
        String IMAGEURL = hashMapValues.get(SharedDB.IMAGEURL);
        SHORTFORM = hashMapValues.get(SharedDB.SHORTFORM);
        BRANCHNAME = hashMapValues.get(SharedDB.BRANCHNAME);
        BRANCHID = hashMapValues.get(SharedDB.BRANCHID);
        ROLEPKEY = hashMapValues.get(SharedDB.ROLEPKEY);
        try {
            if (!IMAGEURL.equals("")) {
                Picasso.with(getActivity()).load(IMAGEURL).into(profileimage);
            } else {
                profileimage.setImageResource(R.drawable.noprofile);
            }
        } catch (Exception e) {
        }

        usernametxt.setText(USERNAME);
        branchtxt.setText("Branch : " + BRANCHNAME);

        if (SHORTFORM.equals("ADMIN")||SHORTFORM.equals("PACKER")|| SHORTFORM.equals("FM")|| SHORTFORM.equals("SE")) {
            branchtxt.setVisibility(View.GONE);
            employeesspinner.setVisibility(View.VISIBLE);
                loadRoles();
        }else{
            branchtxt.setVisibility(View.VISIBLE);
            employeesspinner.setVisibility(View.GONE);
        }
        showMenuData();
        return layout;
    }

    public void showMenuData() {
        if (SHORTFORM.equals("DEALER")) {
            titles = getActivity().getResources().getStringArray(R.array.dealer_nav_drawer_labels);
            adapter = new NavigationDrawerAdapter(getActivity(), getData(), menuicons);
            roletxt.setText("Dealer");
        } else if (SHORTFORM.equals("FM")) {
            titles = getActivity().getResources().getStringArray(R.array.finance_nav_drawer_labels);
            adapter = new NavigationDrawerAdapter(getActivity(), getData(), financedept_menuicons);
            //  roletxt.setText("HR & Finance Manager");
            roletxt.setText("Depot Manager");
        } else if (SHORTFORM.equals("PACKER")) {
            titles = getActivity().getResources().getStringArray(R.array.dispatch_nav_drawer_labels);
            adapter = new NavigationDrawerAdapter(getActivity(), getData(), dispatchdept_menuicons);
            roletxt.setText("Dispatcher");
        } else if (SHORTFORM.equals("DB")) {
            titles = getActivity().getResources().getStringArray(R.array.delivery_nav_drawer_labels);
            adapter = new NavigationDrawerAdapter(getActivity(), getData(), deliverydept_menuicons);
            roletxt.setText("Delivery Boy");
        } else if (SHORTFORM.equals("SE")) {
            titles = getActivity().getResources().getStringArray(R.array.sales_nav_drawer_labels);
            adapter = new NavigationDrawerAdapter(getActivity(), getData(), salesmenuicons);
            roletxt.setText("Sales Executive");
        } else if (SHORTFORM.equals("ADMIN")) {
            titles = getActivity().getResources().getStringArray(R.array.admin_nav_drawer_labels);
            adapter = new NavigationDrawerAdapter(getActivity(), getData(), adminmenuicons);
            roletxt.setText("Finance Admin");

        } else if (SHORTFORM.equals("SA")) {
            titles = getActivity().getResources().getStringArray(R.array.superadmin_nav_drawer_labels);
            adapter = new NavigationDrawerAdapter(getActivity(), getData(), superadminmenuicons);
            roletxt.setText("Finance Manager");
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
                showMenuData();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
                showMenuData();
                hideKeyboard();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
                hideKeyboard();
                showMenuData();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        //  clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public static void closeDrawer() {
        mDrawerLayout.closeDrawer(containerView);
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void loadRoles() {
        isroleloaded=false;
        RetrofitAPI mApiService = SharedDB.getInterfaceService();
        Call<Login> mService = mApiService.getEmpRolesList(PRIMARYID);
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                try {
                    Log.e("response",""+response);
                    Login mLoginObject = response.body();
                    String status = mLoginObject.getStatus();
                    Log.e("Status", " :" + status);
                    String message = mLoginObject.getMessage();
                    if (status.equals("1")) {
                        try {
                            String role_count = mLoginObject.getRole_count();
                            String branch_count = mLoginObject.getBranch_count();
                            List<RolesModelDTO> rolesDTOLis = mLoginObject.getRolesDTOLis();
                            if (rolesDTOLis != null) {
                                if (rolesDTOLis.size() > 0) {
                                    ArrayList<String> rolebranchList = new ArrayList<>();
                                    ArrayList<String> branch_idsList = new ArrayList<>();
                                    ArrayList<String> branchnames_list = new ArrayList<>();
                                    ArrayList<String> shortforms_list = new ArrayList<>();
                                    ArrayList<String> branchcontact_list = new ArrayList<>();

                                    ArrayList<HashMap<String,String>> hashMapArrayList=new ArrayList<HashMap<String,String>>();
                                    for (int i = 0; i < rolesDTOLis.size(); i++) {
                                        HashMap<String,String> hashMap=new HashMap<>();

                                        String id = rolesDTOLis.get(i).getId();
                                        String branch_id = rolesDTOLis.get(i).getBranch_id();
                                        String branch_name = rolesDTOLis.get(i).getBranch_name();
                                        String short_form = rolesDTOLis.get(i).getShort_form();
                                        String role_id = rolesDTOLis.get(i).getRole_id();
                                        String role_name = rolesDTOLis.get(i).getRole_name();
                                        String branch_contact = rolesDTOLis.get(i).getBranch_contact();
                                        String company = rolesDTOLis.get(i).getCompany();
                                        String company_id = rolesDTOLis.get(i).getCompany_id();
                                        String rolebranch = branch_name;

                                        hashMap.put("id",id);
                                        hashMap.put("company_id",company_id);
                                        hashMap.put("company",company);
                                        hashMap.put("branch_id",branch_id);
                                        hashMap.put("branch_name",branch_name);
                                        hashMap.put("branch_contact",branch_contact);
                                        hashMap.put("short_form",short_form);
                                        hashMap.put("role_id",role_id);
                                        hashMap.put("role_name",role_name);
                                        hashMapArrayList.add(hashMap);

                                        if (ROLEPKEY.equals(id)) {
                                            SELECTEDPOSITION = i;
                                        }
                                        rolebranchList.add(rolebranch);
                                        branch_idsList.add(branch_id);
                                        branchnames_list.add(branch_name);
                                        shortforms_list.add(short_form);
                                        branchcontact_list.add(branch_contact);
                                    }
                                    loadRolesList(rolebranchList, branch_idsList, branchnames_list, shortforms_list,
                                            role_count,branch_count,branchcontact_list,hashMapArrayList);
                                }
                            } else {
                            }
                        } catch (Exception e) {
                        }
                    } else {
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void loadRolesList(ArrayList<String> rolebranchList, final ArrayList<String> branch_idsList,
                              final ArrayList<String> branchnames_list,
                              ArrayList<String> shortforms_list,final String role_count,final String branch_count,
                              final ArrayList<String> branchcontact_list,
                              final ArrayList<HashMap<String,String>> hashMapArrayList) {
        SpinnerItemsAdapternew spinnerClass = new SpinnerItemsAdapternew(getActivity(),
                R.layout.layout_spinneritemswhite, rolebranchList,hashMapArrayList);
        spinnerClass
                .setDropDownViewResource(R.layout.layout_spinneritemswhite);
        employeesspinner.setAdapter(spinnerClass);
        employeesspinner.setSelection(SELECTEDPOSITION);

         employeesspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                 // TODO Auto-generated method stub
                if (globalShare.getLoginselectedfromval().equals("multiplescreen")) {
                    globalShare.setLoginselectedfromval("other");
                    isroleselected=false;
                } else if (globalShare.getLoginselectedfromval().equals("loginscreen")) {
                    isroleselected=false;
                }else if (globalShare.getLoginselectedfromval().equals("splash")) {
                    globalShare.setLoginselectedfromval("other");
                    isroleselected=false;
                }else if (globalShare.getLoginselectedfromval().equals("notification")) {
                    globalShare.setLoginselectedfromval("other");
                    isroleselected=false;
                }
                else if (globalShare.getLoginselectedfromval().equals("other")) {
//                    if(isroleselected==false)
//                    {
                        globalShare.setLoginselectedfromval("menu");

                        String rolePkey =hashMapArrayList.get(position).get("id");
                        String company_id=hashMapArrayList.get(position).get("company_id");
                        String company=hashMapArrayList.get(position).get("company");
                        String branchid =hashMapArrayList.get(position).get("branch_id");
                        String branchname = hashMapArrayList.get(position).get("branch_name");
                        String branch_contact = hashMapArrayList.get(position).get("branch_contact");
                        String short_form = hashMapArrayList.get(position).get("short_form");
                        String role_id = hashMapArrayList.get(position).get("role_id");
                        String role_name = hashMapArrayList.get(position).get("role_name");

                        globalShare.setLoginselectedfromval("splash");

                        HashMap<String, String>  values = SharedDB.getUserDetails(getActivity());
                        String ADDRESSID = values.get(SharedDB.ADDRESSID);
                        String USERTYPE = values.get(SharedDB.USERTYPE);
                        //String SHORTFORM = values.get(SharedDB.SHORTFORM);
                        //String USERNAME = values.get(SharedDB.USERNAME);
                        String MOBILE = values.get(SharedDB.MOBILE);
                        String DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                        String COMPANY = values.get(SharedDB.COMPANY);
                       // String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
                        // String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
                        //String BRANCHID = values.get(SharedDB.BRANCHID);
                        String PINCODE = values.get(SharedDB.PINCODE);
                        String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
                        String BRANCHCOUNT = values.get(SharedDB.BRANCHCOUNT);
                        String ROLECOUNT = values.get(SharedDB.ROLECOUNT);
                        String IMAGEURL = values.get(SharedDB.IMAGEURL);

                        double lat = 0.0;
                        double lon = 0.0;
                        SharedDB.loginSahred(getActivity(), MOBILE, DELIVERY_ADDRESS, role_name, "",
                                "", lat, lon, PRIMARYID, PINCODE, USERNAME, IMAGEURL, ADDRESSID, branchid,
                                branchname, short_form,company_id,branch_contact,COMPANIESLIST,
                                branch_count,role_count,rolePkey);

                    if(DealerMenuScreenActivity.mainfinish!=null)
                    {
                        DealerMenuScreenActivity.mainfinish.finish();
                    }
                    if(FinanceDeptMenuScreenActivity.mainfinish!=null)
                    {
                        FinanceDeptMenuScreenActivity.mainfinish.finish();
                    }
                    if(DispatchMenuScreenActivity.mainfinish!=null)
                    {
                        DispatchMenuScreenActivity.mainfinish.finish();
                    }
                    if(AdminMenuScreenActivity.mainfinish!=null)
                    {
                        AdminMenuScreenActivity.mainfinish.finish();
                    }
                    if(SuperAdminMenuScrenActivity.mainfinish!=null)
                    {
                        SuperAdminMenuScrenActivity.mainfinish.finish();
                    }

                        if (short_form.equals("ADMIN")) {
                            globalShare.setAdminmenuselectpos("1");

                            Intent intent = new Intent(getActivity(),
                                    AdminMenuScreenActivity.class);
                            startActivity(intent);
                        } else if (short_form.equals("FM")) {
                            globalShare.setFinancemenuselectpos("1");

                            Intent intent = new Intent(getActivity(),
                                    FinanceDeptMenuScreenActivity.class);
                            startActivity(intent);
                        } else if (short_form.equals("SE")) {
                            globalShare.setDealerMenuSelectedPos("1");

                            Intent intent = new Intent(getActivity(),
                                    DealerMenuScreenActivity.class);
                            startActivity(intent);
                        } else if (short_form.equals("DB")) {
                            globalShare.setDeliverymenuselectpos("1");
                            Intent intent = new Intent(getActivity(),
                                    DeliveryMenuScreenActivity.class);
                            startActivity(intent);
                        } else if (short_form.equals("PACKER")) {
                            globalShare.setDispatchmenuselectpos("1");

                            Intent intent = new Intent(getActivity(),
                                    DispatchMenuScreenActivity.class);
                            startActivity(intent);
                        } else if (short_form.equals("DEALER")) {
                            globalShare.setDealerMenuSelectedPos("1");

                            Intent intent = new Intent(getActivity(),
                                    DealerMenuScreenActivity.class);
                            startActivity(intent);
                        } else if (short_form.equals("SA")) {
                            globalShare.setSuperadminmenuselectpos("1");

                            Intent intent = new Intent(getActivity(),
                                    SuperAdminMenuScrenActivity.class);
                            startActivity(intent);
                        }
          //          refreshToken();
                   // }
                }else {
//                    String branchid = branch_idsList.get(position);
//                    String branchname = branchnames_list.get(position);
//                    String branch_contact = branchcontact_list.get(position);
//                    String company_id=hashMapArrayList.get(position).get("company_id");

                    String rolePkey =hashMapArrayList.get(position).get("id");
                    String company_id=hashMapArrayList.get(position).get("company_id");
                    String company=hashMapArrayList.get(position).get("company");
                    String branchid =hashMapArrayList.get(position).get("branch_id");
                    String branchname = hashMapArrayList.get(position).get("branch_name");
                    String branch_contact = hashMapArrayList.get(position).get("branch_contact");
                    String short_form = hashMapArrayList.get(position).get("short_form");
                    String role_id = hashMapArrayList.get(position).get("role_id");
                    String role_name = hashMapArrayList.get(position).get("role_name");

                    HashMap<String, String>  values = SharedDB.getUserDetails(getActivity());
                    String ADDRESSID = values.get(SharedDB.ADDRESSID);
                    String USERTYPE = values.get(SharedDB.USERTYPE);
                    String SHORTFORM = values.get(SharedDB.SHORTFORM);
                    String USERNAME = values.get(SharedDB.USERNAME);
                    String MOBILE = values.get(SharedDB.MOBILE);
                    String DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
                    String COMPANY = values.get(SharedDB.COMPANY);
                    String BRANCHCONTACT = values.get(SharedDB.BRANCHCONTACT);
                    // String BRANCHNAME = values.get(SharedDB.BRANCHNAME);
                    //String BRANCHID = values.get(SharedDB.BRANCHID);
                    String PINCODE = values.get(SharedDB.PINCODE);
                    String COMPANIESLIST = values.get(SharedDB.COMPANIESLIST);
                    String BRANCHCOUNT = values.get(SharedDB.BRANCHCOUNT);
                    String ROLECOUNT = values.get(SharedDB.ROLECOUNT);
                    String IMAGEURL = values.get(SharedDB.IMAGEURL);

                    double lat = 0.0;
                    double lon = 0.0;
//                    SharedDB.loginSahred(getActivity(), MOBILE, DELIVERY_ADDRESS, USERTYPE, "",
//                            "", lat, lon, PRIMARYID, PINCODE, USERNAME, IMAGEURL, ADDRESSID, branchid,
//                            branchname, SHORTFORM,company_id,branch_contact,COMPANIESLIST,
//                            BRANCHCOUNT,ROLECOUNT);
                    SharedDB.loginSahred(getActivity(), MOBILE, DELIVERY_ADDRESS, role_name, "",
                            "", lat, lon, PRIMARYID, PINCODE, USERNAME, IMAGEURL, ADDRESSID, branchid,
                            branchname, short_form,company_id,branch_contact,COMPANIESLIST,
                            branch_count,role_count,rolePkey);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void refreshToken() {
        try {
            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();
            saveTokenToPrefs("");
            globalShare.setNotificationfrom("login");
        } catch (Exception e) {

        }
    }
    private void saveTokenToPrefs(String _token) {
        try{
            SharedPreferences preferences = getActivity().getSharedPreferences(Config.SHARED_PREF, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("regId", _token);
            editor.apply();
        }catch (Exception e)
        {
        }
    }
}