package com.example.univity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class Register2Activity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;

    private EditText editName, editNickname, editEmail, editNumber;
    private ImageView nextBtn2;
    private TextView tvEmailChk, tvEmailView;

    private boolean emailChk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity");

        editName = (EditText) findViewById(R.id.editFindName);
        editNickname = (EditText)findViewById(R.id.editNickname);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editNumber = (EditText)findViewById(R.id.editFindNumber);
        nextBtn2 = (ImageView)findViewById(R.id.nextBtn2);
        tvEmailChk = (TextView)findViewById(R.id.tvEmailChk);
        tvEmailView = (TextView)findViewById(R.id.tvPwdChk);

        Intent intent = getIntent();
        String userId = intent.getStringExtra("USER_ID");
        String userPwd = intent.getStringExtra("USER_PWD");

        editEmail.setText(userId);
        editEmail.setEnabled(false);

        tvEmailView.setVisibility(View.INVISIBLE);
        editNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());


        tvEmailChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    tvEmailView.setVisibility(View.VISIBLE);

                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        public void run() {

                            mFirebaseAuth.createUserWithEmailAndPassword(userId, userPwd).addOnCompleteListener(Register2Activity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                        UserAccount account = new UserAccount();
                                        account.setId(userId);
                                        account.setPwd(userPwd);
                                        account.setUid(firebaseUser.getUid());
                                        account.setName(editName.getText().toString());
                                        account.setNickname(editNickname.getText().toString());
                                        account.setCoin(1000);
                                        account.setThunder(0);
                                        account.setGoalSteps(15);
                                        account.setTodaySteps(7);
                                        account.setPhonenumber(editNumber.getText().toString());
                                        account.setLevel(1);
                                        account.setTutorial(false); // tutorial 재생 했는지 유무

                                        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);


                                        Handler mHandler2 = new Handler();
                                        mHandler2.postDelayed(new Runnable() {
                                            public void run() {

                                                mFirebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(Register2Activity.this, "메일 발송 완료", Toast.LENGTH_SHORT).show();
                                                    }
                                                });


                                            }}, 4000);

                                        Timer mTimer;
                                        TimerTask mTask;

                                        mTimer = new Timer();
                                        mTask = new TimerTask() {
                                            @Override
                                            public void run() {

                                                mFirebaseAuth.signInWithEmailAndPassword(userId, userPwd).addOnCompleteListener(Register2Activity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if(task.isSuccessful()){

                                                            if(mFirebaseAuth.getCurrentUser().isEmailVerified()){

                                                                //이메일 인증되면
                                                                tvEmailChk.setText("인증완료");
                                                                tvEmailChk.setEnabled(false);
                                                                tvEmailChk.setTextColor(Color.parseColor("#4CAF50"));
                                                                emailChk = true;
                                                                mTimer.cancel();
                                                            }
                                                        }
                                                    }
                                                });

                                            }
                                        };

                                        mTimer.schedule(mTask, 0, 2000);


                                        Toast.makeText(Register2Activity.this, "회원가입 완료~", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(Register2Activity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }}, 800);


            }
        });

        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(editName.getText().toString()) && !TextUtils.isEmpty(editNickname.getText().toString())
                        && !TextUtils.isEmpty(editNumber.getText().toString())){

                    if(emailChk == false){
                        Toast.makeText(Register2Activity.this, "이메일 인증을 진행해주세요", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(Register2Activity.this, RegisterFinishActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }else {
                    Toast.makeText(Register2Activity.this, "모든 정보를 기입해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}