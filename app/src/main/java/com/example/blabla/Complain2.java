package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Complain2 extends AppCompatActivity {
    EditText additional_info;
    private static final int CAMERA_REQUEST_CODE = 0;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain2);
        additional_info = findViewById(R.id.additional_info);
        additional_info.setFocusable(false);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();



    }

    public void icon_camera (View view) {

        String[] permissions = {Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (ContextCompat.checkSelfPermission(this,permissions[0]) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,permissions[1]) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this,permissions[2]) == PackageManager.PERMISSION_GRANTED) {

            Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intentToCamera,CAMERA_REQUEST_CODE);

        }

        else{


            ActivityCompat.requestPermissions(this,
                    permissions,
                    1); //REQUEST CODE 1 : FOR THE PERMISSIONS
        }

    }

    public void icon_location (View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Intent intentToCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentToCamera,CAMERA_REQUEST_CODE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CAMERA_REQUEST_CODE
        && resultCode == RESULT_OK
        && data != null){
            Uri uri = data.getData();



        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void back (View view){
        Intent intent = new Intent(Complain2.this,Complain.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
