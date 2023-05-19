package com.example.univity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class StoryActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    boolean tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // 비디오뷰 가져오기
        VideoView mVideoView = (VideoView) findViewById(R.id.screenVideoView);

        // sample.mp4 설정
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/storytelling_video");
        mVideoView.setVideoURI(uri);

        // 리스너 등록 (영상재생)
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 준비 완료되면 비디오 재생
                mp.start();

//                // 5 초간 멈추게 하고싶다면
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        Log.d("LOG", "video");
//                    }
//                }, 5000);

            }
        });


        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity").child("UserAccount");

        Log.d("LOG", "3");

        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d("LOG", "4");
                UserAccount user = snapshot.getValue(UserAccount.class);
                tutorial = user.getTutorial();

                Log.d("LOG", String.valueOf(user.getTutorial()));
                // 30초 뒤에 실행
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (tutorial) {
                            // 튜토리얼 재생 했다 (true) -> 그냥 종료만 (설정화면에서 스토리 재생한 경우임)
                            Log.d("LOG", "그냥 종료만");
                            finish();
                        } else {
                            // 튜토리얼 재생 *안 했다 (false) -> 튜토리얼 (스토리, "newuser1", newuser2)
                            Log.d("LOG", "newuser1");
                            Intent intent = new Intent(StoryActivity.this, NewUser1.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }, 30000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
