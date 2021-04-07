package com.arcias.melocate.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.arcias.melocate.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Button registerView;
    private Button login;
    private EditText email;
    private EditText password;
    private AlertDialog alertDialog;
    private ProgressDialog dialog;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerView=findViewById(R.id.registerView);

        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Login Status");
        dialog=new ProgressDialog(this);
        myAuth=FirebaseAuth.getInstance();
        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.
                        this,RegisterActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Please Wait");
                dialog.show();
                final String getEmail=email.getText().toString();
                String getPassword=password.getText().toString();

            if(TextUtils.isEmpty(getEmail)|| TextUtils.isEmpty(getPassword))
            {
                alertDialog.setMessage("Empty is not allowed");
                alertDialog.show();
                dialog.dismiss();
            }
            else
            {
                myAuth.signInWithEmailAndPassword(getEmail,getPassword)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                dialog.dismiss();
                                alertDialog.setMessage("You are Successfully Logged in");
                                alertDialog.show();
                                startActivity(new Intent(
                                        LoginActivity.this, MainFrame.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        email.setText("");password.setText("");
                        alertDialog.setMessage(e.getMessage());
                        alertDialog.show();
                        dialog.dismiss();
                    }
                });
            }
            }
        });


    }

}
