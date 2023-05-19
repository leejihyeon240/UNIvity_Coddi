package com.example.univity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class RegisterFinishActivity extends AppCompatActivity {

    ImageView goToLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_finish);

        goToLoginBtn = (ImageView)findViewById(R.id.goToLoginBtn);

        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterFinishActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}