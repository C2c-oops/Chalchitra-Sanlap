package com.c2c.chalchitrasanlap.utilities;

public class Constants {

    public static final String KEY_COLLECTION_USERS = "users";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_USER_ID = "user_id";

    /**
     * FCM token is required of particular user,
     * required to send and receive a meeting invitation
     * so, we'll update user's token in db after sign up/in
     * and will remove from db on sign out
     */
    public static final String KEY_FCM_TOKEN = "fcm_token";

    public static final String KEY_PREFERENCE_NAME = "videoMeetingPreference";
    public static final String KEY_IS_SIGNED_IN = "isSignedIn";
}
