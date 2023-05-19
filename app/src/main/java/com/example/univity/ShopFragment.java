package com.example.univity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment {

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    public static TextView coinText, levelText, nicknameText, item_1, item_2, item_3;
    public static TextView levelBarCount;
    public String flag;
    public ImageView levelImg, itemImg1, itemImg2, itemImg3;
    boolean sound = true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_shop, container, false);
        final ProgressBar levelProgressBar = (ProgressBar) rootView.findViewById(R.id.levelProgressBar);

        // 버튼 클릭 애니메이션
        MainActivity activity = (MainActivity) getActivity();
        final Animation anim_buttonclick = AnimationUtils.loadAnimation(activity, R.anim.anim_buttonclick);


        // 버튼에 발생하는 이벤트
        levelBarCount = (TextView) rootView.findViewById(R.id.levelBarCount);
        coinText = (TextView) rootView.findViewById(R.id.coinText);
        levelText = (TextView) rootView.findViewById(R.id.levelText);
        nicknameText = (TextView) rootView.findViewById(R.id.nicknameText);

        item_1 = (TextView) rootView.findViewById(R.id.item_1);
        item_2 = (TextView) rootView.findViewById(R.id.item_2);
        item_3 = (TextView) rootView.findViewById(R.id.item_3);

        levelImg = (ImageView)rootView.findViewById(R.id.levelImg);

        itemImg1 = (ImageView)rootView.findViewById(R.id.itemImg1);
        itemImg2 = (ImageView)rootView.findViewById(R.id.itemImg2);
        itemImg3 = (ImageView)rootView.findViewById(R.id.itemImg3);

        FrameLayout coin15BuyBox = (FrameLayout)rootView.findViewById(R.id.coin15BuyBox);
        FrameLayout coin20BuyBox = (FrameLayout)rootView.findViewById(R.id.coin20BuyBox);
        FrameLayout coin40BuyBox = (FrameLayout)rootView.findViewById(R.id.coin40BuyBox);

        ImageView coin15BuyButton = (ImageView) rootView.findViewById(R.id.coin15BuyButton);
        ImageView coin20BuyButton = (ImageView) rootView.findViewById(R.id.coin20BuyButton);
        ImageView coin40BuyButton = (ImageView) rootView.findViewById(R.id.coin40BuyButton);


        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");

        levelProgressBar.setMax(1000);

        //코인, 레벨 게이지 실시간
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserAccount user = snapshot.getValue(UserAccount.class);

                coinText.setText(String.valueOf(user.getCoin()));
                levelText.setText("Lv." + String.valueOf(user.getLevel()));
                nicknameText.setText(String.valueOf(user.getNickname()));
                levelProgressBar.setProgress(user.getThunder());
                levelBarCount.setText(String.valueOf(user.getThunder()));

                //레벨 별로 유니 이미지 바꾸기
                if(user.getLevel() ==1){
                    levelImg.setImageResource(R.drawable.uni_level_img1);
                    levelImg.setVisibility(View.VISIBLE);
                    item_1.setText("달조각 떡뻥");
                    itemImg1.setImageResource(R.drawable.level1_first_img);
                    item_2.setText("무지개 거품 목욕");
                    itemImg2.setImageResource(R.drawable.level1_second_img);
                    item_3.setText("별똥별 촉감 놀이");
                    itemImg3.setImageResource(R.drawable.level1_third_img);
                }else if(user.getLevel()==2){
                    levelImg.setImageResource(R.drawable.uni_level_img2);
                    levelImg.setVisibility(View.VISIBLE);
                    item_1.setText("소행성 미트볼");
                    itemImg1.setImageResource(R.drawable.level2_first_img);
                    item_2.setText("UFO 쿠키");
                    itemImg2.setImageResource(R.drawable.level2_second_img);
                    item_3.setText("블랙홀 미끄럼틀");
                    itemImg3.setImageResource(R.drawable.level2_third_img);
                }else if(user.getLevel() == 3){
                    levelImg.setImageResource(R.drawable.uni_level_img3);
                    levelImg.setVisibility(View.VISIBLE);
                    item_1.setText("행성 비빔밥");
                    itemImg1.setImageResource(R.drawable.level3_first_img);
                    item_2.setText("달토끼 인절미");
                    itemImg2.setImageResource(R.drawable.level3_second_img);
                    item_3.setText("태양열 온천 목욕");
                    itemImg3.setImageResource(R.drawable.level3_third_img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //15코인 사용 버튼
        coin15BuyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if(Integer.valueOf(coinText.getText().toString()) < 15){
                    Intent intent = new Intent(getActivity(), StoreBuyFailActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), StorePopupActivity.class);
                    String strItem_1 = item_1.getText().toString();

                    if(strItem_1.equals("달조각 떡뻥")){
                        intent.putExtra("COIN", "15");
                        intent.putExtra("THUNDER", "3");
                        intent.putExtra("ITEM", "달조각 떡뻥");
                        intent.putExtra("ITEM2", "을");
                        startActivity(intent);
                    }else if(strItem_1.equals("소행성 미트볼")){
                        intent.putExtra("COIN", "15");
                        intent.putExtra("THUNDER", "3");
                        intent.putExtra("ITEM", "소행성 미트볼");
                        intent.putExtra("ITEM2", "을");
                        startActivity(intent);
                    }else if(strItem_1.equals("행성 비빔밥")){
                        intent.putExtra("COIN", "15");
                        intent.putExtra("THUNDER", "3");
                        intent.putExtra("ITEM", "행성 비빔밥");
                        intent.putExtra("ITEM2", "을");
                        startActivity(intent);
                    }


                }

                coin15BuyButton.startAnimation(anim_buttonclick);

                if(sound == true){
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
                }



            }
        });

        //20코인 사용 버튼
        coin20BuyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if(Integer.valueOf(coinText.getText().toString()) < 20){
                    Intent intent = new Intent(getActivity(), StoreBuyFailActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), StorePopupActivity.class);
                    String strItem_2 = item_2.getText().toString();

                    if(strItem_2.equals("무지개 거품 목욕")){
                        intent.putExtra("COIN", "20");
                        intent.putExtra("THUNDER", "5");
                        intent.putExtra("ITEM", "무지개 거품 목욕");
                        intent.putExtra("ITEM2", "을");
                        startActivity(intent);
                    }else if(strItem_2.equals("UFO 쿠키")){
                        intent.putExtra("COIN", "20");
                        intent.putExtra("THUNDER", "5");
                        intent.putExtra("ITEM", "UFO 쿠키");
                        intent.putExtra("ITEM2", "를");
                        startActivity(intent);
                    }else if(strItem_2.equals("달토끼 인절미")){
                        intent.putExtra("COIN", "20");
                        intent.putExtra("THUNDER", "5");
                        intent.putExtra("ITEM", "달토끼 인절미");
                        intent.putExtra("ITEM2", "를");
                        startActivity(intent);
                    }

                }

                coin20BuyButton.startAnimation(anim_buttonclick);

                if(sound == true){
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
                }

                /*levelBarCount.startAnimation(anim_buttonclick);
                coinText.startAnimation(anim_buttonclick);
                coin15BuyBox.startAnimation(anim_buttonclick);*/

            }
        });

        //40코인 사용 버튼
        coin40BuyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if(Integer.valueOf(coinText.getText().toString()) < 40){
                    Intent intent = new Intent(getActivity(), StoreBuyFailActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getActivity(), StorePopupActivity.class);
                    String strItem_3 = item_3.getText().toString();

                    if(strItem_3.equals("별똥별 촉감 놀이")){
                        intent.putExtra("COIN", "40");
                        intent.putExtra("THUNDER", "10");
                        intent.putExtra("ITEM", "별똥별 촉감 놀이");
                        intent.putExtra("ITEM2", "를");
                        startActivity(intent);
                    }else if(strItem_3.equals("블랙홀 미끄럼틀")){
                        intent.putExtra("COIN", "40");
                        intent.putExtra("THUNDER", "10");
                        intent.putExtra("ITEM", "블랙홀 미끄럼틀");
                        intent.putExtra("ITEM2", "을");
                        startActivity(intent);
                    }else if(strItem_3.equals("태양열 온천 목욕")){
                        intent.putExtra("COIN", "40");
                        intent.putExtra("THUNDER", "10");
                        intent.putExtra("ITEM", "태양열 온천 목욕");
                        intent.putExtra("ITEM2", "을");
                        startActivity(intent);
                    }
                }

                coin40BuyButton.startAnimation(anim_buttonclick);

                if(sound == true){
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
                }

                /*levelBarCount.startAnimation(anim_buttonclick);
                coinText.startAnimation(anim_buttonclick);
                coin15BuyBox.startAnimation(anim_buttonclick);*/

            }
        });

        // Inflate the layout for this fragment (기존에 있던 코드)
        return rootView;
    }

    // *

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


}