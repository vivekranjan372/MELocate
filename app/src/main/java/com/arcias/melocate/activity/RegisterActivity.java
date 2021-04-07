package com.arcias.melocate.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.arcias.melocate.Model.User;
import com.arcias.melocate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText mobileNo;
    private EditText password;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private AlertDialog alertDialog;
    private Button loginView;
    private Button register;
    private ProgressDialog progressDialog;
    private String existMobileNo;
    private String getFirstName;
    private String getLastName,getMobileNumber,getEmail,getPassword;
public static  int AUTH_VALUE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Login");
        loginView=findViewById(R.id.login_view);
        firstName=findViewById(R.id.first_name);
        lastName=findViewById(R.id.last_name);
        email=findViewById(R.id.email);
        mobileNo=findViewById(R.id.mobile_number);
        password=findViewById(R.id.password);
        auth=FirebaseAuth.getInstance();
        register=findViewById(R.id.register);
        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Registration Status");
        progressDialog=new ProgressDialog(this);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                progressDialog.setCancelable(false);
                getFirstName=firstName.getText().toString();
                getLastName=lastName.getText().toString();
                 getMobileNumber=mobileNo.getText().toString();
                getEmail=email.getText().toString();
                getPassword=password.getText().toString();

                isInfoCorrect();



            }
        });
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,
                        LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
    }
private void isInfoCorrect()
{

    if(TextUtils.isEmpty(getFirstName)|| TextUtils.isEmpty(getLastName)||
            TextUtils.isEmpty(getEmail)|| TextUtils.isEmpty(getMobileNumber)||
            TextUtils.isEmpty(getPassword))
    {
        alertDialog.setMessage("Empty is not Allowed");
        alertDialog.show();
        progressDialog.dismiss();
    }
    else if(getPassword.length()<6)
    {
        alertDialog.setMessage("Password length should be greater than 6");
        alertDialog.show();
        progressDialog.dismiss();

    }
else if((getMobileNumber.length())!=10)
    {
        alertDialog.setMessage("Please enter Valid mobile number");
        alertDialog.show();
        progressDialog.dismiss();
        Log.d("tag"," "+getMobileNumber.length());
    }

    else {
        FirebaseDatabase.getInstance().getReference().child("Registered Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         int count=0;
                        long totalCountNumbers=0;
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {

                            User user=snapshot.getValue(User.class);
                            totalCountNumbers=snapshot.getChildrenCount();
                            for(DataSnapshot snapshot1:snapshot.getChildren())
                            {
                                count++;
                                if(getMobileNumber.equals(snapshot1
                                        .getValue(String.class)))
                                {
alertDialog.setMessage("This number is already used");
alertDialog.show();
progressDialog.dismiss();
                                    existMobileNo=user.getMobileNumber();

                                    break;
                                }


                            }

                        }
                        totalCountNumbers=totalCountNumbers*dataSnapshot
                                .getChildrenCount();
                        Log.d("tag",existMobileNo+" "+getMobileNumber+" "+totalCountNumbers+
                        " "+count);
                        if(totalCountNumbers==count)
                        {
                            registration(getFirstName,getLastName,getEmail,getMobileNumber,getPassword);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
    private void registration(final String getFirstName, final String getLastName, final String getEmail,
                              final String getMobileNumber, final String getPassword) {
        auth.createUserWithEmailAndPassword(getEmail,getPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                currentUser=auth.getCurrentUser();
                HashMap<String, Object> map=new HashMap<>();
                map.put("firstName",getFirstName);
                map.put("lastName",getLastName);
                map.put("email",getEmail);
                map.put("mobileNumber",getMobileNumber);
                map.put("password",getPassword);
                map.put("id",currentUser.getUid());
                map.put("imageUrl","default");
                FirebaseDatabase.getInstance().getReference().child("Registered Users")
                        .child(currentUser.getUid()).setValue(map).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    alertDialog.setMessage("you are Successfully Registered");
                                    alertDialog.show();
                                    progressDialog.dismiss();
                                    AUTH_VALUE=1;
                                    startActivity(new Intent(RegisterActivity.this,
                                            LoginActivity.class).addFlags(Intent
                                            .FLAG_ACTIVITY_CLEAR_TASK |
                                            Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                }
                                else
                                {
                                    alertDialog.setMessage("Registration failed");
                                    alertDialog.show();
                                    progressDialog.dismiss();
                                }
                            }
                        }
                ) ;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.setMessage(e.getMessage());
                alertDialog.show();
                progressDialog.dismiss();
            }
        });
    }


}
