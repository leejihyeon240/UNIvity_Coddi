package com.example.univity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    ShopFragment shopFragment;
    CalendarFragment calendarFragment;
    SettingFragment settingFragment;

    private MaterialCalendarView calendarView;

    public void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

//        fragmentTransaction.replace(R.id.container, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView bigARButton = (ImageView)findViewById(R.id.bigARButton);

        bigARButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ArActivity.class);
                startActivity(intent);
            }
        });

        // 디스플레이 사이즈 가져오기
//        Display display = getWindowManager().getDefaultDisplay();  // in Activity
//        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
//        Point size = new Point();
//        display.getRealSize(size); // or getSize(size)
//        int width = size.x;
//        int height = size.y;

        homeFragment = new HomeFragment();
        shopFragment = new ShopFragment();
        calendarFragment = new CalendarFragment();
        settingFragment = new SettingFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        BottomNavigationView bottom_menu = findViewById(R.id.bottomMenu);
        bottom_menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeButton:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.shopButton:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, shopFragment).commit();
                        Log.d("LOG", "상점화면");
                        return true;
                    case R.id.calendarButton:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarFragment).commit();
                        Log.d("LOG", "캘린더화면");
                        return true;
                    case R.id.setButton:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingFragment).commit();
                        Log.d("LOG", "세팅화면");
                        return true;
                    case R.id.arButton:
                        Intent intent = new Intent(getApplicationContext(), ArActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });



    }

//    public void onLevelPopup(){
//        Intent intent = new Intent(MainActivity.this, LevelPopupActivity.class);
//        // 인텐트 대해 startActivityForResult()수행.
//        startActivityForResult(intent, 1);
//        Log.d("LOG", "팝업");
//    }

    // 커스텀캘린더 데코레이터 함수 정의
    public void onCalendarCustom(MaterialCalendarView m){
        // 글꼴 지정
//        m.setHeaderTextAppearance(R.style.CalendarWidgetHeader);

        // 오늘 날짜 보라색 표시
//        m.addDecorator(new OneDayDecorator());

        //년, 월, 일 형식 지정
//        //m.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
//        m.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));

        // 년/월 보이는 방식 커스텀
        final Calendar date  = Calendar.getInstance();
        m.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                SimpleDateFormat calendar_view_format = new SimpleDateFormat("yyyy M"+"월");
                String monthAndYear = calendar_view_format.format(day.getCalendar().getTime());
                return monthAndYear;
            }
        });

        // 선택한 곳에 보라 원형
        //m.addDecorator(new MySelectorDecorator(this));

        // event dots 표시 (임시로 오늘 날짜에)
        //m.addDecorator(new EventDecorator(Color.RED, Collections.singleton(CalendarDay.today())));

        // 달력 변화 모션 제거
        //

    }
}