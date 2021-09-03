package com.c2c.chalchitrasanlap.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.c2c.chalchitrasanlap.R;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.textSignUp).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        //firestore data insert testing code
        /*FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                });*/


    }
}