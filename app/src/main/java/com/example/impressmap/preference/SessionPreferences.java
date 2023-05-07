package com.example.impressmap.preference;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SessionPreferences
{
    private static final String SESSION_PREFS = "SESSION_PREFS";
    private static final String EMAIL_KEY = "EMAIL_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";
    private final SharedPreferences preferences;

    public SessionPreferences(@NonNull Context context)
    {
        preferences = context.getSharedPreferences(SESSION_PREFS, Context.MODE_PRIVATE);
    }

    public void setUser(String email,
                        String password)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(EMAIL_KEY, email);
        editor.putString(PASSWORD_KEY, password);
        editor.apply();
    }

    public void removeUser()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(EMAIL_KEY);
        editor.remove(PASSWORD_KEY);
        editor.apply();
    }

    @Nullable
    public String[] getUserInfo()
    {
        boolean userExists = preferences.contains(EMAIL_KEY) && preferences.contains(PASSWORD_KEY);

        if (userExists)
        {
            String email = preferences.getString(EMAIL_KEY, "");
            String password = preferences.getString(PASSWORD_KEY, "");
            return new String[]{email, password};
        }
        return null;
    }
}
