package com.c2c.chalchitrasanlap.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.c2c.chalchitrasanlap.R;
import com.c2c.chalchitrasanlap.network.ApiClient;
import com.c2c.chalchitrasanlap.network.ApiService;
import com.c2c.chalchitrasanlap.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomingInvitationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_invitation);

        ImageView imgMeetingType = findViewById(R.id.imgInMeetingType);
        String meetingType = getIntent().getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE);

        if (meetingType != null) {
            if (meetingType.equals("video")) {
                imgMeetingType.setImageResource(R.drawable.ic_round_videocam_24);
            }
        }

        TextView txtFirstChar = findViewById(R.id.txtInFirstChar);
        TextView txtUsername = findViewById(R.id.txtInUsername);
        TextView txtEmail = findViewById(R.id.txtInEmail);

        String firstName = getIntent().getStringExtra(Constants.KEY_FIRST_NAME);
        if (firstName != null) {
            txtFirstChar.setText(firstName.substring(0, 1));
        }

        txtUsername.setText(
                String.format(
                        "%s %s",
                        firstName,
                        getIntent().getStringExtra(Constants.KEY_LAST_NAME)
                )
        );

        txtEmail.setText(getIntent().getStringExtra(Constants.KEY_EMAIL));

        ImageView imgAcceptInvitation = findViewById(R.id.imgInAcceptInvitation);
        imgAcceptInvitation.setOnClickListener(v -> sendInvitationResponse(
                Constants.REMOTE_MSG_INVITATION_ACCEPTED,
                getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN)
        ));

        ImageView imgRejectInvitation = findViewById(R.id.imgInRejectInvitation);
        imgRejectInvitation.setOnClickListener(v-> sendInvitationResponse(
                Constants.REMOTE_MSG_INVITATION_REJECTED,
                getIntent().getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN)
        ));
     }

    private void sendInvitationResponse(String type, String receiverToken) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, type);

            body.put(Constants.REMOTE_MSG_DATA, data);
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), type);

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
                    if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {
                        Toast.makeText(IncomingInvitationActivity.this, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(IncomingInvitationActivity.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(IncomingInvitationActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable error) {
                Toast.makeText(IncomingInvitationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}