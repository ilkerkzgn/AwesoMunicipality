package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class MyComplaints extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    String userEmail;
    Toolbar toolbar_mycomplaints;

    //TOOLBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu_withoutcomplain,menu);

        return super.onCreateOptionsMenu(menu);
    }

    //TOOLBAR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //FIREBASE SIGNOUT

        if (item.getItemId() == R.id.menu_signout){
            AlertDialog.Builder builder = new AlertDialog.Builder(MyComplaints.this);
            builder.setCancelable(true);
            builder.setTitle("Is that all?");
            builder.setMessage("Are you sure about to sign out?");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog progressDialog = new ProgressDialog(MyComplaints.this);
                    progressDialog.setMessage("Please wait while we handling this..");
                    progressDialog.show();

                    try {
                        firebaseAuth.signOut();
                        Intent intentToLogin = new Intent(MyComplaints.this,LoginActivity.class);
                        startActivity(intentToLogin);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                        progressDialog.dismiss();
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
            AlertDialog alert2 = builder.create();
            alert2.show();
        }
        // COMPLAIN


        else if (item.getItemId() == R.id.menu_my_complains){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaints);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userEmail = firebaseUser.getEmail();

        //TOOLBAR
        toolbar_mycomplaints = findViewById(R.id.toolbar_mycomplaints);
        toolbar_mycomplaints.setTitle("AwesoMunicipality");
        toolbar_mycomplaints.setTitleTextAppearance(this,R.style.SatisfyText);
        setSupportActionBar(toolbar_mycomplaints);


        //getData();


    }

    public void getData () {

        CollectionReference collectionReference = firebaseFirestore.collection("Complaints");
        collectionReference.whereEqualTo("userEmail",userEmail).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){

                }else {

                    if (queryDocumentSnapshots !=null){

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){

                            Map<String,Object> data = snapshot.getData();
                            String downloadUrl = (String) data.get("downloadUrl");



                        }

                    }

                }


            }
        });


    }


}
