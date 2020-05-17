package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static androidx.core.content.FileProvider.getUriForFile;

public class Complain2 extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 0;
    FirebaseStorage firebaseStorage;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    String currentPhotoPath, imageFileName, withoutLogin;
    Intent intentToshowPic;
    Dialog dialog;
    Toolbar toolbar_complain2;
    ImageView preview, takeapicture,icon_camera, button_continue;
    Bitmap bitmap;
    Uri photoURI;




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
            AlertDialog.Builder builder = new AlertDialog.Builder(Complain2.this);
            builder.setCancelable(true);
            builder.setTitle("Is that all?");
            builder.setMessage("Are you sure about to signout?");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ProgressDialog progressDialog = new ProgressDialog(Complain2.this);
                    progressDialog.setMessage("Please wait while we handling this..");
                    progressDialog.show();

                    try {
                        firebaseAuth.signOut();
                        Intent intentToLogin = new Intent(getApplicationContext(),LoginActivity.class);
                        intentToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intentToLogin);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        finish();
                        progressDialog.dismiss();
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
            AlertDialog alert2 = builder.create();
            alert2.show();
        }
        // COMPLAIN


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
        setContentView(R.layout.activity_complain2);

        preview = findViewById(R.id.preview);
        preview.setVisibility(View.INVISIBLE);

        button_continue = findViewById(R.id.button_continue);
        button_continue.setVisibility(View.INVISIBLE);

        takeapicture = findViewById(R.id.takeapicture);
        icon_camera = findViewById(R.id.icon_camera);


        //TOOLBAR
        toolbar_complain2 = findViewById(R.id.toolbar_complain2);
        toolbar_complain2.setTitle("AwesoMunicipality");
        toolbar_complain2.setTitleTextAppearance(this,R.style.SatisfyText);
        setSupportActionBar(toolbar_complain2);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        //WITHOUT LOGIN
       /* withoutLogin = getIntent().getStringExtra("withoutLogin");

        if (withoutLogin.matches("withoutLogin")){


        }*/


        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null ) {
            toolbar_complain2.setVisibility(View.INVISIBLE);
        }

       /* if (firebaseUser == null) {
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }*/


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void icon_camera (View view) {

        String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };

        if (ContextCompat.checkSelfPermission(this,permissions[0]) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,permissions[1]) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,permissions[2]) == PackageManager.PERMISSION_GRANTED) {

            /* Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intentToCamera,CAMERA_REQUEST_CODE); */
            dispatchTakePictureIntent();


        }

        else{


            ActivityCompat.requestPermissions(this,
                    permissions,
                    1); //REQUEST CODE 1 : FOR THE PERMISSIONS
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED){

                 /* Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentToCamera,CAMERA_REQUEST_CODE); */
               dispatchTakePictureIntent();

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {




       if (requestCode == CAMERA_REQUEST_CODE
        && resultCode == RESULT_OK){

            try {

                /* intentToshowPic = new Intent(getApplicationContext(),test.class);
                intentToshowPic.putExtra("image_path",currentPhotoPath);
                startActivity(intentToshowPic); */

                icon_camera.setVisibility(View.INVISIBLE);
                takeapicture.setVisibility(View.INVISIBLE);
                bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                preview.setImageBitmap(bitmap);
                preview.setVisibility(View.VISIBLE);
                button_continue.setVisibility(View.VISIBLE);

            }

            catch (Exception e){

                Log.e("hata", "hata",e);
                e.printStackTrace();
                // Toast.makeText(Complain2.this, e.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* TURN BACK
    public void back (View view){

        Intent intent = new Intent(Complain2.this,Complain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    } */

    @Override
    public void onBackPressed() {

        super.onBackPressed();


        if (firebaseUser != null){

            Intent intent = new Intent(Complain2.this,HomePage.class);
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

    public void complain2_continue (View view){

        if (photoURI != null){
            // PROGRESS DIALOG
            final ProgressDialog progressDialog = new ProgressDialog(Complain2.this);
            progressDialog.setMessage("Please wait while we handling this..");
            progressDialog.show();

            //UNIQUE ID
            UUID uuid = UUID.randomUUID();
            final String imageName = "pictures/" + uuid + ".jpg";

            storageReference.child(imageName).putFile(photoURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //DOWNLOAD URL
                    StorageReference secondReference = FirebaseStorage.getInstance().getReference(imageName);
                    secondReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String downloadurl = uri.toString();
                            System.out.println("Url : "+downloadurl);

                            Intent intenttoLocation = new Intent(getApplicationContext(),Complain3.class);
                            intenttoLocation.putExtra("downloadurl",downloadurl);
                            startActivity(intenttoLocation);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    // PROGRESS DIALOG
                    progressDialog.dismiss();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // PROGRESS DIALOG
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }else {

            Toast.makeText(getApplicationContext(), "Houston we have a problem! Please try again.", Toast.LENGTH_LONG).show();
        }

    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

                ex.printStackTrace();
            }

            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

            }
        }
    }

}
