package com.arcias.melocate.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.arcias.melocate.R;
import com.arcias.melocate.services.ConnectivityReceiver;
import com.arcias.melocate.services.LocationService;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainFrame extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ProgressDialog progressDialog;
    private Fragment fragment;
    private FirebaseUser user;
    private AlertDialog alertDialog;
    private String TAG = "MainFrame";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseUser currentUser;
    private Intent serviceIntent;
    private TextView myLocation;
    private TextView findLocation;
    private TextView myProfile;
    private TextView feedback;
    private TextView aboutUs;
    private TextView friendList;
    private LocationRequest request;
    private LocationSettingsRequest.Builder builder;
    private static final int GPS_REQUEST_CODE=2221;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_frame);
        toolbar = findViewById(R.id.tool_bar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        myLocation=findViewById(R.id.my_location);
        findLocation=findViewById(R.id.find_location);

        myProfile=findViewById(R.id.my_profile);
        feedback=findViewById(R.id.feedback);
        aboutUs=findViewById(R.id.about_us);
        friendList=findViewById(R.id.friend_list);
        progressDialog = new ProgressDialog(this);

        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert !!");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this
                , drawerLayout, toolbar, R.string.openNavDrawer,
                R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

//appBarConfiguration=new AppBarConfiguration.Builder(R.id.nav_home)
//        .setDrawerLayout(drawerLayout).build();
//        NavController navController= Navigation.findNavController(this,
//                R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this,
//                navController,appBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView,navController);
        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainFrame.this, MyLocation.class));
            }
        });
        findLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFrame.this, FindLocation.class));
            }
        });
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFrame.this, ProfileActivity.class));
            }
        });
        friendList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFrame.this, FriendList.class));
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFrame.this, FeedbackActivity.class));
            }
        });
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainFrame.this, AboutUsActivity.class));
            }
        });

        myLocation();


    }

    @Override
    protected void onStart() {
        super.onStart();
        enableGps();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GPS_REQUEST_CODE)
        {
            ///operation perform
        }
        else {
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home: {
//                startActivity(new Intent(MainFrame.this,MainFrame.class));
                Intent intent=getIntent();
                finish();
                startActivity(intent);
                break;
            }
            case R.id.nav_my_location: {
                startActivity(new Intent(MainFrame.this,
                        MyLocation.class));

                break;
            }
            case R.id.nav_find_location: {
                startActivity(new Intent(MainFrame.this,
                        FindLocation.class));

                break;
            }

            case R.id.nav_profile: {
                startActivity(new Intent(MainFrame.this, ProfileActivity
                        .class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            }
            case R.id.nav_log_out: {
                fragment = null;
                logout();
                break;
            }
            case R.id.nav_acc_delete: {
                fragment = null;
                deleteAccount();
                break;
            }
            case R.id.feedback: {
                startActivity(new Intent(MainFrame.this, FeedbackActivity.class));
                break;
            }
            case R.id.about_app: {
                startActivity(new Intent(MainFrame.this,
                        AboutUsActivity.class));

                break;
            }
        }


        return true;
    }

    private void deleteAccount() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainFrame.this);
        alert.setMessage("Are You Really want to Delete Your Account ??")
                .setCancelable(false).setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase
                                .getInstance().getReference().child("Registered Users")
                                .child(user.getUid()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("User Location").child(
                                user.getUid()
                        ).removeValue();
                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    alertDialog.setMessage("your Account is Successfully deleted");
                                    alertDialog.show();

                                    startActivity(new Intent(MainFrame
                                            .this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                } else {
                                    alertDialog.setMessage(task.getException().getMessage());
                                    alertDialog.show();
                                }
                            }
                        });

                    }
                }
        ).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alert.create();

        alertDialog.show();
    }

    private void logout() {

        AlertDialog.Builder alert = new AlertDialog.Builder(MainFrame.this);
        alert.setMessage("Are You Really want to Log out ??")
                .setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth.getInstance().signOut();

                        startActivity(new Intent(MainFrame
                                .this, MainActivity.class));
                        finish();
                    }
                }
        ).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alert.create();

        alertDialog.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public boolean isLocationServiceRunning() {

     try {
         ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
         for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
             if ("com.arcias.trackingapp.services.LocationService".equals(service.service.getClassName())) {
                 Log.d("Actvity", "isLocationServiceRunning: location service is already running.");
                 return true;
             }
         }
     }
     catch (Exception e)
     {
         e.printStackTrace();
     }

        Log.d("Activity", "isLocationServiceRunning: location service is not running.");
        return false;
    }

    private void startLocationService() {

        try {

            boolean isPermission = isLocationServiceRunning();
            if (!(isPermission)) {
                serviceIntent = new Intent(this, LocationService.class);
//        this.startService(serviceIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    MainFrame.this.startForegroundService(serviceIntent);
                } else {
                    startService(serviceIntent);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            stopService(serviceIntent);
            Log.d("tag", "OnDestroy");
            Intent broadcastIntent = new Intent(this, ConnectivityReceiver.class);
            sendBroadcast(broadcastIntent);
            Log.d("tag", "after Destroy connecting to receiver");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


        super.onDestroy();
    }

    private void myLocation() {
     try {
         if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                 != PackageManager.PERMISSION_GRANTED &&
                 ActivityCompat.checkSelfPermission(this,
                         Manifest.permission.ACCESS_COARSE_LOCATION)
                         != PackageManager.PERMISSION_GRANTED) {

             return;
         }
     }
     catch (Exception e)
     {
         e.printStackTrace();
     }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {

                        if (task.isSuccessful()) {

                      try {
                          Location location = task.getResult();
                          String getLat = Double.toString(location.getLatitude());
                          String getLong = Double.toString(location.getLongitude());

                          if (!(getLat.equals("37.4219983")) && !(getLong.equals("-122.084"))) {
                              Log.d(TAG, "" + location);
                              DatabaseReference reference = FirebaseDatabase
                                      .getInstance().getReference().child("User Location")
                                      .child(currentUser.getUid());
                              HashMap<String, Object> map = new HashMap<>();
                              map.put("latitude", location.getLatitude());
                              map.put("id", currentUser.getUid());
                              map.put("longitude", location.getLongitude());
                              reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if (task.isSuccessful()) {
                                          Log.d(TAG, "Data added");
                                      }
                                  }
                              });
                          }
                      }
catch (NullPointerException e)
{
    e.printStackTrace();
}

                        }
                        else
                            {
                            Log.d(TAG, task.getException().getMessage());
                        }
                    }

                }
        );
        startLocationService();
    }


    @Override
    public void onClick(View view) {

    }
    private  void enableGps()
    {
        request=new LocationRequest();
        request.setFastestInterval(1500).
                setInterval(3000).
                setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder=new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        Task<LocationSettingsResponse> responseTask=
                LocationServices.
                        getSettingsClient(this)
                        .checkLocationSettings(builder.build());
        responseTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    e.printStackTrace();
                    switch (e.getStatusCode())
                    {
                        case LocationSettingsStatusCodes
                                .RESOLUTION_REQUIRED
                                :
                        {
                            try {
                                ResolvableApiException
                                        apiException=(ResolvableApiException)e;
                                apiException.
                                        startResolutionForResult(MainFrame.this,GPS_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

    }
}
