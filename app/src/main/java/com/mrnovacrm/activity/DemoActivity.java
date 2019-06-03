package com.mrnovacrm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mrnovacrm.R;

public class DemoActivity extends AppCompatActivity {

    public static Activity mainfinish;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mainfinish=this;
        setContentView(R.layout.layout_changepassword);

    }
}
