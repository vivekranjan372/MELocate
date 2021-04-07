package com.arcias.melocate.activity;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arcias.melocate.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MyLocation extends AppCompatActivity implements OnMapReadyCallback {
private LocationListener locationListener;
private LocationManager locationManager;
private String setLatitude;
private String setLongitude;
private LatLng latLng;
private FirebaseUser currentUser;
private final long MIN_TIME=1000;
private final long MIN_DIS=5;
private static final float DEFAULT_ZOOM=17f;
private ProgressDialog progressDialog;


    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Toolbar toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Location");
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       finish();
                    }
                }
        );
       progressDialog=new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap=googleMap;
        myMap.getUiSettings().setZoomControlsEnabled(true);


        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try
                {

                    setLatitude= Double.toString(location.getLatitude());
                    setLongitude= Double.toString(location.getLongitude());
                    latLng=new LatLng(Double.parseDouble(setLatitude),
                            Double.parseDouble(setLongitude));

                  myMap.setMyLocationEnabled(true);
                    myMap.clear();

                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
                   UserLocation();
progressDialog.dismiss();
                }
                catch (SecurityException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME,MIN_DIS,locationListener);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();
        }

    }
    private void UserLocation() {

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference()
                .child("User Location");
        HashMap<String, Object> map=new HashMap<>();
        map.put("Latitude",setLatitude);
        map.put("id",currentUser.getUid());
        map.put("Longitude",setLongitude);
        reference.child(currentUser.getUid()).setValue(map);

    }
}
