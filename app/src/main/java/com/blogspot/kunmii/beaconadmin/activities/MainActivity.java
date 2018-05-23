package com.blogspot.kunmii.beaconadmin.activities;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.blogspot.kunmii.beaconadmin.Helpers;
import com.blogspot.kunmii.beaconadmin.R;

public class MainActivity extends AppCompatActivity {

    AppBarLayout layout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        layout = findViewById(R.id.appbar_layout);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setSubtitle("Projects");


    }


    @Override
    protected void onResume() {
        super.onResume();

        if(Helpers.getUserToken(getApplication()) == null)
        {
            startActivityForResult(new Intent(this, LoginActivity.class),1);
        }

    }
}
