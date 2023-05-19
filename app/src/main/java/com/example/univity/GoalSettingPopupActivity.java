package com.example.univity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.lang.reflect.Field;
import java.util.HashMap;


public class GoalSettingPopupActivity extends Activity {

    ImageView settingYesBtn;
    int newgoal=0;
    boolean sound = true;

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_setting_popup);
        settingYesBtn = (ImageView) findViewById(R.id.settingYesBtn);


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


        NumberPicker picker = (NumberPicker) findViewById(R.id.picker);
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

        settingYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("goalSteps",newgoal);
                        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                        Log.d("LOG", String.valueOf(newgoal));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                if(sound == true){
                    Context c = view.getContext();

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
                }
            }

        });

    }
}