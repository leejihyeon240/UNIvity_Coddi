package com.example.univity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import javax.security.auth.AuthPermission;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText ediTextId = (EditText) findViewById(R.id.editTextId);
        EditText ediTextPwd = (EditText) findViewById(R.id.editTextPwd);
        TextView registerBtn = (TextView) findViewById(R.id.registerBtn);
        TextView findIdPwdBtn = (TextView) findViewById(R.id.findIdPwdBtn);
        ImageView loginBtn = (ImageView) findViewById(R.id.loginBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        findIdPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindUserActivity.class);
                startActivity(intent);
            }
        });

        ediTextId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!ediTextId.getText().toString().isEmpty() && !ediTextPwd.getText().toString().isEmpty()){
                    loginBtn.setImageResource(R.drawable.login_button2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ediTextId.getText().toString().equals("") || ediTextPwd.getText().toString().equals("")){
                    loginBtn.setImageResource(R.drawable.login_button);
                }

            }
        });

        ediTextPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!ediTextId.getText().toString().isEmpty() && !ediTextPwd.getText().toString().isEmpty()){
                    loginBtn.setImageResource(R.drawable.login_button2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ediTextId.getText().toString().equals("") || ediTextPwd.getText().toString().equals("")){
                    loginBtn.setImageResource(R.drawable.login_button);
                }
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mFirebaseAuth = FirebaseAuth.getInstance();


                mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity").child("UserAccount");

                String strEmail = ediTextId.getText().toString();
                String strPwd = ediTextPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                //로그인 성공

                                mDatabaseRef.child(mFirebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        UserAccount user = snapshot.getValue(UserAccount.class);
                                        Log.d("LOG", "dd");

                                        if(user.getTutorial()){
                                            // 튜토리얼 재생 했다 (true) -> 메인화면
                                            Log.d("LOG", "1");
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            Log.d("LOG", "2");
                                            // 튜토리얼 재생 *안 했다 (false) -> 튜토리얼 (스토리, newuser1, newuser2)
                                            Intent intent = new Intent(LoginActivity.this, StoryActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }


                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }
}