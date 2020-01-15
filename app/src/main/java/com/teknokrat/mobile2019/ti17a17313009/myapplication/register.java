package com.teknokrat.mobile2019.ti17a17313009.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullname,mEmail,mPassword,mPhone;
    Button mregisterbtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    ProgressBar progressBar;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullname = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.Email);
        mPhone = findViewById(R.id.Phone);
        mPassword = findViewById(R.id.Password);

        mregisterbtn = findViewById(R.id.registerBtn);
        mLoginBtn= findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullname = mFullname.getText().toString();
                final String phone = mPhone.getText().toString();

                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email Is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password Is Required.");
                    return;
                }

                if (password.length() < 6){
                    mPassword.setError("Password Must be >= 6 characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //register  the user in firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(register.this,"user Created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            final Map<String,Object> user = new HashMap<>();
                            user.put("fName",fullname);
                            user.put("email",email);
                            user.put("phone",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //ctrl+alt+c ON "TAG"
                                    Log.d(TAG,"On Success: user profile is created for "+ userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }else{
                            Toast.makeText(register.this,"Error" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),login.class));
            }
        });
    }
}

