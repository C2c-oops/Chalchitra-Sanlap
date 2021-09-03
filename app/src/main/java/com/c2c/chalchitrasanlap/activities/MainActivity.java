package com.c2c.chalchitrasanlap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.c2c.chalchitrasanlap.R;
import com.c2c.chalchitrasanlap.adapters.UsersAdapter;
import com.c2c.chalchitrasanlap.listener.UsersListeners;
import com.c2c.chalchitrasanlap.models.User;
import com.c2c.chalchitrasanlap.utilities.Constants;
import com.c2c.chalchitrasanlap.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UsersListeners {

    private PreferenceManager preferenceManager;

    private List<User> users;
    private UsersAdapter usersAdapter;

    private TextView txtErrorMsg;

    private ProgressBar userProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());

        TextView txtTitle = findViewById(R.id.textTitle);
        txtTitle.setText(
                String.format(
                        "%s %s",
                        preferenceManager.getString(Constants.KEY_FIRST_NAME),
                        preferenceManager.getString(Constants.KEY_LAST_NAME)
                )
        );

        findViewById(R.id.textSignOut).setOnClickListener(v -> signOut());

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null) {
                sendFCMTokenToDB(task.getResult());
            }
        });

        RecyclerView userRecyclerView = findViewById(R.id.usersRecyclerView);

        txtErrorMsg = findViewById(R.id.txtErrorMessage);
        userProgressBar = findViewById(R.id.usersProgressBar);

        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users, this);
        userRecyclerView.setAdapter(usersAdapter);

        getUsers();

    }

    private void getUsers() {
        userProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    userProgressBar.setVisibility(View.GONE);
                    String userId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (userId.equals(documentSnapshot.getId())) {
                                /**
                                 * excluding signed in user here
                                 * and showing rest
                                 **/
                                continue;
                            }
                            User user = new User();
                            user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME);
                            user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME);
                            user.email = documentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            users.add(user);
                        }
                        if (users.size() > 0) {
                            usersAdapter.notifyDataSetChanged();
                        } else {
                            txtErrorMsg.setText(String.format("%s", "No users available"));
                            txtErrorMsg.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txtErrorMsg.setText(String.format("%s", "No users available"));
                        txtErrorMsg.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void sendFCMTokenToDB(String token) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this, "Token updated successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(error -> Toast.makeText(this, "Unable to send token: " + error.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void signOut() {
        Toast.makeText(this, "Signing out...", Toast.LENGTH_SHORT).show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.clearPreferences();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(error -> Toast.makeText(this, "Unable to sign out", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void initiateVideoMeeting(User user) {
        if (user.token == null || user.token.trim().isEmpty()) {
            Toast.makeText(this,
                    user.firstName + " " + user.lastName + " is not available for meeting",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(this,
                    "Video meeting with " + user.firstName + " " + user.lastName,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    public void initiateAudioMeeting(User user) {
        if (user.token == null || user.token.trim().isEmpty()) {
            Toast.makeText(this,
                    user.firstName + " " + user.lastName + " is not available for meeting",
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            Toast.makeText(this,
                    "Audio meeting with " + user.firstName + " " + user.lastName,
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}