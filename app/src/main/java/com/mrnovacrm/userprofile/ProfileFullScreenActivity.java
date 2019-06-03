package com.mrnovacrm.userprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.mrnovacrm.R;
import com.mrnovacrm.activity.TouchImageView;
import com.mrnovacrm.db.SharedDB;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileFullScreenActivity extends AppCompatActivity {

    TouchImageView imgDisplay;
    private HashMap<String, String> values;
    private String PRIMARYID="";
    private String USERTYPE="";
    String USERNAME="",MOBILE="",DELIVERY_ADDRESS="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_full_screen);
        imgDisplay = findViewById(R.id.imgDisplay);
        if (SharedDB.isLoggedIn(getApplicationContext())) {
            values = SharedDB.getUserDetails(getApplicationContext());
            PRIMARYID = values.get(SharedDB.PRIMARYID);
            USERTYPE = values.get(SharedDB.USERTYPE);
            USERNAME = values.get(SharedDB.USERNAME);
            MOBILE = values.get(SharedDB.MOBILE);
            DELIVERY_ADDRESS = values.get(SharedDB.DELIVERY_ADDRESS);
            String IMAGEURL = values.get(SharedDB.IMAGEURL);

            if (!IMAGEURL.equals("")) {
                Picasso
                        .with(this)
                        .load(IMAGEURL)
                        .into(imgDisplay);
            }else{
                imgDisplay.setImageResource(R.drawable.noprofile);
            }

        }

        ImageButton imageperclose=findViewById(R.id.imageperclose);
        imageperclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}