package com.example.univity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

public class NewUser2 extends AppCompatActivity {

    ImageView user2;
    int newgoal=0;

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user2);

        user2 = findViewById(R.id.user2);

        //firebase-----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");
        /*-----------------------------------------*/

        NumberPicker.Formatter mFormatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                String result = (value * 1000) + "보";

                // TODO Auto-generated method stub
                return result;
            }
        };

        NumberPicker picker = (NumberPicker) findViewById(R.id.new_picker);
        picker.setMinValue(1);
        picker.setMaxValue(15);
        picker.setValue(10); // 초기 선택값
        picker.setWrapSelectorWheel(false); // 범위 내에서만 이동
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); // 키보드 입력 불가
        picker.setFormatter(mFormatter); // ~보 라고 보여지도록함

        // NumberPicker로 선택한 값은 numberPicker.value에 int 형식으로 저장

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                // TODO Auto-generated method stub
                newgoal = newVal*1000;
            }

        });


        user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("goalSteps",newgoal);
//                        map.put("tutorial",true); //tutorial // ** 0518
                        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                        Log.d("LOG", String.valueOf(newgoal));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

//                // 튜토리얼 (스토리, newuser1, "newuser2") // 0518
//                Intent intent = new Intent(NewUser2.this, MainActivity.class);
//                startActivity(intent);
//                finish();

                // 튜토리얼 (스토리, newuser1, "newuser2")
                Intent intent = new Intent(NewUser2.this, Tutorial1Activity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}