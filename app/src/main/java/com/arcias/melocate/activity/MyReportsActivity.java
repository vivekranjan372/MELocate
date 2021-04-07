package com.arcias.melocate.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcias.melocate.Adapter.SuggestionAdapter;
import com.arcias.melocate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyReportsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView myIssue;
    private TextView mySuggestions;
    private RecyclerView issueRecyclerView;
    private RecyclerView suggestionRecyclerView;
    private boolean clickMe=true;
    private List<String> suggestionList;
    private FirebaseUser currentUser;
    private SuggestionAdapter suggestionAdapter;
    private ProgressDialog dialog;
 private LinearLayout linearLayout;
 private int SUGGESTION=0;
 private int getId=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);
        toolbar=findViewById(R.id.tool_bar);
        myIssue=findViewById(R.id.issue_text);
        mySuggestions=findViewById(R.id.suggestion_text);
        issueRecyclerView=findViewById(R.id.issue_recycler_view);
        suggestionRecyclerView=findViewById(R.id.suggestion_recycler_view);
        linearLayout=findViewById(R.id.layout_no_records);
suggestionList=new ArrayList<>();

currentUser= FirebaseAuth.getInstance().getCurrentUser();
        dialog=new ProgressDialog(this);



    quitActivity();
    showIssue();
    showSuggestions();


    }
    private  void  quitActivity() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Reports");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private  void showIssue()
    {
        myIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(clickMe)
               {
SUGGESTION=321;
getId=myIssue.getId();
processingAction();


               }
               else
               {
                   linearLayout.setVisibility(View.GONE);
                   suggestionRecyclerView.setVisibility(View.GONE);
                   issueRecyclerView.setVisibility(View.GONE);
                   clickMe=true;
             if(getId== R.id.suggestion_text)
             {
                 SUGGESTION=321;
                 processingAction();
                 getId=0;
             }



               }
            }
        });
    }
    private void showSuggestions()
    {
        mySuggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(clickMe)
                {
                    SUGGESTION=123;
                    getId=mySuggestions.getId();
                    processingAction();


                }
                else
                {
                   linearLayout.setVisibility(View.GONE);
                    suggestionRecyclerView.setVisibility(View.GONE);
                    issueRecyclerView.setVisibility(View.GONE);
                    clickMe=true;
                    if(getId== R.id.issue_text)
                    {
                        SUGGESTION=123;
                        processingAction();
                        getId=0;
                    }

                }

            }
        });

    }
private void addSuggestion()
{
    FirebaseDatabase.getInstance().getReference().child("Suggestions")
            .child(currentUser.getUid()).addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    suggestionList.clear();
               for(DataSnapshot snapshot:dataSnapshot.getChildren())
               {
                   String getSuggestion=snapshot.child
                           ("suggestion").getValue(String.class);
                   suggestionList.add((getSuggestion));
               }
if(suggestionList.size()==0)
{
   linearLayout.setVisibility(View.VISIBLE);
    dialog.dismiss();
    clickMe=false;
}
      else
{
    callingSuggestionAdapter();
    suggestionAdapter.notifyDataSetChanged();
}


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }
    );
}
private void callingSuggestionAdapter()
{
    suggestionRecyclerView.setHasFixedSize(true);
    suggestionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    suggestionAdapter=new SuggestionAdapter(this,suggestionList);
    suggestionRecyclerView.setAdapter(suggestionAdapter);
    dialog.dismiss();
    suggestionRecyclerView.setVisibility(View.VISIBLE);
    clickMe=false;

}
private void processingAction()
{
    dialog.show();

dialog.setCancelable(false);
    dialog.setContentView(R.layout.progress_dialog);
    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

if(SUGGESTION==123)
{
    addSuggestion();
}
else if(SUGGESTION==321)
{
   addIssue();
}



}
private void addIssue()
{
    FirebaseDatabase.getInstance().getReference().child("Issue")
            .child(currentUser.getUid()).addValueEventListener(
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    suggestionList.clear();
                    for(DataSnapshot snapshot:dataSnapshot.getChildren())
                    {
                        String getSuggestion=snapshot.child
                                ("issue").getValue(String.class);
                        suggestionList.add((getSuggestion));
                    }
                    if(suggestionList.size()==0)
                    {
                        linearLayout.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                        clickMe=false;
                    }
                    else
                    {
                       callingIssueAdapter();
                        suggestionAdapter.notifyDataSetChanged();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }
    );
}
private void callingIssueAdapter()
{
    issueRecyclerView.setHasFixedSize(true);
    issueRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    suggestionAdapter=new SuggestionAdapter(this,suggestionList);
    issueRecyclerView.setAdapter(suggestionAdapter);
    dialog.dismiss();
    issueRecyclerView.setVisibility(View.VISIBLE);
    clickMe=false;
}
            }