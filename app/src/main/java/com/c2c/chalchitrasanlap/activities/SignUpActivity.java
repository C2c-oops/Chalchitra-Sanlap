package com.c2c.chalchitrasanlap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.c2c.chalchitrasanlap.R;
import com.c2c.chalchitrasanlap.utilities.Constants;
import com.c2c.chalchitrasanlap.utilities.PreferenceManager;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getName();

    private EditText inputFirstName;
    private EditText inputLastName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputConfirmPassword;

    private MaterialButton btnSignUp;

    private ProgressBar signUpProgressBar;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferenceManager = new PreferenceManager(getApplicationContext());

        findViewById(R.id.textSignIn).setOnClickListener(v -> onBackPressed());
        findViewById(R.id.textSignIn).setOnClickListener(v -> onBackPressed());

        inputFirstName = findViewById(R.id.inputSignUpFirstName);
        inputLastName = findViewById(R.id.inputSignUpLastName);
        inputEmail = findViewById(R.id.inputSignUpEmail);
        inputPassword = findViewById(R.id.inputSignUpPassword);
        inputConfirmPassword = findViewById(R.id.inputSignUpConfirmPassword);

        btnSignUp = findViewById(R.id.buttonSignUp);

        signUpProgressBar = findViewById(R.id.signUpProgressBar);

        btnSignUp.setOnClickListener(v -> {
            if(inputFirstName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter first name", Toast.LENGTH_SHORT).show();
            } else if(inputLastName.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter last name", Toast.LENGTH_SHORT).show();
            } else if(inputEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            } else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
                Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            } else if(inputPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            } else if(inputConfirmPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Confirm password is required ", Toast.LENGTH_SHORT).show();
            } else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString())) {
                Toast.makeText(this, "Password & confirm password must be same", Toast.LENGTH_SHORT).show();
            } else {
                signUp();
            }
        });
    }

    private void signUp() {
        btnSignUp.setVisibility(View.INVISIBLE);
        signUpProgressBar.setVisibility(View.VISIBLE);

        //firestore data insert testing code
        /*
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put("first_name", "Sahil");
        user.put("last_name", "Singh");
        user.put("email", "sahil.singh@email.com");
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "User inserted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(error -> {
                    Log.e(TAG, "onCreate: " + error);
                    Toast.makeText(this, "Error adding user: " , Toast.LENGTH_SHORT).show();
                });
         */

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString());
        user.put(Constants.KEY_LAST_NAME, inputLastName.getText().toString());
        user.put(Constants.KEY_EMAIL, inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, inputPassword.getText().toString());

        db.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preferenceManager.putString(Constants.KEY_FIRST_NAME, inputFirstName.getText().toString());
                    preferenceManager.putString(Constants.KEY_LAST_NAME, inputLastName.getText().toString());
                    preferenceManager.putString(Constants.KEY_EMAIL, inputEmail.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(error -> {
                    signUpProgressBar.setVisibility(View.INVISIBLE);
                    btnSignUp.setVisibility(View.VISIBLE);
                    Log.e(TAG, "onCreate: " + error);
                    Toast.makeText(this, "Error adding user" , Toast.LENGTH_SHORT).show();
                });
    }
}