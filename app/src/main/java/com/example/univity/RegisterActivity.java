package com.example.univity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private EditText editEmail, editPwd, editPwdChk;
    private ImageView nextBtn;
    private TextView tvIdChk, tvPwdChk, tvIdChkView;

    private boolean emailChk = false;
    private boolean pwdChk = false;
    private boolean doubleChk = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity");

        editEmail = (EditText)findViewById(R.id.editEmail);
        editPwd = (EditText)findViewById(R.id.editPwd);
        editPwdChk = (EditText)findViewById(R.id.editPwdChk);
        nextBtn = (ImageView)findViewById(R.id.nextBtn2);
        tvIdChk = (TextView)findViewById(R.id.tvIdChk);
        tvPwdChk = (TextView)findViewById(R.id.tvPwdChk);
        tvIdChkView = (TextView)findViewById(R.id.tvIdChkView);

        Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;


        String strId = editEmail.getText().toString();
        String strPwd = editPwdChk.getText().toString();

        tvIdChkView.setVisibility(View.INVISIBLE);
        tvPwdChk.setVisibility(View.INVISIBLE);


        tvIdChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!editEmail.getText().toString().isEmpty()){ //이메일 칸이 비어있지 않으면

                    mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity").child("UserAccount");

                    mDatabaseRef.orderByChild("id").equalTo(editEmail.getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.d("TAG",editEmail.getText().toString());
                                    if(!snapshot.exists()){
                                        tvIdChkView.setVisibility(View.VISIBLE);
                                        tvIdChkView.setText("*사용 가능한 이메일입니다.");
                                        tvIdChkView.setTextColor(Color.parseColor("#FFF27E"));
                                        doubleChk = true;
                                        emailChk = true;
                                    }else {
                                        tvIdChkView.setVisibility(View.VISIBLE);
                                        tvIdChkView.setText("*이미 가입된 아이디입니다.");
                                        tvIdChkView.setTextColor(Color.parseColor("#E81414"));
                                        Log.d("TAG", "이미 가입된 아이디");
                                        emailChk = false;
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }else{
                    tvIdChkView.setVisibility(View.VISIBLE);
                    tvIdChkView.setText("*이메일을 입력해주세요.");
                    tvIdChkView.setTextColor(Color.parseColor("#E81414"));
                }

            }
        });

        editPwdChk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editPwd.getText().toString().equals(editPwdChk.getText().toString())){
                    tvPwdChk.setVisibility(View.VISIBLE);
                    tvPwdChk.setText("*비밀번호가 일치합니다.");
                    tvPwdChk.setTextColor(Color.parseColor("#4CAF50"));
                }else {
                    tvPwdChk.setVisibility(View.VISIBLE);
                    tvPwdChk.setText("*비밀번호가 일치하지 않습니다.");
                    tvPwdChk.setTextColor(Color.parseColor("#E81414"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editPwd.getText().toString().equals(editPwdChk.getText().toString())){
                    tvPwdChk.setVisibility(View.VISIBLE);
                    tvPwdChk.setText("*비밀번호가 일치합니다.");
                    tvPwdChk.setTextColor(Color.parseColor("#FFF27E"));
                    pwdChk = true;
                }else {
                    tvPwdChk.setVisibility(View.VISIBLE);
                    tvPwdChk.setText("*비밀번호가 일치하지 않습니다.");
                    tvPwdChk.setTextColor(Color.parseColor("#E81414"));
                    pwdChk = false;
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(emailChk == false){

                    if(doubleChk == false){

                        Toast.makeText(RegisterActivity.this, "중복 확인 버튼을 눌러주세요", Toast.LENGTH_SHORT).show();

                    }else if(editEmail.getText().toString().equals("")){

                        Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(RegisterActivity.this, "이미 가입된 아이디입니다", Toast.LENGTH_SHORT).show();

                        tvIdChkView.setVisibility(View.VISIBLE);
                        tvIdChkView.setText("*이미 가입된 아이디입니다.");
                        tvIdChkView.setTextColor(Color.parseColor("#E81414"));
                    }
                }else if(pwdChk == false){
                    tvPwdChk.setVisibility(View.VISIBLE);
                    tvPwdChk.setText("*비밀번호가 일치하지 않습니다.");
                    tvPwdChk.setTextColor(Color.parseColor("#E81414"));
                }else{
                    Intent intent = new Intent(RegisterActivity.this, Register2Activity.class);
                    intent.putExtra("USER_ID", editEmail.getText().toString());
                    intent.putExtra("USER_PWD", editPwdChk.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}