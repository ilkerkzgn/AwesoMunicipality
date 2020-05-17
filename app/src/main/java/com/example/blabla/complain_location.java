package com.example.blabla;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class complain_location extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    Button button_continue_location;
    Location mLastLocation;
    LatLng latLng;
    LocationListener locationListener;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    int MY_PERMISSIONS_REQUEST_GPS = 101;
    List<Address> addressList;
    String address, downloadurl, userEmail;
    EditText additional_info;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private ProgressDialog progressDialog;
    private TextView textView_hitmarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(complain_location.this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        button_continue_location = findViewById(R.id.button_continue_location);
        button_continue_location.setVisibility(View.INVISIBLE);

        textView_hitmarker = findViewById(R.id.textView_hitmarker);
        textView_hitmarker.setVisibility(View.INVISIBLE);

        downloadurl = getIntent().getStringExtra("downloadurl");

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null){
            userEmail = "anonymous";
        }else {
            userEmail = firebaseUser.getEmail();
        }





    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                button_continue_location.setVisibility(View.VISIBLE);
                return false;
            }
        });



        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000); // two minute interval
        mLocationRequest.setFastestInterval(60000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                checkGPSandTakeLoc();

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            checkGPSandTakeLoc();
        }


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {


                checkGPSandTakeLoc();

            }

            @Override
            public void onProviderDisabled(String provider) {
                checkGPSandTakeLoc();


            }
        };
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {


            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;

                try {
                    Geocoder geocoder = new Geocoder(complain_location.this, Locale.getDefault());
                    addressList = geocoder.getFromLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude(),1);
                    address = addressList.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                if (addressList != null && addressList.size() > 0){
                    markerOptions.title(address);
                }else {
                    markerOptions.title("You're here!");
                }

                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_a));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);


                //move map camera
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                mGoogleMap.animateCamera(cameraUpdate);

            }
        }
    };




    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(complain_location.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                checkGPSandTakeLoc();

            }else {

                Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void checkGPSandTakeLoc (){
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlertToUser();
        }else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
            textView_hitmarker.setVisibility(View.VISIBLE);
        }
    }

     public void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(complain_location.this);
        alertDialogBuilder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(enableGpsIntent,MY_PERMISSIONS_REQUEST_GPS);
                            }
                        });
        final AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MY_PERMISSIONS_REQUEST_GPS){

            checkGPSandTakeLoc();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void button_continue_location (View view){
    AdditionalInfo();
    }

    public void AdditionalInfo (){
        AlertDialog.Builder builder = new AlertDialog.Builder(complain_location.this);
        LayoutInflater inflater = complain_location.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);
        additional_info = view.findViewById(R.id.additional_info);

        builder.setView(view)
                .setTitle("Additional Information")
                .setMessage("Feel free to give use any additional information on below.")
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override // SEND BUTTON -- WILL UPLOAD INFORMATION TO THE DATABASE
                    public void onClick(DialogInterface dialog, int which) {

                        //PROGRESS DIALOG
                        progressDialog = new ProgressDialog(complain_location.this);
                        progressDialog.setMessage("Please wait while we handling this..");
                        progressDialog.show();

                        String extraInfo;

                        if (additional_info != null){
                            extraInfo = additional_info.getText().toString();


                        }else {
                            extraInfo = "No Comment";
                        }
                        HashMap<String, Object> postData = new HashMap<>();
                        postData.put("userEmail", userEmail);
                        postData.put("downloadUrl",downloadurl);
                        postData.put("address", address);
                        postData.put("latLong", latLng);
                        postData.put("extraInfo",extraInfo);
                        postData.put("date", FieldValue.serverTimestamp());

                        firebaseFirestore.collection("Complaints").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(),Success.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                    finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                progressDialog.dismiss();
                                Toast.makeText(complain_location.this, e.getLocalizedMessage(),Toast.LENGTH_LONG).show();

                            }
                        });


                    }
                })
                .create()
                .show();

    }
}
