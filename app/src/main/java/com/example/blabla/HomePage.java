package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import eu.long1.spacetablayout.SpaceTabLayout;

public class HomePage extends AppCompatActivity {

    Button button_complain;
    SpaceTabLayout tabLayout;
    Toolbar toolbar_home;
    private FirebaseAuth firebaseAuth;

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
    //TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //TOOLBAR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //FIREBASE SIGNOUT

           if (item.getItemId() == R.id.menu_signout){
               AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
               builder.setCancelable(true);
               builder.setTitle("Is that all?");
               builder.setMessage("Are you sure about to signout?");
               builder.setNegativeButton("No", null);
               builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       final ProgressDialog progressDialog = new ProgressDialog(HomePage.this);
                       progressDialog.setMessage("Please wait while we handling this..");
                       progressDialog.show();

                       try {
                           firebaseAuth.signOut();
                           Intent intentToLogin = new Intent(HomePage.this,LoginActivity.class);
                           intentToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(intentToLogin);
                           overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                           finish();
                           progressDialog.dismiss();
                       }
                       catch (Exception e){
                           Toast.makeText(HomePage.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                           progressDialog.dismiss();
                       }
                   }
               });
               AlertDialog alert2 = builder.create();
               alert2.show();
           }
           // COMPLAIN

           else if (item.getItemId() == R.id.menu_complain){
               Intent intentToComplain = new Intent(HomePage.this,Complain2.class);
               startActivity(intentToComplain);
               overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
           }
           else if (item.getItemId() == R.id.menu_my_complains){

               Intent intent = new Intent(getApplicationContext(),MyComplaints.class);
               startActivity(intent);
               overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

           }
                return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //FIREBASE
        firebaseAuth = FirebaseAuth.getInstance();

        //TOOLBAR
        toolbar_home = findViewById(R.id.toolbar_home);
        toolbar_home.setTitle("AwesoMunicipality");
        toolbar_home.setTitleTextAppearance(this,R.style.SatisfyText);
        setSupportActionBar(toolbar_home);

    }

    //COMPLAIN BUTTON
    public void complain(View view) {
        button_complain = findViewById(R.id.button_home);
        Intent intentToComplain = new Intent(HomePage.this,Complain2.class);
        startActivity(intentToComplain);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

}
