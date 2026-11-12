package com.example.kabinregistration;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;

/** Keeps logged-in user session in SharedPreferences & FirebaseAuth. */
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

    public void saveUserUid(String uid) {
        prefs.edit().putString("user_uid", uid).apply();
    }

    public String getUserUid() {
        String uid = prefs.getString("user_uid", null);
        if (uid == null && FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return uid;
    }

    public boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null || getUserId() != -1;
    }

    public void logout() {
        prefs.edit().clear().apply();
        try {
            FirebaseAuth.getInstance().signOut();
        } catch (Exception ignored) {
        }
    }
}
