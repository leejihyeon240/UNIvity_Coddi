package com.example.univity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.lang.reflect.Member;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static android.content.ContentValues.TAG;
import static com.google.firebase.database.core.RepoManager.clear;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment {

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    TextView headerText;
    TextView dateText;
    RecyclerView recycler;
    boolean sound = true;


    // db 관련 애들..
    ArrayList<CalendarDay> walkdateList = new ArrayList<>();
    Map<String, Object> calendardata;
    // 리사이클러뷰에 표시할 데이터 리스트 생성.
    ArrayList<String> startlist = new ArrayList<>();
    ArrayList<String> endlist = new ArrayList<>();
    ArrayList<String> steplist = new ArrayList<>();

    // 선택한 날짜 년/월/요일
    String selyear;
    String selmonth;
    String selday;

    String seldate;
    String seldayofweek = "n/a";

    ArrayList<String> array = new ArrayList<String>();

    // workout box 띄워져 있는지 유무
    boolean boxvisible = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        MainActivity activity = (MainActivity) getActivity();
        final Animation anim_up = AnimationUtils.loadAnimation(activity, R.anim.anim_up);
        final Animation anim_down = AnimationUtils.loadAnimation(activity, R.anim.anim_down);

        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar, container, false);
        headerText = (TextView) rootView.findViewById(R.id.HeaderText);
        dateText = (TextView) rootView.findViewById(R.id.datetext);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycler);
        recycler.setVisibility(View.GONE);
        boxvisible = false;


        RecyclerView recyclerView = rootView.findViewById(R.id.recycler);

        MaterialCalendarView materialCalendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        activity.onCalendarCustom(materialCalendarView);

        // 선택 초기값 오늘로 설정
        materialCalendarView.setSelectedDate(CalendarDay.today());

        //firebase 2 -----------------------------------/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");
        /*-----------------------------------------*/

        // 캘린더데베(1) 활동기록 있는 날짜 리스트로 빼오기!!
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).child("calendar")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        // calendar에 저장된 모든 데이터
                        calendardata = (HashMap<String, Object>) snapshot.getValue();
                        Log.d("HEY0-calendardata(1)", String.valueOf(calendardata));

                        walkdateList.clear(); // list 초기화
                        // 키값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        Set<String> keys = calendardata.keySet();
                        for (String key : keys) {
                            // 활동날짜 리스트에 추가
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date date22 = formatter.parse(key);
//                                date22.setMonth(date22.getMonth()+1);
                                walkdateList.add(CalendarDay.from(date22));
                                Log.d("HEY0-walkdateList+222", String.valueOf(walkdateList));
                            } catch (Exception e) {

                            }

                        }

                        // 캘린더 점 표시
                        Log.d("HEY", String.valueOf(walkdateList));
                        for (CalendarDay d : walkdateList) {
                            materialCalendarView.addDecorator(new CalEventDecorator(Color.rgb(74, 64, 142), Collections.singleton(d)));
                            Log.d("HEY22222", "dd");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // 캘린더 헤더 형식 지정
        final Calendar date = Calendar.getInstance();
        materialCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                SimpleDateFormat calendar_view_format = new SimpleDateFormat("yyyy.MM"); //header
                SimpleDateFormat calendar_date_format = new SimpleDateFormat("yyyy.MM.dd"); // box title text

                String monthAndYear = calendar_view_format.format(day.getCalendar().getTime());
                headerText.setText(monthAndYear); // 헤더 텍스트 직접 지정 2022.03
//                headerText .setText(getCurrentlySelectedDate().toString()); // 헤더 텍스트 직접 지정 2022.03
                return monthAndYear;
            }
        });


        // 캘린더 헤더 없앰
        materialCalendarView.setTopbarVisible(false);

        // 캘린더 오늘 보라 표시
        materialCalendarView.addDecorators(new CalTodayDecorator(activity));

        // 선택한 곳에 원형 이미지
        materialCalendarView.addDecorator(new CalSelectedDecorator(activity));
//        materialCalendarView.addDecorator(new MySelectionTextDecorator(Color.rgb(74,64,142), activity));


        // 날짜 클릭했을 때!!!
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

