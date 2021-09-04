package com.c2c.chalchitrasanlap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c2c.chalchitrasanlap.R;
import com.c2c.chalchitrasanlap.models.User;
import com.c2c.chalchitrasanlap.network.ApiClient;
import com.c2c.chalchitrasanlap.network.ApiService;
import com.c2c.chalchitrasanlap.utilities.Constants;
import com.c2c.chalchitrasanlap.utilities.PreferenceManager;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingInvitationActivity extends AppCompatActivity {

    private PreferenceManager preferenceManager;
    private String inviterToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_invitation);

        preferenceManager = new PreferenceManager(getApplicationContext());
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null) {
                inviterToken = task.getResult();
            }
        });

        ImageView imgMeetingType = findViewById(R.id.imgOutMeetingType);
        String meetingType = getIntent().getStringExtra("type");

        if(meetingType!=null) {
            if(meetingType.equals("video")) {
                imgMeetingType.setImageResource(R.drawable.ic_round_videocam_24);
            }
        }

        TextView txtFirstChar = findViewById(R.id.txtOutFirstChar);
        TextView txtUsername = findViewById(R.id.txtOutUsername);
        TextView txtEmail = findViewById(R.id.txtOutEmail);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            txtFirstChar.setText(user.firstName.substring(0, 1));
            txtUsername.setText(String.format("%s %s", user.firstName, user.lastName));
            txtEmail.setText(user.email);
        }

        ImageView imgRejectInvitation = findViewById(R.id.imgOutRejectInvitation);
        imgRejectInvitation.setOnClickListener(v -> onBackPressed());

        if (meetingType != null && user != null) {
            initiateMeeting(meetingType, user.token);
        }
    }

    private void initiateMeeting(String meetingType, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            /**
             * "data": {
             *      "type": "invitation",
             *      "meetingType": "video",
             *      "first_name": "Sahil",
             *      "last_name": "Singh",
             *      "email": "sahil.singh@gmail.com"
             * },
             */
            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION);
            data.put(Constants.REMOTE_MSG_MEETING_TYPE, meetingType);
            data.put(Constants.KEY_FIRST_NAME, preferenceManager.getString(Constants.KEY_FIRST_NAME));
            data.put(Constants.KEY_LAST_NAME, preferenceManager.getString(Constants.KEY_LAST_NAME));
            data.put(Constants.KEY_EMAIL, preferenceManager.getString(Constants.KEY_EMAIL));
            /**
             * "registration_ids": ["receiver_token"]
             */
            data.put(Constants.REMOTE_MSG_INVITER_TOKEN, inviterToken);

            /**
             * Body
             * {
             *      "body": { },
             *      "registration_ids": [ ]
             * }
             */
            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION);

        } catch (Exception exception) {
            Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void sendRemoteMessage(String remoteMessageBody, String type) {
        ApiClient.getClient().create(ApiService.class).sendRemoteMessage(
                Constants.getRemoteMessagingHeaders(), remoteMessageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                        Toast.makeText(OutgoingInvitationActivity.this, "Invitation Sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OutgoingInvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable error) {
                Toast.makeText(OutgoingInvitationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}