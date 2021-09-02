package com.c2c.chalchitrasanlap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        findViewById(R.id.textSignIn).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.textSignIn).setOnClickListener(v -> onBackPressed());
    }
}