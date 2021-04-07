package com.arcias.melocate.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

public class FriendList extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private Toolbar toolbar;
    private List<String> userNumberList;
    private ClusterManager<ClusterMarker> markerClusterManager;
    private ClusterRenderer renderer;
    private List<String> existUserId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userNumberList = new ArrayList<>();
        existUserId = new ArrayList<>();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

    }

    private void showItem() {
        if (map != null) {
            markerClusterManager = new ClusterManager
                    <>(FriendList.this, map
            );

            Log.d("tag", "clusterManager is invoked" + markerClusterManager);


            renderer = new ClusterRenderer(FriendList.this,
                    map, markerClusterManager);
            Log.d("tag", "Renderer Class is invoked" + renderer);

            markerClusterManager.setRenderer(renderer);
            addUserItem();

        }

    }

    private void addUserItem() {
        FirebaseDatabase.getInstance().getReference().child("User Location")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            for (int i = 0; i < existUserId.size(); i++) {
                                if (snapshot.getKey().equals(existUserId.get(i))) {
                                    final String getUserKey = snapshot.getKey();
                                    String getLatitude = snapshot.child("latitude").getValue().toString();
                                    String getLongitude = snapshot.child("longitude").getValue().toString();
                                    final LatLng userLocation = new LatLng(Double.parseDouble(getLatitude), Double.parseDouble(getLongitude));
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 4.5f));
                                    FirebaseDatabase.getInstance().getReference().child("Registered Users")
                                            .child(getUserKey).addValueEventListener(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                    String getUsername = "";
                                                    getUsername += dataSnapshot.child("firstName").getValue().toString();
                                                    getUsername += " ";
                                                    getUsername += dataSnapshot.child("lastName").getValue().toString();
                                                    ClusterMarker marker = new ClusterMarker(userLocation, getUsername, null,
                                                            R.drawable.icon, getBaseContext());
                                                    markerClusterManager.addItem(marker);
                                                    markerClusterManager.cluster();
                                                    progressDialog.dismiss();
                                                }


                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            }
                                    );


                                }
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        map.setMyLocationEnabled(true);

        map.getUiSettings().setZoomControlsEnabled(true);

        readUserContact();

    }
    private void readUserContact()
    {
        if(ActivityCompat.checkSelfPermission(getBaseContext(),
                (Manifest.permission.READ_CONTACTS))==
                PackageManager.PERMISSION_GRANTED);
        {

            String userContactId;
            ContentResolver resolver=getContentResolver();
            Cursor cursor=resolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,null,null,null
            );
            int count=0;
            while(cursor.moveToNext())
            {
                userContactId =cursor.getString(cursor.getColumnIndex(
                        ContactsContract.Contacts._ID
                )) ;



                Cursor phoneCursor=resolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
                        , ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                +"= ?",new String[]{userContactId},null
                );


                while (phoneCursor.moveToLast())
                {
                    String phoneNumber=phoneCursor
                            .getString(phoneCursor.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                            ));

                    phoneNumber=phoneNumber.replaceAll(" ","");
                    String special ="+91";
                    phoneNumber=phoneNumber.replace(special,"");

                  userNumberList.add(phoneNumber);

break;
                }

            }
            Log.d("tag"," "+count+"userSize = "+userNumberList.size());
        }
        fetchUserNumber();
    }
    private void fetchUserNumber()
    {
        FirebaseDatabase.getInstance().getReference().child("Registered Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        User user=snapshot.getValue(User.class);
                        String getDatabaseNumber=user.getMobileNumber();

                        for(int i=0;i<userNumberList.size();i++)
                        {
                            if(getDatabaseNumber.equals(userNumberList.get(i)))
                            {
                                Log.d("tag","total Number = "+userNumberList.size());
                                existUserId.add(snapshot.getKey());
                                Log.d("tag","Name = "+user.getFirstName()+" Exist Number = "+getDatabaseNumber);
                            }
                        }
                    }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
      showItem();
    }
}
