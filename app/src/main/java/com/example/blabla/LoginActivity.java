package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText editText_mail, editText_password;
    TextView textView_register;
    ImageView visibility_on, visibility_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        editText_mail = findViewById(R.id.editText_mail);
        editText_password = findViewById(R.id.editText_password);
        textView_register = findViewById(R.id.textView_register);
        visibility_off = findViewById(R.id.visibility_off);
        visibility_on = findViewById(R.id.visibility_on);
        visibility_off.setVisibility(View.GONE);



        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            //Toast.makeText(LoginActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,HomePage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }

        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

    }


//LOGIN BUTTON
    public void login (View view) {

        String mail = editText_mail.getText().toString();
        String password = editText_password.getText().toString();

        if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(password)) {

            Toast.makeText(getApplicationContext(),"Please Send us your Info",Toast.LENGTH_LONG).show();
        }

        else {
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage("Please wait while we handling this..");
            progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(mail,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {


                if (firebaseAuth.getCurrentUser().isEmailVerified()){

                    Intent intent = new Intent(LoginActivity.this,HomePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Please Verify your E-mail", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Please Try Again", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        }

    }

    public void VisibilityOn (View view){

        visibility_off.setVisibility(View.VISIBLE);
        visibility_on.setVisibility(View.GONE);
        editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());



    }
    public void VisibilityOff (View view){

        visibility_on.setVisibility(View.VISIBLE);
        visibility_off.setVisibility(View.GONE);
        editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


    }
}
