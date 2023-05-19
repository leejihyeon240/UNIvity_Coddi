package com.example.univity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class LogoutPopupActivity extends Activity {

    boolean sound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //타이틀바 없애기

        setContentView(R.layout.activity_logout_popup);

        ImageView logoutYesButton = (ImageView) findViewById(R.id.logoutYesButton);
        logoutYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Intent intent = new Intent(LogoutPopupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ImageView logoutNoButton = (ImageView) findViewById(R.id.logoutNoButton);
        logoutNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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