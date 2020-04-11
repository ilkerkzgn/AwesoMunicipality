package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    EditText editText_mail, editText_password, editText_password2;
    ImageView visibility_on2, visibility_off2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        editText_mail = findViewById(R.id.editText_mail);
        editText_password = findViewById(R.id.editText_password);
        editText_password2 = findViewById(R.id.editText_password2);
        visibility_off2 = findViewById(R.id.visibility_off2);
        visibility_on2 = findViewById(R.id.visibility_on2);
        visibility_off2.setVisibility(View.GONE);



    }
//SIGN UP BUTTON CLICK
    public void signup (View view){

        String mail = editText_mail.getText().toString();
        String password = editText_password.getText().toString();
        String password2 = editText_password2.getText().toString();

        //CONTROLLING EDIT TEXTS
        if ((TextUtils.isEmpty(mail) || TextUtils.isEmpty(password) || TextUtils.isEmpty(password2))) {

            Toast.makeText(getApplicationContext(),"Please make sure about your information.",Toast.LENGTH_LONG).show();
        }
        //DOUBLE PASSWORD CHECK
        else if (!password.equals(password2)){
            Toast.makeText(getApplicationContext(),"Your passwords are not equal.",Toast.LENGTH_LONG).show();
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
            builder.setCancelable(true);
            builder.setTitle("Is that all?");
            builder.setMessage("Are you sure about your information?");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                    progressDialog.setMessage("Please wait while we handling this..");
                    progressDialog.show();

                    String mail = editText_mail.getText().toString();
                    String password = editText_password.getText().toString();
                    // CHECKING THE EDIT TEXTS

                    firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            //VERIFICATION MAIL
                            firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    progressDialog.dismiss();
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignupActivity.this, "User Created | Please check your mail for verification.", Toast.LENGTH_LONG).show();

                                        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                                    }else {
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        Toast.makeText(SignupActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(SignupActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });

            AlertDialog alert1 = builder.create();
            alert1.show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void back (View view){
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }


    //PASSWORD VISIBILITY (SHOW-HIDE)
    public void VisibilityOn2 (View view){

        visibility_off2.setVisibility(View.VISIBLE);
        visibility_on2.setVisibility(View.GONE);
        editText_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        editText_password2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    public void VisibilityOff2 (View view){

        visibility_on2.setVisibility(View.VISIBLE);
        visibility_off2.setVisibility(View.GONE);
        editText_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        editText_password2.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }
}
