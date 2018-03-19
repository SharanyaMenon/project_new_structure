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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.Models.Trainer;
import com.example.toshimishra.photolearn.Utilities.Constants;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TrainerAddQuizTitle extends AppCompatActivity {
    Button button;
    EditText et;
    TextView text_ls,textView_mode;
    String key,value;
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_add_quiztitle);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle(Constants.PHOTOLEARN);
        toolbar.setSubtitle(Constants.PARTICIPANT);
        if (State.isTrainerMode()) {
            toolbar.setSubtitle(Constants.TRAINER);
        }
        toolbar.setSubtitleTextColor(Color.WHITE);
        // toolbar.setLogo(R.drawable.timg);
        toolbar.setNavigationIcon(R.drawable.ww);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        text_ls = (TextView)findViewById(R.id.title_LS);
        button = (Button)findViewById(R.id.bt_Add);
        et = (EditText)findViewById(R.id.xh_txt);
        text_ls.setText(State.getCurrentSession().getCourseCode());
        textView_mode = (TextView)findViewById(R.id.textView4);
        if(!State.isUpdateMode()) { //Add Mode
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = et.getText().toString();
                    if (title == null || title.isEmpty()) {
                        Toast.makeText(TrainerAddQuizTitle.this, Constants.ADD_TITLE, Toast.LENGTH_SHORT).show();
                    } else {
                        ((Trainer)State.getCurrentUser()).createQuizTitle(title);
                        finish();
                    }

                }
            });
        }else{//update mode
            button.setText("Update");
            textView_mode.setText("Update Quiz Title");
            Bundle b = getIntent().getExtras();
            key = b.getString("key");
            value = b.getString("value");
            et.setText(value);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    value = et.getText().toString(); // changed title
                    new Trainer().updateQuizTitle(key,value);
                    finish();

                }
            });
        }
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
}

