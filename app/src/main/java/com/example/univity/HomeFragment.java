package com.example.univity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    TextView coinText, stepText, levelText, nicknameText;
    public static TextView levelBarCount;

    ImageView uni, uni2, levelImg;
    AnimationDrawable unijump;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,@Nullable Bundle savedInstanceState){

        View rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        final ProgressBar levelProgressBar = (ProgressBar) rootView.findViewById(R.id.levelProgressBar);
        final ProgressBar todayProgressBar = (ProgressBar) rootView.findViewById(R.id.todayProgressBar);


        //유니 애니메이션
        uni = (ImageView) rootView.findViewById(R.id.univity);
        uni2 = (ImageView)rootView.findViewById(R.id.univity2);

        levelImg = (ImageView) rootView.findViewById(R.id.levelImg);

/*        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(uni);
        Glide.with(getContext()).load(R.drawable.aaaaaaaaa).into(gifImage);*/

        coinText = (TextView) rootView.findViewById(R.id.coinText);
        stepText = (TextView) rootView.findViewById(R.id.stepText);
        levelText = (TextView) rootView.findViewById(R.id.levelText);
        nicknameText = (TextView) rootView.findViewById(R.id.nicknameText);
        levelBarCount = (TextView) rootView.findViewById(R.id.levelBarCount);

        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");

        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserAccount user = snapshot.getValue(UserAccount.class);

                coinText.setText(String.valueOf(user.getCoin()));
                stepText.setText(String.valueOf(user.getTodaySteps())+"보");
                levelText.setText("Lv." + String.valueOf(user.getLevel()));
                nicknameText.setText(String.valueOf(user.getNickname()));
                levelProgressBar.setProgress(user.getThunder());
                todayProgressBar.setMax(user.getGoalSteps());
                todayProgressBar.setProgress(user.getTodaySteps());
                levelBarCount.setText(String.valueOf(user.getThunder()));

                //레벨 별로 유니 이미지 바꾸기
                if(user.getLevel() == 1){
                    uni2.setVisibility(View.INVISIBLE);
                    uni.setImageResource(R.drawable.uni_level1);
                    levelImg.setImageResource(R.drawable.uni_level_img1);
                    uni.setVisibility(View.VISIBLE);
                    levelImg.setVisibility(View.VISIBLE);
                    unijump = (AnimationDrawable)uni.getDrawable();
                    unijump.setOneShot(true);
                    unijump.start();
                }else if(user.getLevel() == 2){
                    uni2.setVisibility(View.INVISIBLE);
                    uni.setImageResource(R.drawable.uni_level2);
                    levelImg.setImageResource(R.drawable.uni_level_img2);
                    uni.setVisibility(View.VISIBLE);
                    levelImg.setVisibility(View.VISIBLE);
                    unijump = (AnimationDrawable)uni.getDrawable();
                    unijump.setOneShot(true);
                    unijump.start();
                }else if(user.getLevel() == 3){
                    uni.setVisibility(View.INVISIBLE);
                    uni2.setImageResource(R.drawable.uni_level3);
                    levelImg.setImageResource(R.drawable.uni_level_img3);
                    uni2.setVisibility(View.VISIBLE);
                    levelImg.setVisibility(View.VISIBLE);
                    unijump = (AnimationDrawable)uni2.getDrawable();
                    unijump.setOneShot(true);
                    unijump.start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unijump.isRunning()){
                    unijump.stop();
                }

                unijump.start();

            }
        });
        uni2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(unijump.isRunning()){
                    unijump.stop();
                }

                unijump.start();

            }
        });


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

    public HomeFragment() {
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