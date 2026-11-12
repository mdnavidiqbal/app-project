package com.example.kabinregistration;

import android.content.Context;
import android.content.SharedPreferences;

/** Keeps only the logged-in user's id in SharedPreferences. */
public class SessionManager {

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("kabin_session", Context.MODE_PRIVATE);
    }

    public void saveUserId(int id) {
        prefs.edit().putInt("user_id", id).apply();
    }

    public int getUserId() {
        return prefs.getInt("user_id", -1);
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}
