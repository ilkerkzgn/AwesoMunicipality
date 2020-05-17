package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText editText_mail_fp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        editText_mail_fp = findViewById(R.id.editText_mail_fp);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void sendPassword (View view){
        String mail = editText_mail_fp.getText().toString();

        if (TextUtils.isEmpty(mail)) {

            Toast.makeText(getApplicationContext(),"Please make sure about your information.",Toast.LENGTH_LONG).show();
        }else {

            final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this);
            progressDialog.setMessage("Please wait while we handling this..");
            progressDialog.show();

            firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Successful | Please check your mail for reset your password.", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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

    public void back (View view){
        Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    public void onBackPressed() {
        Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();

    }
}
