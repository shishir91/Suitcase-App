package com.example.suitcase;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
//    private static final String PREF_NAME = "MyAppSession";
//    private static final String KEY_ID = "1";
//    private static final String KEY_NAME = "name";
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_LOGGED_IN = "loggedIn";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
//    private Context context;

    public SessionManager(Context context) {
//        this.context = context;
        sharedPreferences = context.getSharedPreferences("appKey", 0);
        editor = sharedPreferences.edit();
    }

    public void createSession(Integer id, String name, String email) {
        editor.putInt("KEY_ID", id);
        editor.putString("KEY_NAME", name);
        editor.putString("KEY_EMAIL", email);
        editor.putBoolean("KEY_LOGGED_IN", true);
        editor.commit();
//        editor.apply();
    }

    public void endSession() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("KEY_LOGGED_IN", false);
    }

    public Integer getID() {
        return sharedPreferences.getInt("KEY_ID", 0);
    }
    public String getName() {
        return sharedPreferences.getString("KEY_NAME", "");
    }
    public String getEmail() {
        return sharedPreferences.getString("KEY_EMAIL", "");
    }
}
