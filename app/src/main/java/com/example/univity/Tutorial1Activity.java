package com.example.univity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class Tutorial1Activity extends AppCompatActivity {

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);

        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");

        TextView nicknameText = (TextView) findViewById(R.id.tutorial1_nicknameText);

        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserAccount user = snapshot.getValue(UserAccount.class);
                nicknameText.setText(String.valueOf(user.getNickname()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        ConstraintLayout tutorial1_1_set = (ConstraintLayout) findViewById(R.id.tutorial1_1_set);
        ImageView tutorial1_1_button = (ImageView) findViewById(R.id.tutorial1_1_button);
        ConstraintLayout tutorial1_2_set = (ConstraintLayout) findViewById(R.id.tutorial1_2_set);
        ImageView tutorial1_2_button = (ImageView) findViewById(R.id.tutorial1_2_button);

        // tutorial 1-1 : 현재 걸음수와 목표 달성률을 알 수 있어
        tutorial1_1_button.setOnClickListener(new View.OnClickListener() {
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

                tutorial1_1_set.setVisibility(View.GONE);
                tutorial1_2_set.setVisibility(View.VISIBLE);
            }
        });


        // tutorial 1-2 : AR걷기모드를 클릭해볼까?
        tutorial1_2_button.setOnClickListener(new View.OnClickListener() {
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

                Intent intent = new Intent(Tutorial1Activity.this, Tutorial2Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}