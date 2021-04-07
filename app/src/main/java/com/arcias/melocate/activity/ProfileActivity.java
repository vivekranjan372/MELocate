package com.arcias.melocate.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arcias.melocate.Model.User;
import com.arcias.melocate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
private Toolbar toolbar;
private CircleImageView profileImage;
private TextView name;
private TextView email;
private TextView mobileNumber;
private Button changePassword;
private Button submit;
private EditText newPassword;
private EditText conNewPassword;
private FirebaseUser currentUser;
private LinearLayout bottomView;
private boolean visible=true;
private AlertDialog alertDialog;
private ProgressDialog progressDialog,dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileImage=findViewById(R.id.profile_image);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        mobileNumber=findViewById(R.id.mobile_number);
        changePassword=findViewById(R.id.change_password);
        submit=findViewById(R.id.submit);
        newPassword=findViewById(R.id.new_password);
        conNewPassword=findViewById(R.id.confirm_new_password);
currentUser= FirebaseAuth.getInstance().getCurrentUser();
bottomView=findViewById(R.id.bottom_view);
alertDialog=new AlertDialog.Builder(this).create();
alertDialog.setTitle("Alert !!");
progressDialog=new ProgressDialog(this);


        toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        dialog=new ProgressDialog(this);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        getUserInfo();

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(visible)
            {
                bottomView.setVisibility(View.VISIBLE);
                visible=false;
            }
            else {
                bottomView.setVisibility(View.GONE);
                visible=true;
            }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePass();
            }
        });
    }
private void changePass()
{
    progressDialog.setMessage("Please Wait");
    progressDialog.show();
    final String pass=newPassword.getText().toString();
    final String conPass=conNewPassword.getText().toString();
    if( TextUtils.isEmpty(pass)|| TextUtils
    .isEmpty(conPass))
    {
        alertDialog.setMessage("Empty is not Allowed");
        alertDialog.show();
        progressDialog.dismiss();
    }
    else if((pass.length())<6||conPass.length()<6)
    {
        alertDialog.setMessage("Password Length must be greater than 6");
        alertDialog.show();
        progressDialog.dismiss();
    }
    else if(!(pass.equals(conPass)))
    {
        alertDialog.setMessage("Password did not match");
        alertDialog.show();
        progressDialog.dismiss();
    }
    else
        {
        currentUser.updatePassword(pass).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful())
                     {

                         FirebaseDatabase.getInstance()
                                 .getReference().child("Registered Users").child(currentUser
                         .getUid()).child("password").setValue(pass);
progressDialog.dismiss();
                         alertDialog.setMessage("Your Password is " +
                                 "Successfully changed");
                         alertDialog.show();
                         newPassword.setText("");
                         conNewPassword.setText("");
                         bottomView.setVisibility(View.GONE);
                         visible=true;
                     }
                     else
                     {
                         alertDialog.setMessage(task.getException().getMessage());
                         alertDialog.show();
                     }
                    }
                }
        );
    }

}
    private void getUserInfo() {
        FirebaseDatabase.getInstance().getReference().child("Registered Users")
                .child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.getValue(User.class);
                    name.setText(user.getFirstName()+" "+user.getLastName());
                    email.setText(user.getEmail());
                    mobileNumber.setText(user.getMobileNumber());
                        Picasso.get().load(R.drawable.icon).into(profileImage);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
