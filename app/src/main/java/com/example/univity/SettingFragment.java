package com.example.univity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    //firebase 1 -----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    // 각각의 하위 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static SettingFragment newInstance() {
        return new SettingFragment();
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting, container, false);

        View view = inflater.inflate(R.layout.fragment_setting_support, null); // Fragment로 불러올 xml파일을 view로 가져옵니다.
        TextView supportButton = (TextView) rootView.findViewById(R.id.supportButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        TextView storyButton = (TextView) rootView.findViewById(R.id.storyButton);
        TextView tutorialButton = (TextView) rootView.findViewById(R.id.tutorialButton);
        TextView logoutButton = (TextView) rootView.findViewById(R.id.logoutButton);
        TextView profileEdit = (TextView) rootView.findViewById(R.id.profileEdit);
        TextView nicknameText = (TextView) rootView.findViewById(R.id.nicknameText);
        TextView levelText = (TextView) rootView.findViewById(R.id.levelText);

        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");
        /*-----------------------------------------*/

        // 글자 변경!!!!!!!!!!!!!!!!! 왜 얘만 안되는거임?
        //<--firebase-->
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //firebase 3 -----------------------------------/
                UserAccount user = snapshot.getValue(UserAccount.class);
                /*-----------------------------------------*/

                nicknameText.setText(String.valueOf(user.getNickname()));
                levelText.setText(String.valueOf(user.getLevel())); // 레벨 반영
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "profileEdit!!");
                ((MainActivity) getActivity()).replaceFragment(SettingProfileFragment.newInstance());
            }
        });

        storyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "storyButton!!");
                Intent intent = new Intent(getActivity(), StoryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); // 화면 부드럽게 전환하고 싶어서 추가함!! - 근데 큰 차이가 없네?
            }
        });

        tutorialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "tutorialButton!!");
                Intent intent = new Intent(getActivity(), Tutorial1Activity.class);
                startActivity(intent);
            }
        });

        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "고객지원클릭!!");
                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity) getActivity()).replaceFragment(SettingSupportFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "logoutButton!!");
                Intent intent = new Intent(getActivity(), LogoutPopupActivity.class);
                startActivity(intent);
            }
        });



        return rootView;
    }
}