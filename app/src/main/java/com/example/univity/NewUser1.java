package com.example.univity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class NewUser1 extends AppCompatActivity {

    // firebase 1
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    ImageView user1;
    ImageView letter_light;

    TextView letter_text1;
    TextView letter_text2;
    TextView letter_text3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user1);

        user1 = findViewById(R.id.user1);
        user1.setVisibility(View.INVISIBLE);

        letter_light = findViewById(R.id.letter_light);

        letter_text1 = findViewById(R.id.letter_text1);
        letter_text2 = findViewById(R.id.letter_text2);
        letter_text3 = findViewById(R.id.letter_text3);

        letter_text1.setVisibility(View.INVISIBLE);
        letter_text2.setVisibility(View.INVISIBLE);
        letter_text3.setVisibility(View.INVISIBLE);

        // firebase2
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity").child("UserAccount");

        //<--firebase-->
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //firebase 3 -----------------------------------/
                UserAccount user = snapshot.getValue(UserAccount.class);
                /*-----------------------------------------*/

                letter_text1.setText("나 "+String.valueOf(user.getNickname())+"은(는) 유니를");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        // 애니메이션

        final Animation text_anim = AnimationUtils.loadAnimation(this, R.anim.anim_floating);
        final Animation light_rotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        final Animation button_alpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_visible);
        final Animation button_click = AnimationUtils.loadAnimation(this, R.anim.anim_buttonclick);



        letter_text1.startAnimation(text_anim); // 첫번째줄 글씨 올라옴
        letter_text2.startAnimation(text_anim); // 첫번째줄 글씨 올라옴
        letter_text3.startAnimation(text_anim); // 첫번째줄 글씨 올라옴
//        user1.startAnimation(button_alpha); // 버튼 나타남

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                            public void run() {
                                user1.startAnimation(button_alpha); // 버튼 나타남
                            }
                        }, 1000);

//        letter_text1.setVisibility(View.VISIBLE);
//        letter_text1.startAnimation(text_anim); // 첫번째줄 글씨 올라옴
//        // 500 밀리 초간 멈추게 하고싶다면
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                letter_text2.setVisibility(View.VISIBLE);
//                letter_text2.startAnimation(text_anim); // 2번째줄 글씨 올라옴
//
//                // 500 밀리 초간 멈추게 하고싶다면
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//                        letter_text3.setVisibility(View.VISIBLE);
//                        letter_text3.startAnimation(text_anim); // 3번째줄 글씨 올라옴
//
//                        // 1000 밀리 초간 멈추게 하고싶다면
//                        handler.postDelayed(new Runnable() {
//                            public void run() {
//                                user1.setVisibility(View.VISIBLE);
//                                user1.startAnimation(button_alpha); // 버튼 나타남
//                            }
//                        }, 1000);
//                    }
//                }, 500);
//
//            }
//        }, 500);

        letter_light.startAnimation(light_rotate); // 빛 반짝


        user1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 튜토리얼 (스토리, "newuser1", newuser2)
                user1.startAnimation(button_click);
                Intent intent = new Intent(NewUser1.this, NewUser2.class);
                startActivity(intent);
                finish();
            }
        });

    }


}