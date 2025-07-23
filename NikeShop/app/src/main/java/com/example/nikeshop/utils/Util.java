package com.example.nikeshop.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Util {
    public static int userId;

    public static int getUserIdFromSession(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);
        return userId;
    }
}
