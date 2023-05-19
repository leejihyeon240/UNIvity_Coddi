package com.example.univity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class FindUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);


        ImageView findIdBtn = (ImageView)findViewById(R.id.findIdBtn);
        ImageView goLoginBtn = (ImageView)findViewById(R.id.goLoginBtn);

        findIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindUserActivity.this, FindIdActivity.class);
                startActivity(intent);
            }
        });

        goLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindUserActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });
    }
}