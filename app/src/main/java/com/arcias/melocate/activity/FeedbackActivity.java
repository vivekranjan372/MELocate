package com.arcias.melocate.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arcias.melocate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RadioButton radioButton;
    private EditText reportText;
    private Button sendButton;
    private ImageButton sendImage;
    private RadioGroup radioGroup;
    private AlertDialog alertDialog;
    private ProgressDialog dialog;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        toolbar=findViewById(R.id.tool_bar);

        reportText=findViewById(R.id.report_text);
        sendButton=findViewById(R.id.submit_button);
        sendImage=findViewById(R.id.submit_image);
        radioGroup=findViewById(R.id.radio_group);

        alertDialog=new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Reminder !!");
        dialog=new ProgressDialog(this);
        dialog.setMessage("Accepting Request..");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        finishActivity();
        radioOperation();

    }

    private  void  finishActivity()
    {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.feedback_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {
            case R.id.report_history:
            {
                startActivity(new Intent(FeedbackActivity.this, MyReportsActivity.class));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void radioOperation()
    {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issueOrSuggestion();
            }
        });
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issueOrSuggestion();
            }
        });

    }
    private void issueOrSuggestion()
    {
        int selectedItemId=radioGroup.getCheckedRadioButtonId();
        radioButton=(RadioButton)findViewById(selectedItemId);
        if(TextUtils.isEmpty(reportText.getText()))
        {
            alertDialog.setMessage("please do not leave empty\n" +
                    "your valuable feedback is important for us");
            alertDialog.show();
        }
        else
        {
            try {
                if(radioButton.getId()== R.id.issue_radio)
                {
                    issueCollections();
                }
                else if(radioButton.getId()== R.id.suggestion_radio)
                {
                    suggestionCollections();
                }

            }
            catch(NullPointerException e)
            {
                alertDialog.setMessage("please choose any of the above options");
                alertDialog.show();
            }
        }
    }
    private void issueCollections()
    {
        dialog.show();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Issue").child(currentUser.getUid());
        String postId=reference.push().getKey();
        HashMap<String, Object> map=new HashMap<>();
        map.put("postId",postId);
        map.put("issue",reportText.getText().toString());

        reference.child(postId).setValue(map).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            dialog.dismiss();
                            reportText.setText("");
                            alertDialog.setMessage("Thanks for informing us " +
                                    "about the issues, we will try to fix it very soon\n" +
                                    "Developers Team");
                            alertDialog.show();
                        }
                    }
                }
        );
    }
    private void suggestionCollections()
    {
        dialog.show();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference()
                .child("Suggestions").child(currentUser.getUid());
        String postId=reference.push().getKey();
        HashMap<String, Object> map=new HashMap<>();
        map.put("postId",postId);
        map.put("suggestion",reportText.getText().toString());

        reference.child(postId).setValue(map).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            dialog.dismiss();
                            reportText.setText("");
                            alertDialog.setMessage("Thanks for suggesting us," +
                                    "we will mind on your suggestion");
                            alertDialog.show();
                        }
                    }
                }
        );
    }
}
