package com.example.univity;

import android.Manifest;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.google.ar.core.*;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class ArActivity extends AppCompatActivity implements SensorEventListener {

    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    private static final String TAG = ArActivity.class.getSimpleName();
    private ArFragment arFragment;
    private static final double MIN_OPENGL_VERSION = 3.0;
    private ModelRenderable treasureRenderable;
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    //firebase-----------------------------------/
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    /*-----------------------------------------*/

    //AR 객체 한개만 띄우기 위한 변수
    private int count = 1;

    private int tempCoin;

    FrameLayout ar_back; //

    // 시간측정
    long starttime = 0;
    long endtime = 0;
    int interval = 0;
    int hour=0, min=0, sec = 0; // 시분초로 계산한 값 저장
    String myRunTime="";


    // 걸음수 측정
    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView stepCountView;
    TextView stepTodayView; // 일별 누적 걸음 수
    int currentSteps = 0; // 현재 걸음 수
    int goalSteps;
    int todaySteps;


    LinearLayout ar_startui;
    ImageView arStartButton, walkStart;
    TextView arText1;
    TextView arText2;
    TextView coinText;
    ImageView arBlackBg;
    ImageView arBlackBg2;

    ImageView pauseButton;
    ImageView soundButton;
    ImageView finishButton;

    ImageView ar_coin_button;
    ImageView ar_coin_gem;
    TextView tvCointext;
    TextView tvCoin;


    ProgressBar progressBar1;


    boolean gemopen = false;

    boolean start = true;
    boolean sound = true;

    private String coin;

    //보물상자에서 나오는 랜덤 코인
    private int[] rnd = {30, 35, 40, 45, 50};


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // <--- AR --->
        if (!checkIsSupportDeviceOrFinish(this)){
            return;
        }

        setContentView(R.layout.activity_ar);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        ar_back = (FrameLayout) findViewById(R.id.ar_back);
        ar_back.setVisibility(View.INVISIBLE);


        Intent intent; // 얻은 코인, 걸은 시간, 현재 시간, 종료 시간, 걸음수 넘길 인텐트
        intent = new Intent(ArActivity.this, FinishWalkActivity.class);

        //firebase
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity")
                .child("UserAccount");


        ar_coin_button = (ImageView)findViewById(R.id.ar_coin_button);
        ar_coin_gem = (ImageView) findViewById(R.id.ar_coin_gem);
        tvCoin = (TextView)findViewById(R.id.tvCoin);
        tvCointext = (TextView) findViewById(R.id.tvCointext);
        coinText = (TextView)findViewById(R.id.coinText);
        walkStart = (ImageView)findViewById(R.id.walkStart);

        ar_coin_gem.setVisibility(View.INVISIBLE);
        ar_coin_button.setVisibility(View.INVISIBLE);
        tvCoin.setVisibility(View.INVISIBLE);
        tvCointext.setVisibility(View.INVISIBLE);


        // <--- 걸음수 측정 --->
        stepCountView = findViewById(R.id.stepCountView);
        stepTodayView = findViewById(R.id.stepTodayView);

        ar_startui = (LinearLayout) findViewById(R.id.ar_startui);
        arStartButton = (ImageView) findViewById(R.id.arStartButton);
        arText1 = (TextView) findViewById(R.id.arText1);
        arText2 = (TextView) findViewById(R.id.arText2);
        arBlackBg = (ImageView) findViewById(R.id.arBlackBg);
        arBlackBg2 = (ImageView) findViewById(R.id.arBlackBg2);

        arBlackBg.setVisibility(View.VISIBLE);
        arBlackBg2.setVisibility(View.INVISIBLE);

        pauseButton = (ImageView) findViewById(R.id.arPauseButton);
        soundButton = (ImageView) findViewById(R.id.arSoundButton);
        finishButton = (ImageView) findViewById(R.id.arFinishButton);


        // <--- chronometer --->
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");

        progressBar1 = (ProgressBar)findViewById(R.id.todayProgressBar);


        //애니메이션

        final Animation anim_scale_alpha = AnimationUtils.loadAnimation(this, R.anim.anim_scale_alpha);
        final Animation anim_alpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        final Animation anim_buttonclick = AnimationUtils.loadAnimation(this, R.anim.anim_buttonclick);
        final Animation anim_fadeout = AnimationUtils.loadAnimation(this, R.anim.anim_fadeout);


        //사용할 3D 객체를 렌더링 가능한 모델로 빌드하도록 설정하고 빌드시킴
        ModelRenderable.builder()
                .setSource(this, R.raw.gem2)
                //.setSource(this, Uri.parse("andy.sfb"))
                .build()
                .thenAccept(renderable -> treasureRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });


        //화면에 현재 사용자 보유 코인, 오늘 걸은 걸음수, 사용자의 목표 걸음수 출력
        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserAccount user = snapshot.getValue(UserAccount.class);

                coinText.setText(String.valueOf(user.getCoin()));
                stepTodayView.setText(String.valueOf(user.getTodaySteps()));

                goalSteps = user.getGoalSteps();
                todaySteps = user.getTodaySteps();
                progressBar1.setMax(user.getGoalSteps());
                progressBar1.setProgress(user.getTodaySteps());


                Log.d("progress",String.valueOf(user.getGoalSteps()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        // 활동 퍼미션 체크
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작 (정확도 낮음, 지연 최대 2초)는 개뿔이나..
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴 (정확도 높음, 지연 최대 10초)
        //
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        // 디바이스에 걸음 센서의 존재 여부 체크
        if (stepCountSensor == null) {
            Toast.makeText(this, "No Step Sensor", Toast.LENGTH_SHORT).show();
        }



        arStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arStartButton.startAnimation(anim_scale_alpha);
                arText1.startAnimation(anim_alpha);
                arText2.startAnimation(anim_alpha);
                arBlackBg.startAnimation(anim_alpha);

                //arStartButton.setVisibility(View.GONE);
                arText1.setVisibility(View.GONE);
                arText2.setVisibility(View.GONE);
                arBlackBg.setVisibility(View.INVISIBLE);

                arStartButton.setEnabled(false);
                //ar_back.setVisibility(View.VISIBLE);

                starttime = System.currentTimeMillis(); // 걸은시간 측정 (시작)

                Date date1 = new Date(starttime); //날짜

                Date date2 = new Date(starttime); //시간

                SimpleDateFormat mFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat mFormat2 = new SimpleDateFormat("HH:mm");

                String date = mFormat1.format(date1); //2022-01-01 형식으로 저장됨
                String strStratTime = mFormat2.format(date2); //14:40 형식으로 저장

                intent.putExtra("CDATE", date);
                intent.putExtra("STARTTIME", strStratTime);

                // 걷기를 시작합니다 (음성)
                if(sound == true){
                    Context c = view.getContext();

                    MediaPlayer m = MediaPlayer.create(c , R.raw.walk_start_voice);
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


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        walkStart.startAnimation(anim_fadeout);
                        walkStart.setVisibility(View.INVISIBLE); // 3초뒤 실행할 작업
                    }
                }, 3000); // 3000==3초

                //타이머 시작
                if(!running){
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }

                // 400 밀리 초간 멈추게 하고싶다면
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        pauseButton.setVisibility(View.VISIBLE);
                        pauseButton.startAnimation(anim_buttonclick);
                        walkStart.setVisibility(View.VISIBLE);
                    }
                }, 400);
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(start==true){


                    // 기록이 일시정지 되었습니다 (음성)
                    if(sound == true){
                        Context c = view.getContext();

                        MediaPlayer m = MediaPlayer.create(c , R.raw.walk_pause_voice);
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

                    start = false;
                    pauseButton.setImageResource(R.drawable.ar_restart);
                    finishButton.setVisibility(View.VISIBLE);

                    //티이머 일시정지
                    if(running){
                        chronometer.stop();
                        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                        running = false;
                    }

                }
                else{

                    // 기록을 계속 측정합니다 (음성)
                    if(sound == true){
                        Context c = view.getContext();

                        MediaPlayer m = MediaPlayer.create(c , R.raw.walk_restart_voice);
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

                    start = true;
                    pauseButton.setImageResource(R.drawable.ar_pause);
                    finishButton.setVisibility(View.GONE);

                    if(!running){
                        chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                        chronometer.start();
                        running = true;
                    }
                }
                pauseButton.startAnimation(anim_buttonclick);
            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sound==true){
                    sound = false;
                    soundButton.setImageResource(R.drawable.ar_soundoff);
                }
                else{
                    sound = true;
                    soundButton.setImageResource(R.drawable.ar_sound);
                }
                soundButton.startAnimation(anim_buttonclick);
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                endtime = System.currentTimeMillis(); // 걸은시간 측정 (끝)
                interval = Long.valueOf((endtime - starttime) / 1000).intValue();

                min = interval / 60;
                hour = min / 60;
                sec = interval % 60;
                min = min % 60;


                System.out.println("hour :" + hour);

                if(hour!=0){
                    myRunTime=(hour+"시간 "+min+"분 "+sec+"초");
                }
                else if (min!=0){
                    myRunTime=(min+"분 "+sec+"초");
                }
                else{
                    myRunTime=(sec+"초");
                }

                System.out.println("get JSONData Time :" + (endtime - starttime) / 1000);
                System.out.println("get JSONData Time2 :" + myRunTime);



                //걷기 종료시각 받아서 intent로 넘기기
                Date date = new Date(endtime); //날짜

                SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

                String strEndTime = mFormat.format(date); //14:40 형식으로 저장

                intent.putExtra("ENDTIME", strEndTime);



                // 걷기를 종료합니다 (음성)
                if(sound == true){
                    Context c = view.getContext();

                    MediaPlayer m = MediaPlayer.create(c , R.raw.walk_end_voice);
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

                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserAccount user = snapshot.getValue(UserAccount.class);

                        HashMap<String, Object> map = new HashMap<>();

                        map.put("todaySteps",Integer.valueOf(stepTodayView.getText().toString()));
                        map.put("coin",Integer.valueOf(coinText.getText().toString()));

                        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                intent.putExtra("STEP", String.valueOf(currentSteps)); // 걸음수 결과값 넘기기
                intent.putExtra("COIN", String.valueOf(tempCoin)); // 이번 걷기에서 얻은 코인 결과값 넘기기

                // DB 연동 코드로 변경 예정 (시간, 분, 초 값 넘김)
                intent.putExtra("TIME", myRunTime);


                startActivity(intent);
                finish();
            }
        });

    }




    // < --- ar --->
    @Override
    protected void onResume() {
        super.onResume();
        requestCameraPermission();
    }


    //Ar 버전 체크
    public static boolean checkIsSupportDeviceOrFinish(final Activity activity) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }

        String openGlVersionString = ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }

    //카메라 권한 요청
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
    }


    // <--- ui --->
    public void onStart() {
        super.onStart();
        if(stepCountSensor !=null) {
            // 센서 속도 설정
            // * 옵션
            // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
            // - SENSOR_DELAY_UI: 6,000 초 딜레이
            // - SENSOR_DELAY_GAME: 20,000 초 딜레이
            // - SENSOR_DELAY_FASTEST: 딜레이 없음
            //
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 걸음 센서 이벤트 발생시
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            if (start) {
                if (event.values[0] == 1.0f) {
                    // 센서 이벤트가 발생할때 마다 걸음수 증가
                    currentSteps++;
                    stepCountView.setText(String.valueOf(currentSteps) + "보");


                    //20보마다 1코인 획득 코드
                    if(currentSteps%20==0){

                        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                UserAccount user = snapshot.getValue(UserAccount.class);

                                int plusCoin = user.getCoin()+1;

                                HashMap<String, Object> map = new HashMap<>();

                                map.put("coin",plusCoin);

                                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }


                    //오늘 걸은 걸음수를
                    mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            UserAccount user = snapshot.getValue(UserAccount.class);

                            HashMap<String, Object> map = new HashMap<>();

                            map.put("todaySteps",user.getTodaySteps()+1);

                            mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    //100걸음 걸으면 보물상자 등장
                    if(currentSteps%10==0){
                        Log.d("TAG",String.valueOf(currentSteps));
                        count = 1;

                        // 손모양 보임
                        ar_back.setVisibility(View.VISIBLE);

                        // 보물상자 발견! (음성)
                        if(sound == true){
                            MediaPlayer m = MediaPlayer.create(this, R.raw.treas_find_voice);
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
                        appearTreasure();

                        builder = null;

                        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //버전 오레오 이상일 경우
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            manager.createNotificationChannel(
                                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));

                            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

                            //하위 버전일 경우
                        } else {
                            builder = new NotificationCompat.Builder(this);
                        }

                        //알림창 제목
                        builder.setContentTitle("보물상자 발견!");

                        //알림창 메시지
                        builder.setContentText("\uD83C\uDF81보물상자에서 코인이 팡!\uD83D\uDC8E\n" +
                                "랜덤으로 쏟아지는 코인을 얻으세요!");

                        //알림창 아이콘
                        builder.setSmallIcon(R.drawable.uni_small_img);

                        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
                        builder.setAutoCancel(true);

                        // 진동 알림
                        builder.setDefaults(Notification.DEFAULT_VIBRATE);

                        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        Notification notification = builder.build();

                        //알림창 실행
                        manager.notify(1, notification);

                    }

                    if (stepTodayView.getText().toString().equals(String.valueOf(goalSteps-1)))  {

                        mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("goalIsOk",true); //목표 달성
                                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                        // 목표를 달성했습니다! (음성)
                        if(sound == true){
                            MediaPlayer d = MediaPlayer.create(this , R.raw.achieve_goal_voice);
                            d.start();

                            d.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp)
                                {
                                    mp.stop();
                                    mp.release();
                                }
                            });
                        }

                    }

                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void appearTreasure(){
        //디스플레이의 한 점을 탭하면 그 위치에 3D 객체를 그려주도록 리스너 설치

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if(treasureRenderable == null){
                        return;
                    }

                    if(count != 0){
                        Anchor anchor = hitResult.createAnchor();
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        TransformableNode treasure = new TransformableNode(arFragment.getTransformationSystem());
                        treasure.setParent(anchorNode);
                        treasure.setRenderable(treasureRenderable);
                        treasure.select();
                        final Animation anim_floating = AnimationUtils.loadAnimation(this, R.anim.anim_floating2);

                        treasure.setOnTouchListener(new Node.OnTouchListener() {
                            @Override
                            public boolean onTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {

                                treasure.setRenderable(null);

                                pauseButton.setEnabled(false);
                                soundButton.setEnabled(false);

                                arBlackBg2.setVisibility(View.VISIBLE);

                                String getCoin = String.valueOf(getRandom(rnd));
                                tvCointext.setText(getCoin);

                                ar_coin_gem.setVisibility(View.VISIBLE);
                                ar_coin_button.setVisibility(View.VISIBLE);
                                //tvGetCoin.setVisibility(View.VISIBLE);
                                //tvCoin.setVisibility(View.VISIBLE);



                                ar_coin_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        if(!gemopen){
                                            ar_coin_gem.setImageResource(R.drawable.ar_coin_gem2);
                                            ar_coin_button.setImageResource(R.drawable.ar_coin_button2);
                                            tvCoin.setVisibility(View.VISIBLE);
                                            tvCointext.setVisibility(View.VISIBLE);
                                            tvCoin.startAnimation(anim_floating);
                                            tvCointext.startAnimation(anim_floating);
                                            gemopen = true;
                                        }
                                        else{

                                            mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    UserAccount user = snapshot.getValue(UserAccount.class);

                                                    coin = String.valueOf(user.getCoin()+Integer.valueOf(tvCointext.getText().toString()));

                                                    HashMap<String, Object> map = new HashMap<>();

                                                    tempCoin = tempCoin + Integer.valueOf(getCoin);

                                                    map.put("coin",Integer.valueOf(coin));

                                                    mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).updateChildren(map);

                                                    tvCointext.setText("");

                                                    // 싹 사라짐
                                                    ar_coin_gem.setVisibility(View.GONE);
                                                    ar_coin_button.setVisibility(View.GONE);
                                                    tvCointext.setVisibility(View.GONE);
                                                    tvCoin.setVisibility(View.GONE);
                                                    arBlackBg2.setVisibility(View.GONE);

                                                    // 첫 이미지로 돌리기
                                                    ar_coin_gem.setImageResource(R.drawable.ar_coin_gem1);
                                                    ar_coin_button.setImageResource(R.drawable.ar_coin_button1);

                                                    pauseButton.setEnabled(true);
                                                    soundButton.setEnabled(true);


                                                    gemopen = false;

                                                    // 손모양 안보임
                                                    ar_back.setVisibility(View.VISIBLE);

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });


                                        }

                                    }
                                });

                                return false;
                            }
                        });
                        count--;
                    }
                }
        );
    }

    public static int getRandom(int[] array){
        int rnd = new Random().nextInt(array.length);

        return array[rnd];
    }

}