package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class HomePage extends AppCompatActivity {

    Button button_home;
    SpaceTabLayout tabLayout;
    BottomNavigationView bottom_menu_home;
    private Fragment temporalFragment;
    Toolbar toolbar_home;
    private FirebaseAuth firebaseAuth;

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
           }else if (item.getItemId() == R.id.menu_complain){
               Intent intentToComplain = new Intent(HomePage.this,Complain.class);
               startActivity(intentToComplain);
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


        //BOTTOM MENU
        bottom_menu_home = findViewById(R.id.bottom_menu_home);

        getSupportFragmentManager().beginTransaction().add(R.id.holder_main,new fragment_home()).commit();

        bottom_menu_home.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home){
                    temporalFragment = new fragment_home();
                }
                else if (menuItem.getItemId() == R.id.menu_profile){
                   temporalFragment = new fragment_profile();
                }
                getSupportFragmentManager().beginTransaction().add(R.id.holder_main,temporalFragment).commit();
                return true;
            }

        });




        /*
        //add the fragments you want to display in a List
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentA());
        fragmentList.add(new FragmentB());
        fragmentList.add(new FragmentC());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        //we need the savedInstanceState to get the position
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState); */

    }

    /*
    //we need the outState to save the position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }
*/
    //COMPLAIN BUTTON
    public void didTapButton(View view) {
        button_home = findViewById(R.id.button_home);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        button_home.startAnimation(myAnim);

        Intent intentToComplain = new Intent(HomePage.this,Complain.class);
        startActivity(intentToComplain);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

}
