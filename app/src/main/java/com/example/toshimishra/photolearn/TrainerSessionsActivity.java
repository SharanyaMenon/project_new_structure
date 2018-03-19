package com.example.toshimishra.photolearn;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Adapters.SampleRecyclerAdapter;
import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TrainerSessionsActivity extends AppCompatActivity  implements SampleRecyclerAdapter.OnItemClickListener {

    // æSimulate the data loaded in the listview.
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private List<String> dataSet;
    private List<LearningSession> sessionList;
    private SampleRecyclerAdapter adapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_sessions);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(Constants.PHOTOLEARN);
        toolbar.setSubtitle(Constants.PARTICIPANT);
        if (State.isTrainerMode()) {
            toolbar.setSubtitle(Constants.TRAINER);
        }
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ww);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        dataSet = new ArrayList<>();
        sessionList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recy);
        //Set the layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SampleRecyclerAdapter(this,dataSet,LearningSession.class);
        //   Setadapter
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("Users-LearningSessions").child(getUid());

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSet.clear();
                sessionList.clear();
                for ( DataSnapshot val : dataSnapshot.getChildren()){
                    dataSet.add(val.getValue(LearningSession.class).getSessionID());
                    sessionList.add(val.getValue(LearningSession.class));
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button button = (Button)findViewById(R.id.bt_Add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrainerSessionsActivity.this, TrainerAddSessionActivity.class));
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        else if(i == R.id.action_switch){
            startActivity(new Intent(this, State.changeMode()));
            finish();
            return  true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View view, int position, String name) {
        State.setCurrentSession(sessionList.get(position));
        Toast.makeText(getApplicationContext(),"click "+position,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(TrainerSessionsActivity.this,TrainerViewTitlesActivity.class));
    }

    private String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }



}


