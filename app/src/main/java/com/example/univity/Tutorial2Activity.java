package com.example.univity;

import android.Manifest;
import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class Tutorial2Activity extends AppCompatActivity {

    NotificationManager manager;
    NotificationCompat.Builder builder;
    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    private static final String TAG = ArActivity.class.getSimpleName();
    private ArFragment arFragment;
    private static final double MIN_OPENGL_VERSION = 3.0;
    private ModelRenderable treasureRenderable;

    //AR 객체 한개만 띄우기 위한 변수
    private int count = 1;

    FrameLayout ar_back; //

    // 걸음수 측정
    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView stepCountView;
    TextView stepTodayView; // 일별 누적 걸음 수
    int currentSteps = 0; // 현재 걸음 수
    int goalSteps;

    LinearLayout ar_startui;
    ImageView arStartButton;
    TextView arText1;
    TextView arText2;
    TextView coinText;
    ImageView arBlackBg;
    ImageView arBlackBg2;

    ImageView pauseButton;

    ImageView ar_coin_button;
    ImageView ar_coin_gem;
    TextView tvCointext;
    TextView tvCoin;

    boolean gemopen = false;

    boolean start = true;

    // tutorial 정의 1
    ConstraintLayout tutorial2_1_set;
    ImageView tutorial2_1_button;
    ConstraintLayout tutorial2_2_set;
    ConstraintLayout tutorial2_3_set;
    ConstraintLayout tutorial2_4_set;
    ImageView tutorial2_4_button;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial2);

        // <--- AR --->
        if (!checkIsSupportDeviceOrFinish(this)) {
            return;
        }

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.tutorial2_ux_fragment);
        ar_back = (FrameLayout) findViewById(R.id.tutorial2_ar_back);
        ar_back.setVisibility(View.INVISIBLE);


        ar_coin_button = (ImageView) findViewById(R.id.tutorial2_ar_coin_button);
        ar_coin_gem = (ImageView) findViewById(R.id.tutorial2_ar_coin_gem);
        tvCoin = (TextView) findViewById(R.id.tutorial2_tvCoin);
        tvCointext = (TextView) findViewById(R.id.tutorial2_tvCointext);
        coinText = (TextView) findViewById(R.id.tutorial2_coinText);
        ar_coin_gem.setVisibility(View.INVISIBLE);
        ar_coin_button.setVisibility(View.INVISIBLE);
        tvCoin.setVisibility(View.INVISIBLE);
        tvCointext.setVisibility(View.INVISIBLE);

        ar_startui = (LinearLayout) findViewById(R.id.tutorial2_ar_startui);
        arStartButton = (ImageView) findViewById(R.id.tutorial2_arStartButton);
        arText1 = (TextView) findViewById(R.id.tutorial2_arText1);
        arText2 = (TextView) findViewById(R.id.tutorial2_arText2);
        arBlackBg = (ImageView) findViewById(R.id.tutorial2_arBlackBg);
        arBlackBg2 = (ImageView) findViewById(R.id.tutorial2_arBlackBg2);

        arBlackBg.setVisibility(View.VISIBLE);
        arBlackBg2.setVisibility(View.INVISIBLE);

        pauseButton = (ImageView) findViewById(R.id.tutorial2_arPauseButton);


        // tutorial 정의 2
        tutorial2_1_set = (ConstraintLayout) findViewById(R.id.tutorial2_1_set);
        tutorial2_1_button = (ImageView) findViewById(R.id.tutorial2_1_button);
        tutorial2_2_set = (ConstraintLayout) findViewById(R.id.tutorial2_2_set);
        tutorial2_3_set = (ConstraintLayout) findViewById(R.id.tutorial2_3_set);
        tutorial2_4_set = (ConstraintLayout) findViewById(R.id.tutorial2_4_set);
        tutorial2_4_button = (ImageView) findViewById(R.id.tutorial2_4_button);


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


                // 걷기를 시작합니다 (음성)
                Context c = view.getContext();
                MediaPlayer m = MediaPlayer.create(c, R.raw.walk_start_voice);
                m.start();

                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                    }
                });




                // 400 밀리 초간 멈추게 하고싶다면
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        pauseButton.setVisibility(View.VISIBLE);
                        pauseButton.startAnimation(anim_buttonclick);
                    }
                }, 400);

                // 400 밀리 초간 멈추게 하고싶다면
                handler.postDelayed(new Runnable() {
                    public void run() {
                        tutorial2_1_set.setVisibility(View.VISIBLE);
                    }
                }, 400);


            }
        });


        // tutorial 2-1 : AR걷기 모드를 통해서 걸음수를 채우고 코인을 얻을 수 있어!
        tutorial2_1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // sound
                Context c = arg0.getContext();
                MediaPlayer m = MediaPlayer.create(c, R.raw.pop);
                m.start();
                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                    }
                });

                // tutorial2_2 : 보물상자가 발견되었대! 화면의 바닥을 터치해볼까?
                tutorial2_1_set.setVisibility(View.GONE);
                tutorial2_2_set.setVisibility(View.VISIBLE);
                count = 1; // 보물상자 클릭 가능
                ar_back.setVisibility(View.VISIBLE);// 손모양 보임

                // 보물상자 발견! (음성)

                MediaPlayer m2 = MediaPlayer.create(Tutorial2Activity.this, R.raw.treas_find_voice);
                m2.start();

                m2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                    }
                });

                appearTreasure();

                builder = null;

                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); //버전 오레오 이상일 경우
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    manager.createNotificationChannel(
                            new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));

                    builder = new NotificationCompat.Builder(Tutorial2Activity.this, CHANNEL_ID);

                    //하위 버전일 경우
                } else {
                    builder = new NotificationCompat.Builder(Tutorial2Activity.this);
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

    public void appearTreasure() {
        //디스플레이의 한 점을 탭하면 그 위치에 3D 객체를 그려주도록 리스너 설치

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (treasureRenderable == null) {
                        return;
                    }

                    if (count != 0) {
                        Anchor anchor = hitResult.createAnchor();
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        TransformableNode treasure = new TransformableNode(arFragment.getTransformationSystem());
                        treasure.setParent(anchorNode);
                        treasure.setRenderable(treasureRenderable);
                        treasure.select();

                        // sound
                        Context c = Tutorial2Activity.this;
                        MediaPlayer m = MediaPlayer.create(c, R.raw.pop);
                        m.start();
                        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.stop();
                                mp.release();
                            }
                        });

                        // tutorial 2-3 : 이제 보물상자를 클릭해봐
                        tutorial2_2_set.setVisibility(View.GONE);
                        tutorial2_3_set.setVisibility(View.VISIBLE);


                        final Animation anim_floating = AnimationUtils.loadAnimation(this, R.anim.anim_floating2);

                        treasure.setOnTouchListener(new Node.OnTouchListener() {
                            @Override
                            public boolean onTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {

                                // sound
                                Context c = Tutorial2Activity.this;
                                MediaPlayer m = MediaPlayer.create(c, R.raw.pop);
                                m.start();
                                m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        mp.stop();
                                        mp.release();
                                    }
                                });

                                treasure.setRenderable(null);

                                arBlackBg2.setVisibility(View.VISIBLE);



                                ar_coin_gem.setVisibility(View.VISIBLE);
                                ar_coin_button.setVisibility(View.VISIBLE);
                                //tvGetCoin.setVisibility(View.VISIBLE);
                                //tvCoin.setVisibility(View.VISIBLE);


                                tutorial2_3_set.setVisibility(View.GONE);


                                ar_coin_button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        if (!gemopen) {
                                            ar_coin_gem.setImageResource(R.drawable.ar_coin_gem2);
                                            ar_coin_button.setImageResource(R.drawable.ar_coin_button2);
                                            tvCoin.setVisibility(View.VISIBLE);
                                            tvCointext.setVisibility(View.VISIBLE);
                                            tvCoin.startAnimation(anim_floating);
                                            tvCointext.startAnimation(anim_floating);
                                            gemopen = true;
                                        } else {


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

                                            // 코인텍스트 변경
                                            coinText.setText("35");

                                            gemopen = false;

                                            // 손모양 안보임
                                            ar_back.setVisibility(View.INVISIBLE);

//                                            // sound
//                                            Context c = Tutorial2Activity.this;
//                                            MediaPlayer m = MediaPlayer.create(c, R.raw.pop);
//                                            m.start();
//                                            m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                                                @Override
//                                                public void onCompletion(MediaPlayer mp) {
//                                                    mp.stop();
//                                                    mp.release();
//                                                }
//                                            });

                                            // tutorial 2-4 : 보물상자는 1000보 걸으면 얻을 수 있음
                                            tutorial2_3_set.setVisibility(View.GONE);
                                            tutorial2_4_set.setVisibility(View.VISIBLE);


                                            tutorial2_4_button.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View arg0) {
                                                    // TODO Auto-generated method stub

                                                    // sound
                                                    Context c = arg0.getContext();
                                                    MediaPlayer m = MediaPlayer.create(c, R.raw.pop);
                                                    m.start();
                                                    m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                        @Override
                                                        public void onCompletion(MediaPlayer mp) {
                                                            mp.stop();
                                                            mp.release();
                                                        }
                                                    });


                                                    Intent intent = new Intent(Tutorial2Activity.this, Tutorial3Activity.class);
                                                    startActivity(intent);
                                                    finish();
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

}