package com.example.univity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

public class Tutorial3Activity extends AppCompatActivity {

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial3);


        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");

        TextView nicknameText1 = (TextView) findViewById(R.id.tutorial3_nicknameText1);
        TextView nicknameText2 = (TextView) findViewById(R.id.tutorial3_nicknameText2);
        TextView nicknameText3 = (TextView) findViewById(R.id.tutorial3_nicknameText3);

        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserAccount user = snapshot.getValue(UserAccount.class);
                nicknameText1.setText(String.valueOf(user.getNickname()));
                nicknameText2.setText(String.valueOf(user.getNickname()));
                nicknameText3.setText(String.valueOf(user.getNickname()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ConstraintLayout tutorial3_1_set = (ConstraintLayout) findViewById(R.id.tutorial3_1_set);
        ImageView tutorial3_1_button = (ImageView) findViewById(R.id.tutorial3_1_button);
        ConstraintLayout tutorial3_2_set = (ConstraintLayout) findViewById(R.id.tutorial3_2_set);
        ImageView tutorial3_2_button = (ImageView) findViewById(R.id.tutorial3_2_button);
        ConstraintLayout tutorial3_3_set = (ConstraintLayout) findViewById(R.id.tutorial3_3_set);
        ImageView tutorial3_3_button = (ImageView) findViewById(R.id.tutorial3_3_button);
        ConstraintLayout tutorial3_4_set = (ConstraintLayout) findViewById(R.id.tutorial3_4_set);
        ImageView tutorial3_4_button = (ImageView) findViewById(R.id.tutorial3_4_button);

        // tutorial 3-1 : 레벨 업 게이지를 다 채우면 내 몸에 변화가 생겨!
        tutorial3_1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // sound
                Context c = arg0.getContext();
                MediaPlayer m = MediaPlayer.create(c , R.raw.pop);
                m.start();
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        mp.stop();
                        mp.release();
                    }
                });

                tutorial3_1_set.setVisibility(View.GONE);
                tutorial3_2_set.setVisibility(View.VISIBLE);
            }
        });

        // tutorial 3-2 : 레벨 업 게이지를 다 채우면 내 몸에 변화가 생겨!
        tutorial3_2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // sound
                Context c = arg0.getContext();
                MediaPlayer m = MediaPlayer.create(c , R.raw.pop);
                m.start();
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        mp.stop();
                        mp.release();
                    }
                });

                tutorial3_2_set.setVisibility(View.GONE);
                tutorial3_3_set.setVisibility(View.VISIBLE);
            }
        });

        // tutorial 3-3 : 레벨 업 게이지를 다 채우면 내 몸에 변화가 생겨!
        tutorial3_3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // sound
                Context c = arg0.getContext();
                MediaPlayer m = MediaPlayer.create(c , R.raw.pop);
                m.start();
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        mp.stop();
                        mp.release();
                    }
                });

                tutorial3_3_set.setVisibility(View.GONE);
                tutorial3_4_set.setVisibility(View.VISIBLE);
            }
        });

        // tutorial 3-4 : 레벨 업 게이지를 다 채우면 내 몸에 변화가 생겨!
        tutorial3_4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // sound
                Context c = arg0.getContext();
                MediaPlayer m = MediaPlayer.create(c , R.raw.pop);
                m.start();
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp)
                    {
                        mp.stop();
                        mp.release();
                    }
                });

                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserAccount user = snapshot.getValue(UserAccount.class);
                        Log.d("LOG", "dd");

                        if(user.getTutorial()){
                            // 튜토리얼 재생 했다 (true) -> 설정화면
                            finish();
                        }
                        else{
                            Log.d("LOG", "2");
                            // 튜토리얼 재생 *안 했다 (false) -> 튜토리얼 (스토리, newuser1, newuser2)

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("tutorial",true); //tutorial
                            mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);

                            // -> 메인
                            Intent intent = new Intent(Tutorial3Activity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


                finish();
            }
        });

    }
}