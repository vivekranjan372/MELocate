package com.arcias.melocate.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.arcias.melocate.R;

public class SplashScreenActivity extends AppCompatActivity {
private static long SPLASH_TIME_OUT =3000;
private SharedPreferences preferences;
private  boolean isFirstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

      try {
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  preferences=getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
                  isFirstTime=preferences.getBoolean("firstTime",true);
                  if(isFirstTime)
                  {
                      SharedPreferences.Editor editor=preferences.edit();
                      editor.putBoolean("firstTime",false);
                      editor.apply();
                      startActivity(new Intent(SplashScreenActivity.this
                              , OnBoardingScreen.class));
finish();

                  }
                  else
                  {
                      startActivity(new Intent(SplashScreenActivity.this,
                              MainActivity.class));
                      finish();
                  }



              }
          },SPLASH_TIME_OUT);
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }

    }
}