package com.mrnovacrm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.mrnovacrm.R;
import com.mrnovacrm.splashscreen.SplashScreenActivity;

/**
 * Created by MITRAYAVSP2 on 6/23/2016.
 */
public class ApplicationReloadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ss_applicationreload);

        TextView update_txt=findViewById(R.id.update_txt);
        update_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent=new Intent(getApplicationContext(), SplashScreenActivity.class);
                startActivity(intent);
            }
        });
    }
}