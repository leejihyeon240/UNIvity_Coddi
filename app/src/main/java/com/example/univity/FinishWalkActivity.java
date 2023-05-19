package com.example.univity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.ar.core.Frame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

public class FinishWalkActivity extends AppCompatActivity {

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/


    private TextView myRunningTime;
    private TextView myRunningStep;
    private ImageView exitButton;
    private ImageView titleText;
    private ImageView uni1;
    private ImageView uni2;
    private ImageView uniLevel3;
    private ImageView shadow;
    private FrameLayout reward1;
    private FrameLayout reward2;
    private LinearLayout resultText;
    private TextView tvReward1, tvReward2;

    private double myThunder;


    AnimationDrawable aniUniLevel3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_walk);

        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");

        // 애니메이션
        final Animation anim_uni1 = AnimationUtils.loadAnimation(this, R.anim.anim_uni_rotate_trans);
        final Animation anim_uni2 = AnimationUtils.loadAnimation(this, R.anim.anim_uni_rotate);
        final Animation anim_buttonclick = AnimationUtils.loadAnimation(this, R.anim.anim_buttonclick);
        final Animation anim_floating = AnimationUtils.loadAnimation(this, R.anim.anim_floating);
        final Animation anim_floating3 = AnimationUtils.loadAnimation(this, R.anim.anim_floating3);
        final Animation anim_alpha_visible = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_visible);


        myRunningTime = (TextView)findViewById(R.id.myRunningTime);
        myRunningStep = (TextView)findViewById(R.id.myRunningStep);

        exitButton = (ImageView)findViewById(R.id.exitButton);
        titleText = (ImageView)findViewById(R.id.titleText);

        uni1 = (ImageView)findViewById(R.id.uni1);
        uni2 = (ImageView)findViewById(R.id.uni2);
        uniLevel3 = (ImageView)findViewById(R.id.uniLevel3);
        shadow = (ImageView)findViewById(R.id.shadow);

        //번개, 코인 보상(이미지 띄우기, 값 변경)
        reward1 = (FrameLayout) findViewById(R.id.reward1);
        reward2 = (FrameLayout)findViewById(R.id.reward2);
        tvReward1 = (TextView)findViewById(R.id.tvReward1);
        tvReward2 = (TextView)findViewById(R.id.tvReward2);


        resultText = (LinearLayout)findViewById(R.id.resultText);


        Intent intent = getIntent();
        String myRunTime = intent.getStringExtra("TIME");
        String myStep = intent.getStringExtra("STEP");
        String myNowCoin = intent.getStringExtra("COIN");
        String cDate = intent.getStringExtra("CDATE");
        String strStartTime = intent.getStringExtra("STARTTIME");
        String strEndTime = intent.getStringExtra("ENDTIME");

        Log.d("CHCH",myRunTime);
        Log.d("CHCH",myStep);
        Log.d("CHCH",cDate);
        Log.d("CHCH",strStartTime);
        Log.d("CHCH",strEndTime);


        myRunningTime.setText(myRunTime);
        myRunningStep.setText(myStep);
        tvReward2.setText("+ " + myNowCoin);

        HashMap<String, Object> map = new HashMap<>();


        map.put("startTime",strStartTime);
        map.put("endTime",strEndTime);
        map.put("step",myStep);

        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child(cDate).push().setValue(map);

   /*     mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-05-08").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-05-08").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-05-10").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-05-14").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-05-12").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-05-17").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-05-17").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-05-09").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-02").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-02").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-03").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-04").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-06").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-07").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-10").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-12").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-13").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-14").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-19").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-21").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-22").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-23").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-25").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-27").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-04-30").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-03-09").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-03-19").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-03-21").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-03-22").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-03-23").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-03-25").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-03-27").push().setValue(map);
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar").child("2022-03-30").push().setValue(map);*/




        //방금 걸은 걸음수 / 사용자의 목표 걸음수에 따라서 thunder 획득
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserAccount user = snapshot.getValue(UserAccount.class);

                myThunder = Double.valueOf(myStep)/Double.valueOf(user.getGoalSteps()) * 10;

                tvReward1.setText("+" + String.valueOf(Integer.valueOf((int) myThunder)));

                //레벨 별로 유니 이미지 바꾸기
                if(user.getLevel() == 1){

//        titleText.startAnimation(anim_up); // 걷기완료! 텍스트 위로 올리기

                    // 400 밀리 초간 멈추게 하고싶다면
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            /*uni1.setImageResource(R.drawable.uni_big_img_level1);
                            uni2.setImageResource(R.drawable.uni_bbaggom_img_level1);*/

                            uni1.startAnimation(anim_uni1); // 유니가 오른쪽으로 굴러간다
                            uni2.startAnimation(anim_uni2); // 큰유니가 화면 오른쪽에서 빼꼼한다
                            shadow.startAnimation(anim_alpha_visible); // 큰유니의 그림자..
                            reward1.startAnimation(anim_floating); // 리워드 애니메이션
                            reward2.startAnimation(anim_floating); // 리워드 애니메이션
                            resultText.startAnimation(anim_alpha_visible);
                            titleText.startAnimation(anim_floating3); // 걷기완료! 텍스트 -> S10으로 직접 확인하면서 조정 필요

                        }
                    }, 1500);


                }else if(user.getLevel() == 2){

//        titleText.startAnimation(anim_up); // 걷기완료! 텍스트 위로 올리기

                    // 400 밀리 초간 멈추게 하고싶다면
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
/*                            uni1.setImageResource(R.drawable.uni_big_smile);
                            uni2.setImageResource(R.drawable.uni_big_img);*/

                            uni1.startAnimation(anim_uni1); // 유니가 오른쪽으로 굴러간다
                            uni2.startAnimation(anim_uni2); // 큰유니가 화면 오른쪽에서 빼꼼한다
                            shadow.startAnimation(anim_alpha_visible); // 큰유니의 그림자..
                            reward1.startAnimation(anim_floating); // 리워드 애니메이션
                            reward2.startAnimation(anim_floating); // 리워드 애니메이션
                            resultText.startAnimation(anim_alpha_visible);
                            titleText.startAnimation(anim_floating3); // 걷기완료! 텍스트 -> S10으로 직접 확인하면서 조정 필요
                        }
                    }, 1500);

                }else if(user.getLevel() == 3){
                    uni1.setVisibility(View.INVISIBLE);
                    uni2.setVisibility(View.INVISIBLE);
                    Log.d("LOG", "uni jump =3 !!");
                    //ImageView에 src 속성으로 설정된 이미지를 Drawable 객체로 얻어오기.
                    //Frame Aniamtion은 Drawable의 일종이므로 형변환 가능
                    /*aniUniLevel3 = (AnimationDrawable) uniLevel3.getDrawable();


                    //Frame Animation을 한번만 실행 - 이 코드를 지우면 자동으로 반복함(기본값)
                    aniUniLevel3.setOneShot(true);

                    //AnimationDrawable 객체에게 Frame 변경을 시작하도록 함. - Frame Animation은 한번 start()해주면 계속 Running 상태임.
                    aniUniLevel3.start();*/

                    // 400 밀리 초간 멈추게 하고싶다면
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            shadow.startAnimation(anim_alpha_visible); // 큰유니의 그림자..
                            reward1.startAnimation(anim_floating); // 리워드 애니메이션
                            reward2.startAnimation(anim_floating); // 리워드 애니메이션
                            resultText.startAnimation(anim_alpha_visible);
                            titleText.startAnimation(anim_floating3); // 걷기완료! 텍스트 -> S10으로 직접 확인하면서 조정 필요
                        }
                    }, 1500);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        uni1.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View arg0){
//                uni1.startAnimation(anim_uni1); // 유니가 오른쪽으로 굴러간다
//                uni2.startAnimation(anim_uni2); // 큰유니가 화면 오른쪽에서 빼꼼한다
//
//                // 400 밀리 초간 멈추게 하고싶다면
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        reward1.startAnimation(anim_floating); // 리워드 애니메이션
//                        reward2.startAnimation(anim_floating); // 리워드 애니메이션
//                        resultText.startAnimation(anim_alpha_visible);
//                    }
//                }, 1000);
//
//
//            }
//        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserAccount user = snapshot.getValue(UserAccount.class);
                        Log.d("LOG", "dd");

                        if(user.isGoalIsOk() == true){
                            //사용자가 목표 걸음수를 달성했으면 보여주기
                            exitButton.startAnimation(anim_buttonclick);
                            Intent intent = new Intent(FinishWalkActivity.this, AchieveGoalActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            exitButton.startAnimation(anim_buttonclick);
                            Intent intent;
                            intent = new Intent(FinishWalkActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

}