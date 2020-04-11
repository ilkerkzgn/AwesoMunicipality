package com.example.blabla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Complain extends AppCompatActivity {

    Intent intentToComplain2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);

        intentToComplain2 = new Intent(Complain.this,Complain2.class);

    }

    public void lighting (View view){
        startActivity(intentToComplain2);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

    public void road (View view){
        startActivity(intentToComplain2);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

    public void sidewalk (View view){
        startActivity(intentToComplain2);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

    public void back (View view){
        Intent intent = new Intent(Complain.this,HomePage.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


}