//                                                              materialCalendarView.addDecorator(new EventDecorator(Color.rgb(74,64,142), Collections.singleton(date)));


                Log.d(TAG, date.toString());
                Log.d(TAG, CalendarDay.today().toString());
                Log.d(TAG, CalendarDay.from(2022, 2, 19).toString());

                selyear = String.valueOf(date.getYear());

                selmonth = String.valueOf(date.getMonth() + 1);
                if(selmonth.length()==1)
                    selmonth = "0" + selmonth;

                selday = String.valueOf(date.getDay());
                if(selday.length()==1)
                    selday = "0" + selday;

//                switch (selday) {
//                    case "1":
//                    case "2":
//                    case "3":
//                    case "4":
//                    case "5":
//                    case "6":
//                    case "7":
//                    case "8":
//                    case "9":
//                        selday = "0" + selday;
//                }

                seldate = String.valueOf(date.getDate());
                String[] array = seldate.split(" ");
                Log.d(TAG, array[0]);

                switch (array[0]) {
                    case "Mon":
                        seldayofweek = "월";
                        break;
                    case "Tue":
                        seldayofweek = "화";
                        break;
                    case "Wed":
                        seldayofweek = "수";
                        break;
                    case "Thu":
                        seldayofweek = "목";
                        break;
                    case "Fri":
                        seldayofweek = "금";
                        break;
                    case "Sat":
                        seldayofweek = "토";
                        break;
                    case "Sun":
                        seldayofweek = "일";
                        break;

                }

                Log.d(TAG, seldayofweek);

                if(sound == true){
                    Context c = widget.getContext();

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



                // 캘린더 선택한 날짜값 텍스트 표시 (왜인지 month값이 1씩 작게 받아와짐)
                if (date.equals(CalendarDay.today())) {
                    dateText.setText("Today");
                } else {
                    dateText.setText(selday + ". " + seldayofweek);
                    Log.d("selday", String.valueOf(selday));
                    Log.d("seldayofweek", String.valueOf(seldayofweek));
                }






                // 캘린더데베(2) 해당 날짜의 활동기록 출력!


                // list 초기화
                startlist.clear();
                endlist.clear();
                steplist.clear();

                Log.d("HEY1-calendardata(2)", String.valueOf(calendardata));

                // 해당 날짜에 기록된 모든 데이터
                Map dataset = (Map) calendardata.get(selyear+"-"+selmonth+"-"+selday);
                Log.d("HEY1-dataset(2)", String.valueOf(dataset));

                if(dataset != null && !dataset.isEmpty()){
                    // 각각의 활동기록 하나씩 반복구문 돌아돌아빙글뱅글어지러워~
                    for (int i = 0; i < dataset.values().toArray().length; i++) {
                        // 해당 날짜에 기록된 첫 번째 데이터
                        Object data1 = dataset.values().toArray()[i];
                        Map<String, Object> data2 = (HashMap<String, Object>) data1; //재가공(형변환)
                        Log.d("HEY2-data", String.valueOf(data2));

                        // 요소 접근
                        String startTime = (String) data2.get("startTime");
                        startlist.add(startTime);
                        Log.d("HEY3-startTime", startTime);

                        String step = (String) data2.get("step");
                        steplist.add(step);
                        Log.d("HEY3-step", step);

                        String endTime = (String) data2.get("endTime");
                        endlist.add(endTime);
                        Log.d("HEY3-endTime", endTime);

                    }
                }

                // 리사이클러뷰에 LinearLayoutManager 객체 지정.
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
                SimpleTextAdapter adapter = new SimpleTextAdapter(startlist,endlist,steplist);
                recyclerView.setAdapter(adapter);

                // 활동한 날인 경우 활동기록 표시
                for (CalendarDay d : walkdateList) {
                    if (date.equals(d)) {
                        recycler.setVisibility(View.VISIBLE); // 박스 보이게 하고
                        recycler.startAnimation(anim_up); // 애니메이션 재생
                        boxvisible = true; // 화면에 박스 이제 있다고 변수 바꿔줌
                        Log.d("boxvisible", String.valueOf(boxvisible));
                    } else { // 활동기록 없는 날을 선택했다면!!
//                        if (boxvisible) { // 박스가 보인 상태라면
//                            recycler.startAnimation(anim_down); // 애니메이션 재생
//                            boxvisible = false; // 화면에 박스 이제 없다고 변수 바꿔줌
//                            Log.d("boxvisible", String.valueOf(boxvisible));
//                        }
                    }
                }


            }
        });

        //캘린더 요일 표시
        materialCalendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));


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

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


}