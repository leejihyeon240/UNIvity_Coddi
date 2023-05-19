package com.example.univity;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class StoreBuyFailActivity extends AppCompatActivity {

    private ImageView failOkBtn;
    boolean sound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_store_buy_fail);

        failOkBtn = (ImageView) findViewById(R.id.failOkBtn);

        failOkBtn.setOnClickListener(new View.OnClickListener() {
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