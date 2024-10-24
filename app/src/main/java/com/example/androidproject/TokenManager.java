package com.example.androidproject;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String TOKEN_KEY = "auth_token";
    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();
    }
}
