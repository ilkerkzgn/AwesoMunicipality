package com.example.blabla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Complain3 extends AppCompatActivity {
    String downloadurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain3);
        downloadurl = getIntent().getStringExtra("downloadurl");
    }

    public void icon_location (View view) {
        Intent intenttoLocation = new Intent(getApplicationContext(),complain_location.class);
        intenttoLocation.putExtra("downloadurl",downloadurl);
        startActivity(intenttoLocation);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}
