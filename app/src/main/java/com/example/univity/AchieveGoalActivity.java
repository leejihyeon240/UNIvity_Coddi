package com.example.univity;

import android.content.Intent;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

public class AchieveGoalActivity extends AppCompatActivity {

    private TextView tvNickname;
    private ImageView goHomeBtn;
    private ImageView achieveGoalBg;

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve_goal);

        tvNickname = (TextView) findViewById(R.id.tvNickname);
        goHomeBtn = (ImageView) findViewById(R.id.goHomeBtn);
        achieveGoalBg = (ImageView)findViewById(R.id.achieveGoalBg);

        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");

        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("goalIsOk",false); //ture였던 isGoalOk를 false로 변경
                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserAccount user = snapshot.getValue(UserAccount.class);

                tvNickname.setText(String.valueOf(user.getNickname()));

                switch (user.getLevel()){
                    case 1 :
                        achieveGoalBg.setImageResource(R.drawable.achieve_goal_bg_1);
                        achieveGoalBg.setMaxWidth(1440);
                        achieveGoalBg.setMaxHeight(3040);
                    break;
                    case 2 : achieveGoalBg.setImageResource(R.drawable.achieve_goal_bg_2); break;
                    case 3 : achieveGoalBg.setImageResource(R.drawable.achieve_goal_bg_3); break;
                    default: break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        goHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(AchieveGoalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });





    }
}