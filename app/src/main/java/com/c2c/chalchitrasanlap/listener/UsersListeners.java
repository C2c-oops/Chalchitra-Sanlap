package com.c2c.chalchitrasanlap.listener;

import com.c2c.chalchitrasanlap.models.User;

public interface UsersListeners {

    void initiateVideoMeeting(User user);

    void initiateAudioMeeting(User user);

    void onMultipleUsersAction(Boolean isMultipleUsersSelected);
}
