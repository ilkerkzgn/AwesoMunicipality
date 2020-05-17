package com.example.blabla;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Success extends AppCompatActivity {
    private Button button_gtyc, button_exit;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        button_exit = findViewById(R.id.button_exit);
        button_exit.setVisibility(View.INVISIBLE);

        button_gtyc = findViewById(R.id.button_gtyc);
        button_gtyc.setVisibility(View.INVISIBLE);

        if (firebaseUser != null) {
            button_gtyc.setVisibility(View.VISIBLE);

        }else {
            button_exit.setVisibility(View.VISIBLE);

        }
    }

    public void gtyc (View view){
        Intent intent = new Intent(getApplicationContext(),MyComplaints.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();

    }

    public void exit (View view){

        Intent intentToLogin = new Intent(getApplicationContext(),LoginActivity.class);
        intentToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentToLogin);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();


        if (firebaseUser != null){

            Intent intent = new Intent(getApplicationContext(),HomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            finish();

        }else {
            Intent intentToLogin = new Intent(getApplicationContext(),LoginActivity.class);
            intentToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentToLogin);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            finish();
        }

    }
}
