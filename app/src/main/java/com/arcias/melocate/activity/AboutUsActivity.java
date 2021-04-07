package com.arcias.melocate.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arcias.melocate.R;


public class AboutUsActivity extends AppCompatActivity {
private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
toolbar=findViewById(R.id.tool_bar);
setSupportActionBar(toolbar);
getSupportActionBar().setTitle("About Us");
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});

    }
}