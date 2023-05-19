package com.example.univity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

public class StorePopupActivity extends AppCompatActivity {


    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    private ImageView storeYesButton;
    private TextView tvShopItem, tvShopItem2;

    private String coin, item, item2;

    private String thunder;
    boolean sound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //타이틀바 없애기
        setContentView(R.layout.activity_store_popup);

        // 버튼 클릭 애니메이션
        final Animation anim_buttonclick = AnimationUtils.loadAnimation(this, R.anim.anim_buttonclick);

        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");

        tvShopItem = (TextView)findViewById(R.id.tvShopItem);
        tvShopItem2 = (TextView)findViewById(R.id.tvShopItem2);

        Intent intent = getIntent();
        coin = intent.getStringExtra("COIN");
        thunder = intent.getStringExtra("THUNDER");
        item = intent.getStringExtra("ITEM");
        item2 = intent.getStringExtra("ITEM2");


        tvShopItem.setText(item);
        tvShopItem2.setText(item2);


        storeYesButton = (ImageView)findViewById(R.id.failOkBtn);

        storeYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserAccount user = snapshot.getValue(UserAccount.class);

                        coin = String.valueOf(user.getCoin()-Integer.valueOf(coin));

                        thunder = String.valueOf(user.getThunder()+Integer.valueOf(thunder));

                        HashMap<String, Object> map = new HashMap<>();

                        map.put("coin",Integer.valueOf(coin));

                        if(Integer.valueOf(thunder) >= 1000){
                            int temp = Integer.valueOf(thunder) - 1000;

                            map.put("thunder",temp);

                            mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);

                            if(user.getLevel() == 1){
                                map.put("level",2);
                                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                                Intent intent = new Intent(StorePopupActivity.this, LevelUpActivity.class);
                                startActivity(intent);
                            }else if(user.getLevel() == 2) {
                                map.put("level", 3);
                                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                                Intent intent = new Intent(StorePopupActivity.this, LevelUpActivity.class);
                                startActivity(intent);
                            }

                        }else{
                            map.put("thunder",Integer.valueOf(thunder));
                            mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                        }
                        finish();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                storeYesButton.startAnimation(anim_buttonclick);

                if(sound == true){
                    Context c = view.getContext();

                    MediaPlayer m = MediaPlayer.create(c , R.raw.coin_sound);
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


        ImageView storeNoButton = (ImageView) findViewById(R.id.storeNoButton);
        storeNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                storeNoButton.startAnimation(anim_buttonclick);

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
                finish();
            }
        });
    }

}