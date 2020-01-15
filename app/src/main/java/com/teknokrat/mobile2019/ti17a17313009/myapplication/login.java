package com.teknokrat.mobile2019.ti17a17313009.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mregisterbtn;
    TextView mCreateBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);

        mregisterbtn = findViewById(R.id.registerBtn);
        mCreateBtn= findViewById(R.id.createText);

        fAuth = FirebaseAuth.getInstance();
        progressBar2 = findViewById(R.id.progressBar2);

        mregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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

                progressBar2.setVisibility(View.VISIBLE);

                //authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login.this,"Logged In Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(login.this,"ERROR!!!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar2.setVisibility(View.GONE);
                        }

                    }
                });
            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),register.class));
            }
        });
    }
}
