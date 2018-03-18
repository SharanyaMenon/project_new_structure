package com.example.toshimishra.photolearn.PageView;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.toshimishra.photolearn.Utilities.LoadImage;
import com.example.toshimishra.photolearn.ParticipantAttemptQuizItemActivity;
import com.example.toshimishra.photolearn.Models.QuizAnswer;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.R;
import com.example.toshimishra.photolearn.Utilities.State;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ParticipantPagerViewQI implements LoadImage.Listener {


    private final View mRootView;
    private TextView mQuiz;
    private RadioButton mOption1,mOption2,mOption3,mOption4;
    private DatabaseReference mDatabase;
    private  int choiceselected;
    private QuizItem quizItem;
    private ImageView mImageView;
    private TextView mExplain;

    public ParticipantAttemptQuizItemActivity mMainActivity;
    public final LayoutInflater mInflater;

    public ParticipantPagerViewQI(ParticipantAttemptQuizItemActivity mainActivity, QuizItem qItem,int choiceselected) {
        super();
        mMainActivity = mainActivity;
        mInflater =  LayoutInflater.from(mMainActivity);
        mMainActivity = mainActivity;
        mRootView = initView();

        this.quizItem = qItem;
        this.choiceselected = choiceselected;

    }


    public View initView() {
        View itemView = mInflater.inflate(R.layout.activity_participant_pager_view_qi,null);
        mQuiz=(TextView)itemView.findViewById(R.id.title_Quest) ;
        mOption1=(RadioButton)itemView.findViewById(R.id.radioButton1);
        mOption2=(RadioButton)itemView.findViewById(R.id.radioButton2);
        mOption3=(RadioButton)itemView.findViewById(R.id.radioButton3);
        mOption4=(RadioButton)itemView.findViewById(R.id.radioButton4);
        mImageView = (ImageView)itemView.findViewById(R.id.img);
        mExplain = (TextView)itemView.findViewById(R.id.explain_Answer);
        mExplain.setVisibility(View.GONE);

        if(!State.isReadOnlyQuiz()) {
        mOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOption2.setChecked(false);
                mOption3.setChecked(false);
                mOption4.setChecked(false);
                    writeResponse(1, quizItem.getItemID());

            }
        });
        mOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOption1.setChecked(false);
                mOption3.setChecked(false);
                mOption4.setChecked(false);
                    writeResponse(2, quizItem.getItemID());

            }
        });
        mOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOption1.setChecked(false);
                mOption2.setChecked(false);
                mOption4.setChecked(false);
                    writeResponse(3, quizItem.getItemID());

            }
        });
        mOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOption1.setChecked(false);
                mOption2.setChecked(false);
                mOption3.setChecked(false);
                    writeResponse(4, quizItem.getItemID());

            }
        });
        }
        else
        {
            mOption1.setEnabled(false);
            mOption2.setEnabled(false);
            mOption3.setEnabled(false);
            mOption4.setEnabled(false);


        }

        return itemView;
    }


    public void initData() {
        int COLOR;

       mQuiz.setText((quizItem.getQuestion()));
       mOption1.setText(quizItem.getOption1());
       mOption2.setText(quizItem.getOption2());
       mOption3.setText(quizItem.getOption3());
       mOption4.setText(quizItem.getOption4());

        /*For Read only set different color*/
        if(choiceselected == quizItem.getAnswer()){
           COLOR = Color.GREEN;
        }
        else
            COLOR = Color.RED;
        switch (choiceselected)
       {
           case  1:mOption1.setChecked(true);
               mOption2.setChecked(false);
               mOption3.setChecked(false);
               mOption4.setChecked(false);
               if(State.isReadOnlyQuiz()){
                   mOption1.setTextColor(COLOR);
               }
           break;
           case  2:mOption2.setChecked(true);
               mOption1.setChecked(false);
               mOption3.setChecked(false);
               mOption4.setChecked(false);
               if(State.isReadOnlyQuiz()){
                   mOption2.setTextColor(COLOR);
               }
           break;
           case  3:mOption3.setChecked(true);
               mOption1.setChecked(false);
               mOption2.setChecked(false);
               mOption4.setChecked(false);
               if(State.isReadOnlyQuiz()){
                   mOption3.setTextColor(COLOR);
               }
           break;
           case  4:mOption4.setChecked(true);
               mOption1.setChecked(false);
               mOption2.setChecked(false);
               mOption3.setChecked(false);
               if(State.isReadOnlyQuiz()){
                   mOption4.setTextColor(COLOR);
               }
           break;
       }

        new LoadImage(this,200,300).execute(quizItem.getPhotoURL());

        if(State.isReadOnlyQuiz()) {
            mExplain.setText("Correct Answer:" + quizItem.getAnswer() + "\nExplaination:" + quizItem.getAnsExp());
            mExplain.setVisibility(View.VISIBLE);
    }
    }

     private void writeResponse(int ans,String quiItemID){
         mDatabase = FirebaseDatabase.getInstance().getReference();
         QuizAnswer a = new QuizAnswer(ans);
         mDatabase.child("Users-QuizTitle-QuizItem-QuizAnswer").child(getUid()).child(State.getCurrentQuizTitle().getTitleID()).child(quiItemID).setValue(a);

     }
    @Override
    public void onImageLoaded(Bitmap bitmap) {

        mImageView.setImageBitmap(bitmap);
    }
    public View getRootView() {
        return mRootView;
    }

    public String getUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}