package com.arcias.melocate.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.arcias.melocate.Model.ClusterMarker;
import com.arcias.melocate.Model.User;
import com.arcias.melocate.R;
import com.arcias.melocate.util.ClusterRenderer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;


public class FindLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationListener listener;
    private LocationManager manager;
    private final long minimumTime = 1000;
    private final long distance = 5;
    private Toolbar toolbar;
    private AutoCompleteTextView autoCompleteTextView;
    private String setLatitude;
    private String setLongitude;
    private FirebaseUser currentUser;
    private List<User> users;
    private AlertDialog alertDialog;

    private String otherUserId;
    private ArrayAdapter<String> adapter;
    private ImageView search;
    private String getLatitude;
    private String getLongitude;
    private String getNumber;
    private List<String> getMobileAndEmail;
    private static final float DEFAULT_ZOOM = 17f;
    private String displayName;
    private ImageView myLocation, calculateDistance;

    private LatLng latLng;
    private LatLng userLocation;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autoCompleteTextView = findViewById(R.id.search_bar);

        calculateDistance = findViewById(R.id.calculate_distance);
        toolbar = findViewById(R.id.tool_bar);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        getMobileAndEmail = new ArrayList<>();
        users = new ArrayList<>();


        search = findViewById(R.id.search);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Info !!");



        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            String getId = bundle.getString("id");
            if (otherUserId == null) {
                otherUserId = getId;
                getUserLocation();
            }
        }
        adapter = new ArrayAdapter<>(FindLocation.this,
                android.R.layout.select_dialog_item, getMobileAndEmail);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(adapter);

        readUser();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

    }


    private void showNumber() {
        if (users.size() != 0) {

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);

                getMobileAndEmail.add(user.getMobileNumber());
                getMobileAndEmail.add(user.getEmail());

            }


        }
        progressDialog.dismiss();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });


    }

    private void fetchData() {

        if (!(TextUtils.isEmpty(autoCompleteTextView
                .getText().toString()))) {
            autoCompleteTextView.clearListSelection();
            getNumber = autoCompleteTextView.getText().
                    toString();

            FirebaseDatabase.getInstance().getReference().
                    child("Registered Users")
                    .addValueEventListener
                            (new ValueEventListener() {
                                @Override
                                public void onDataChange
                                        (@NonNull
                                                 DataSnapshot dataSnapshot) {
                                    int count = 0;
                                    long totalCount = 0;

                                    for (DataSnapshot snapshot :
                                            dataSnapshot.getChildren()) {
                                        User user = snapshot.getValue(User.class);
                                        totalCount = snapshot.
                                                getChildrenCount();
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            count++;
                                            if (getNumber.equals
                                                    (snapshot1.getValue(String.class))) {
                                                otherUserId = snapshot.getKey();
                                                displayName = user.getFirstName() + " " + user.getLastName();

                                                getUserLocation();

                                                break;
                                            }

                                        }

                                    }
                                    totalCount = totalCount * dataSnapshot.getChildrenCount();

                                    if (count == totalCount) {
                                        alertDialog.setMessage("No Such Number Found");
                                        alertDialog.show();
                                    }
                                    autoCompleteTextView.setText("");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

        } else {
            alertDialog.setMessage("Empty is not Allowed");
            alertDialog.show();
        }

    }

    private void getUserLocation() {
        FirebaseDatabase.getInstance().getReference().
                child("User Location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange
                    (@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.child(otherUserId).exists()) {
                    Log.d("tag", otherUserId);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.getKey().equals(otherUserId)) {
                            getLatitude = snapshot.child("latitude").
                                    getValue().toString();
                            getLongitude = snapshot.child("longitude").
                                    getValue().toString();
                            userLocation = new LatLng(Double.parseDouble(getLatitude)
                                    , Double.parseDouble(getLongitude));
                            Log.d("tag", "" + userLocation);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    userLocation, DEFAULT_ZOOM

                            ));


                            showCustomMarker();
                            break;
                        }

                    }

                } else {
                    alertDialog.setMessage("No Records Found");
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showCustomMarker() {

        mMap.clear();
        ClusterManager<ClusterMarker>
                clusterManager = new ClusterManager<>
                (getBaseContext(), mMap);
        ClusterRenderer clusterRenderer = new
                ClusterRenderer(getBaseContext(),
                mMap, clusterManager);
        clusterManager.setRenderer(clusterRenderer);
        ClusterMarker marker = new
                ClusterMarker(userLocation, displayName,
                null, R.drawable.icon,
                getBaseContext());
        clusterManager.addItem(marker);
        clusterManager.cluster();

    }


    private void readUser() {
        FirebaseDatabase.getInstance().getReference()
                .child("Registered Users").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            users.add(user);

                        }
                        adapter.notifyDataSetChanged();
                        showNumber();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.getUiSettings().setZoomControlsEnabled(true);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    setLatitude = Double.toString(location.getLatitude());
                    setLongitude = Double.toString(location.getLongitude());


                } catch (SecurityException e) {
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
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            manager.requestLocationUpdates(LocationManager
                    .NETWORK_PROVIDER, minimumTime, distance, listener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }




}
