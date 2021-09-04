package com.c2c.chalchitrasanlap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.c2c.chalchitrasanlap.R;
import com.c2c.chalchitrasanlap.models.User;

public class OutgoingInvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);

        ImageView imgMeetingType = findViewById(R.id.imgMeetingType);
        String meetingType = getIntent().getStringExtra("type");

        if(meetingType!=null) {
            if(meetingType.equals("video")) {
                imgMeetingType.setImageResource(R.drawable.ic_round_videocam_24);
            }
        }

        TextView txtFirstChar = findViewById(R.id.txtFirstChar);
        TextView txtUsername = findViewById(R.id.txtUserName);
        TextView txtEmail = findViewById(R.id.txtEmail);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            txtFirstChar.setText(user.firstName.substring(0, 1));
            txtUsername.setText(String.format("%s %s", user.firstName, user.lastName));
            txtEmail.setText(user.email);
        }

        ImageView imgRejectInvitation = findViewById(R.id.imgRejectInvitation);
        imgRejectInvitation.setOnClickListener(v -> onBackPressed());
    }
}