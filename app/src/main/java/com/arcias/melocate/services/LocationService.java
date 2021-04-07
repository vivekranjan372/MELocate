package com.arcias.melocate.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class LocationService extends Service {

    private static final String TAG = "LocationService";
private FirebaseUser currentUser;
    private FusedLocationProviderClient mFusedLocationClient;
    private final static long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private final static long FASTEST_INTERVAL = 3*60 * 1000; /* 3 min */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "My Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).
                    createNotificationChannel(channel);

            Notification notification = new
                    NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: called.");
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        getLocation();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("Exit in service","onDestroy");
       Intent broadcastIntent=new Intent(this, ConnectivityReceiver.class);
       sendBroadcast(broadcastIntent);
        super.onDestroy();

    }


    private void getLocation() {

        // ---------------------------------- LocationRequest ------------------------------------
        // Create the location request to start receiving updates
        LocationRequest mLocationRequestHighAccuracy = new LocationRequest();
        mLocationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequestHighAccuracy.setInterval(UPDATE_INTERVAL);
        mLocationRequestHighAccuracy.setFastestInterval(FASTEST_INTERVAL);


        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                (Manifest.permission.ACCESS_FINE_LOCATION))!= PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: stopping the location service.");
            stopSelf();
            return;
        }
        Log.d(TAG, "getLocation: getting location information.");

        mFusedLocationClient.requestLocationUpdates(mLocationRequestHighAccuracy, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
try
{
    Location location = locationResult.getLastLocation();
    String getLat= Double.toString(location.getLatitude());
    String getLong= Double.toString(location.getLongitude());
    if(!(getLat.equals("37.4219983"))&&!(getLong.equals("-122.084")))
    {
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());

        saveUserLocation(latLng);
    }
}
catch (NullPointerException e)
{
    e.printStackTrace();
}

                    }
                },
                Looper.myLooper());
        // Looper.myLooper tells this to repeat forever until thread is destroyed
    }

    private void saveUserLocation(LatLng myLocation){
try
{
    HashMap<String, Object> map=new HashMap<>();
    map.put("latitude",myLocation.latitude);
    map.put("id",currentUser.getUid());
    map.put("longitude",myLocation.longitude);
    DatabaseReference reference=FirebaseDatabase
            .getInstance().getReference().child("User Location")
            .child(currentUser.getUid());
    reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful())
            {
                Log.d(TAG,"added successfully");
            }
            else
            {
                Log.d(TAG,"failed");
            }
        }
    });

}
catch (NullPointerException e)
{
    Log.d(TAG,e.getMessage());
    stopSelf();
}

    }




}
