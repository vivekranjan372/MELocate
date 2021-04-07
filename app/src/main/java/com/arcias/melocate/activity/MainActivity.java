package com.arcias.melocate.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.arcias.melocate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button register;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user= FirebaseAuth.getInstance().getCurrentUser();



        if (ActivityCompat.checkSelfPermission(MainActivity.this, (Manifest
                .permission.ACCESS_FINE_LOCATION)) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, (Manifest
                .permission.ACCESS_COARSE_LOCATION)) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, (Manifest
                .permission.READ_CONTACTS)) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.
                    permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                    , Manifest.permission.READ_CONTACTS
            }, PackageManager.PERMISSION_GRANTED);
        }

            login=findViewById(R.id.login);
            register=findViewById(R.id.register);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,
                            RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
            });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
  if(grantResults.length>0 && grantResults[0]==
          PackageManager.PERMISSION_GRANTED&&
          grantResults[1]== PackageManager.PERMISSION_GRANTED
          &&grantResults[2]== PackageManager.PERMISSION_GRANTED)
  {
      //validate
  }
  else
  {
      finish();
  }
    }

    @Override
    protected void onStart() {
if(user!=null)
{
    startActivity(new Intent(MainActivity.this, MainFrame.class));
    finish();
}
        super.onStart();

    }



}

