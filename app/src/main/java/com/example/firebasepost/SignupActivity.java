package com.example.firebasepost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText mNickname, mEmailText, mPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNickname = findViewById(R.id.sign_nickname);
        mEmailText = findViewById(R.id.sign_email);
        mPasswordText = findViewById(R.id.sign_password);

        findViewById(R.id.sign_success).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mAuth.createUserWithEmailAndPassword(mEmailText.getText().toString(), mPasswordText.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null){
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(FirebaseID.documentId, user.getUid());
                                userMap.put(FirebaseID.nickname, mNickname.getText().toString());
                                userMap.put(FirebaseID.email, mEmailText.getText().toString());
                                userMap.put(FirebaseID.password, mPasswordText.getText().toString());
                                mStore.collection(FirebaseID.user).document(user.getUid()).set(userMap, SetOptions.merge()); // SetOptions.merge() -> 덮어쓰기
                                finish();
                            }
                        }
                        else{
                            Toast.makeText(SignupActivity.this, "회원가입 에러", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}