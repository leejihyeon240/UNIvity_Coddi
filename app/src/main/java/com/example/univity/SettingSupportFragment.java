package com.example.univity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingSupportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingSupportFragment extends Fragment {

    // 각각의 하위 Fragment마다 Instance를 반환해 줄 메소드를 생성합니다.
    public static SettingSupportFragment newInstance() {
        return new SettingSupportFragment();
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingSupportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingSupportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingSupportFragment newInstance(String param1, String param2) {
        SettingSupportFragment fragment = new SettingSupportFragment();
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
        View rootView = (ViewGroup)inflater.inflate(R.layout.fragment_setting_support, container, false);

        View view = inflater.inflate(R.layout.fragment_setting_support, null); // Fragment로 불러올 xml파일을 view로 가져옵니다.
        ImageView backButton = (ImageView) rootView.findViewById(R.id.backButton); // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG", "뒤로!!");
                // getActivity()로 MainActivity의 replaceFragment를 불러옵니다.
                ((MainActivity) getActivity()).replaceFragment(SettingFragment.newInstance());    // 새로 불러올 Fragment의 Instance를 Main으로 전달
            }
        });

        return rootView;
    }
}