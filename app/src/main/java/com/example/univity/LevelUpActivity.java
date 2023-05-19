package com.example.univity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class LevelUpActivity extends AppCompatActivity {


    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_up);


        // 비디오뷰 가져오기
        VideoView mVideoView = (VideoView) findViewById(R.id.screenVideoView);



        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity").child("UserAccount");

        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("LOG", "4");
                UserAccount user = snapshot.getValue(UserAccount.class);

                if(user.getLevel() == 2){

                    // sample.mp4 설정
                    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/level_up_1to2");
                    mVideoView.setVideoURI(uri);

                }else if(user.getLevel() == 3){

                    // sample.mp4 설정
                    Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/level_up_2to3");
                    mVideoView.setVideoURI(uri);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        // 리스너 등록 (영상재생)
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 준비 완료되면 비디오 재생
                mp.start();

                // 5 초간 멈추게 하고싶다면
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        Intent intent = new Intent(LevelUpActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, 11000);

            }
        });


    }
}