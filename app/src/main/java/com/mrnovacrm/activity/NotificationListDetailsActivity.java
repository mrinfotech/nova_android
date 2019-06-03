package com.mrnovacrm.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mrnovacrm.R;
import com.mrnovacrm.constants.GlobalShare;
import com.mrnovacrm.constants.TransparentProgressDialog;
import com.mrnovacrm.db.SharedDB;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class NotificationListDetailsActivity extends AppCompatActivity {

    public static Activity mainfinish;
    private String grievance_id;
    Context context;
    ImageView viewimg;
    TextView grievenceid_txt,status_txt,name_txt,mobile_txt,landmark_txt,address_txt,description_txt,region_txt,completein_txt;
    private TransparentProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification_list_details);
        mainfinish=this;
        context=this;

        View view=findViewById(R.id.header);
        ImageView backbtn=(ImageView)view.findViewById(R.id.backimg);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });


        Bundle bundle=getIntent().getExtras();
        grievance_id=bundle.getString("grievance_id");

        viewimg = (ImageView) findViewById(R.id.viewimg);
         grievenceid_txt = (TextView)findViewById(R.id.grievenceid_txt);
         status_txt = (TextView) findViewById(R.id.status_txt);
         name_txt = (TextView)findViewById(R.id.name_txt);
         mobile_txt = (TextView) findViewById(R.id.mobile_txt);
         landmark_txt = (TextView) findViewById(R.id.landmark_txt);
         address_txt = (TextView) findViewById(R.id.address_txt);
         description_txt = (TextView) findViewById(R.id.description_txt);

         region_txt=findViewById(R.id.region_txt);
         completein_txt=findViewById(R.id.completein_txt);
        //  completein_txt.setVisibility(View.GONE);

        getNotificationDetails();
    }

    private void getNotificationDetails() {
        progress=new TransparentProgressDialog(context);
        progress.show();

        String openURL = SharedDB.URL + "android/details.php?id="+grievance_id;
        Log.e("FCM URL....", "" + openURL);
        JSONObject mreq = new JSONObject();
        JsonObjectRequest movieReq = new JsonObjectRequest(openURL, mreq,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", response.toString());
                        try {
                            if (response.getInt("status") == 1) {
                                JSONArray res_reportArray = response.getJSONArray("grievances");
                                for (int i = 0; i < res_reportArray.length(); i++) {

                                    HashMap<String, String> grievancesHashmap = new HashMap<String, String>();
                                    JSONObject grievancesobj = res_reportArray.getJSONObject(i);

                                    if (grievancesobj.has("picture")) {
                                        String picture = grievancesobj.getString("picture");
                                        grievancesHashmap.put("picture", picture);
                                        Picasso.with(context).load(picture).resize(200, 200).placeholder(R.drawable.noimage).into(viewimg);
                                    }

                                    if (grievancesobj.has("ticketId")) {
                                        String ticketId = grievancesobj.getString("ticketId");
                                        grievancesHashmap.put("ticketId", ticketId);
                                        grievenceid_txt.setText(ticketId);
                                    }

                                    if (grievancesobj.has("grivence")) {
                                        String grivence = grievancesobj.getString("grivence");
                                        grievancesHashmap.put("grivence", grivence);
                                        description_txt.setText(grivence);
                                    }

                                    if (grievancesobj.has("reason")) {
                                        String reason = grievancesobj.getString("reason");
                                        grievancesHashmap.put("reason", reason);
                                        region_txt.setText(reason);
                                    }

                                    if (grievancesobj.has("grievanceId")) {
                                        String grievanceId = grievancesobj.getString("grievanceId");
                                        grievancesHashmap.put("grievanceId", grievanceId);
                                    }

                                    if (grievancesobj.has("circle")) {
                                        String circle = grievancesobj.getString("circle");
                                        grievancesHashmap.put("circle", circle);
                                        region_txt.setText(circle);
                                    }

                                    if (grievancesobj.has("circle_name")) {
                                        String circle_name = grievancesobj.getString("circle_name");
                                        grievancesHashmap.put("circle_name", circle_name);
                                    }

                                    if (grievancesobj.has("time_limit")) {
                                        String time_limit = grievancesobj.getString("time_limit");
                                        grievancesHashmap.put("time_limit", time_limit);
                                    }

                                    if (grievancesobj.has("address")) {
                                        String address = grievancesobj.getString("address");
                                        grievancesHashmap.put("address", address);
                                        address_txt.setText(address);
                                    }
                                    String username="";
                                    if (grievancesobj.has("username")) {
                                        username = grievancesobj.getString("username");
                                        grievancesHashmap.put("username", username);

                                    }

                                    String usermobile;
                                    if (grievancesobj.has("usermobile")) {
                                        usermobile = grievancesobj.getString("usermobile");
                                        grievancesHashmap.put("usermobile", usermobile);
                                        name_txt.setText(username+" / "+usermobile);
                                    }

                                    if (grievancesobj.has("status")) {
                                        String status = grievancesobj.getString("status");
                                        grievancesHashmap.put("status", status);
                                        status_txt.setText(status);
                                    }

                                    if (grievancesobj.has("modifiedOn")) {
                                        String modifiedOn = grievancesobj.getString("modifiedOn");
                                        grievancesHashmap.put("modifiedOn", modifiedOn);
                                    }

                                    if (grievancesobj.has("latitude")) {
                                        String latitude = grievancesobj.getString("latitude");
                                        grievancesHashmap.put("latitude", latitude);
                                    }

                                    if (grievancesobj.has("longitude")) {
                                        String longitude = grievancesobj.getString("longitude");
                                        grievancesHashmap.put("longitude", longitude);
                                    }

                                    if (grievancesobj.has("before_picture")) {
                                        String before_picture = grievancesobj.getString("before_picture");
                                        grievancesHashmap.put("before_picture", before_picture);
                                    }
                                    if (grievancesobj.has("after_picture")) {
                                        String after_picture = grievancesobj.getString("after_picture");
                                        grievancesHashmap.put("after_picture", after_picture);
                                    }

                                    if (grievancesobj.has("before_latitude")) {
                                        String before_latitude = grievancesobj.getString("before_latitude");
                                        grievancesHashmap.put("before_latitude", before_latitude);
                                    }

                                    if (grievancesobj.has("before_longitude")) {
                                        String before_longitude = grievancesobj.getString("before_longitude");
                                        grievancesHashmap.put("before_longitude", before_longitude);
                                    }

                                    if (grievancesobj.has("after_latitude")) {
                                        String after_latitude = grievancesobj.getString("after_latitude");
                                        grievancesHashmap.put("after_latitude", after_latitude);
                                    }

                                    if (grievancesobj.has("after_longitude")) {
                                        String after_longitude = grievancesobj.getString("after_longitude");
                                        grievancesHashmap.put("after_longitude", after_longitude);
                                    }

                                    if (grievancesobj.has("landmark")) {
                                        String landmark = grievancesobj.getString("landmark");
                                        grievancesHashmap.put("landmark", landmark);
                                        landmark_txt.setText(landmark);
                                    }

                                    if (grievancesobj.has("time_limit")) {
                                        String time_limit = grievancesobj.getString("time_limit");
                                        grievancesHashmap.put("time_limit", time_limit);
                                        completein_txt.setText(time_limit);
                                    }
                                }
                            } else if (response.getInt("status") == 0) {
                                Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_SHORT).show();
                            }
                            hidePDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("OPENTAB", "Error: " + error.getMessage());
             //   Log.e("OPENTAB", "Error: " + error.getMessage());
                hidePDialog();
            }
        });
        // Adding request to request queue
        GlobalShare.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
}
