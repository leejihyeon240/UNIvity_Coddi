package com.example.univity;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class FindIdActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;

    private EditText editFindName, editFindNumber;
    private ImageView findFinishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);



        mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity");

        editFindName = (EditText) findViewById(R.id.editFindName);
        editFindNumber = (EditText) findViewById(R.id.editFindNumber);
        findFinishBtn = (ImageView) findViewById(R.id.findFinishBtn);

        String strName = editFindName.getText().toString();
        String strNumber = editFindNumber.getText().toString();

        findFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabaseRef = FirebaseDatabase.getInstance().getReference("UNIvity").child("UserAccount");
                mDatabaseRef.orderByChild("name").equalTo(editFindName.getText().toString())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){

                                    UserAccount user = snapshot.getValue(UserAccount.class);

                                    Log.d("USER", String.valueOf(user.getId()));


                                    if(editFindNumber.getText().toString().equals(String.valueOf(user.getPhonenumber()))){
                                        Log.d("email", user.getId());
                                    }else{
                                        Log.d("email", "같지않음");
                                    }

                                    String a = snapshot.getKey();

                                    Log.d("TAG1", String.valueOf(user));
                                    Log.d("TAG2", String.valueOf(a));

                                }else {

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                finish();
            }
        });




    }
}