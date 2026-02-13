package com.example.newsapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginManager {
    private static final String PREF_NAME = "AppPreferences";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public LoginManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setLogin(boolean isLoggedIn, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, null);
    }

    public void logoutUser() {
        editor.remove(KEY_IS_LOGGED_IN);
        editor.remove(KEY_EMAIL);
        editor.apply();
    }
}
