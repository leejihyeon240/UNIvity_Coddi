package com.example.univity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingProfileFragment extends Fragment {

    // firebase 1
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    boolean namefirstclick = true;
    boolean nicknamefirstclick = true;
    boolean emailfirstclick = true;
    boolean goalfirstclick = true;

    // 각각의 하위 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static SettingProfileFragment newInstance() {
        return new SettingProfileFragment();
    }

    /**
     * Hiding keyboard after every button press
     **/
    //fragment 키보드 내리기
    private void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            // 프래그먼트기 때문에 getActivity() 사용
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void showKeyboard(EditText myEditText) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingProfileFragment newInstance(String param1, String param2) {
        SettingProfileFragment fragment = new SettingProfileFragment();
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
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_setting_profile, container, false);

        View view = inflater.inflate(R.layout.fragment_setting_support, null); // Fragment로 불러올 xml파일을 view로 가져옵니다.
        ImageView backButton = (ImageView) rootView.findViewById(R.id.backButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.


        EditText editName = (EditText) rootView.findViewById(R.id.editFindName);
        EditText editNickname = (EditText) rootView.findViewById(R.id.editNickname);
        EditText editEmail = (EditText) rootView.findViewById(R.id.editEmail);
        EditText editGoal = (EditText) rootView.findViewById(R.id.editGoal);

//        editName.setKeyListener(editName.getKeyListener()); // 수정불가1
        Log.d("key0-1", String.valueOf(editName.getKeyListener()));
        Object editTextkey = editName.getKeyListener(); // 수정 가능한 keylistener 저장해두기
        editName.setKeyListener(null); // 수정불가2
        editNickname.setKeyListener(null); // 수정불가2
        editEmail.setKeyListener(null); // 수정불가2
        editGoal.setKeyListener(null); // 수정불가2
        Log.d("key0-2", String.valueOf(editName.getKeyListener()));

        TextView nameChg = (TextView) rootView.findViewById(R.id.nameChg);
        TextView nicknameChg = (TextView) rootView.findViewById(R.id.nicknameChg);
//        TextView emailChg = (TextView) rootView.findViewById(R.id.emailChg);
        TextView goalChg = (TextView) rootView.findViewById(R.id.goalChg);

        TextView nicknameText = (TextView) rootView.findViewById(R.id.nicknameText2);

        // firebase2
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");


        // 글자 변경 실시간
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserAccount user = snapshot.getValue(UserAccount.class);

                nicknameText.setText(user.getNickname() + "님의 프로필 정보입니다.");
                editName.setText(user.getName());
                editNickname.setText(user.getNickname());
                editEmail.setText(user.getId());
                editGoal.setText(String.valueOf(user.getGoalSteps()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "뒤로!!");
                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity) getActivity()).replaceFragment(SettingFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });
        editName.setClickable(false);

//        /** name **/
//        editName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("LOG", "editName!!");
//            }
//        });
        nameChg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "nameChg!!");

                if (namefirstclick) {
                    //변경을 누르면 수정이 가능해지고,변경 글자 대신 완료 글자가 뜬다.
                    editName.setKeyListener((KeyListener) editTextkey); // 수정가능
                    Log.d("key1", String.valueOf(editName.getKeyListener()));

                    editName.requestFocus();
                    InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(editName, 0);

                    Log.d("key111", String.valueOf(editName.requestFocus()));


                    nameChg.setText("완료");
                    namefirstclick = false;
                } else {
                    // 사용자가 수정을 완료하고 완료 글자가 누르면 수정이 완료되는 플로우
                    editName.setKeyListener(null); // 수정불가
                    Log.d("key2", String.valueOf(editName.getKeyListener()));
                    hideKeyboard();
                    nameChg.setText("변경");
                    namefirstclick = true;

                    //<--firebase-->
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", String.valueOf(editName.getText()));
                    mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                    Log.d("LOG", String.valueOf(editName.getText()));
                }


            }
        });

        /** nickname **/
//        editNickname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("LOG", "editNickname!!");
////                editNickname.setCursorVisible(true);
//            }
//        });
        nicknameChg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "nicknameChg!!");

                if (nicknamefirstclick) {
                    //변경을 누르면 수정이 가능해지고,변경 글자 대신 완료 글자가 뜬다.
                    editNickname.setKeyListener((KeyListener) editTextkey); // 수정가능
                    Log.d("key1", String.valueOf(editNickname.getKeyListener()));

                    editNickname.requestFocus();
                    InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    keyboard.showSoftInput(editNickname, 0);

                    Log.d("key111", String.valueOf(editNickname.requestFocus()));


                    nicknameChg.setText("완료");
                    nicknamefirstclick = false;
                } else {
                    // 사용자가 수정을 완료하고 완료 글자가 누르면 수정이 완료되는 플로우
                    editNickname.setKeyListener(null); // 수정불가
                    Log.d("key2", String.valueOf(editNickname.getKeyListener()));
                    hideKeyboard();
                    nicknameChg.setText("변경");
                    nicknamefirstclick = true;

                    //<--firebase-->
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("nickname", String.valueOf(editNickname.getText()));
                    mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                    Log.d("LOG", String.valueOf(editNickname.getText()));
                }
            }
        });

//        /** email **/
////        editEmail.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Log.d("LOG", "editEmail!!");
////                editEmail.setCursorVisible(true);
////            }
////        });
//        emailChg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("LOG", "emailChg!!");
//
//                if (emailfirstclick) {
//                    //변경을 누르면 수정이 가능해지고,변경 글자 대신 완료 글자가 뜬다.
//                    editEmail.setKeyListener((KeyListener) editTextkey); // 수정가능
//                    Log.d("key1", String.valueOf(editEmail.getKeyListener()));
//
//                    editEmail.requestFocus();
//                    InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    keyboard.showSoftInput(editEmail, 0);
//
//                    Log.d("key111", String.valueOf(editEmail.requestFocus()));
//
//
//                    emailChg.setText("완료");
//                    emailfirstclick = false;
//                } else {
//                    // 사용자가 수정을 완료하고 완료 글자가 누르면 수정이 완료되는 플로우
//                    editEmail.setKeyListener(null); // 수정불가
//                    Log.d("key2", String.valueOf(editEmail.getKeyListener()));
//                    hideKeyboard();
//                    emailChg.setText("변경");
//                    emailfirstclick = true;
//
//                    //<--firebase-->
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("email", String.valueOf(editEmail.getText()));
//                    mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
//                    Log.d("LOG", String.valueOf(editEmail.getText()));
//                }
//            }
//        });

        /** goal setting **/
//        editGoal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("LOG", "editGoal!!");
////                editGoal.setCursorVisible(false);
//            }
//        });
        goalChg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "emailChg!!");

                Intent intent = new Intent(getActivity(), GoalSettingPopupActivity.class);
                startActivity(intent);

            }
        });

        return rootView;
    }


}