package com.example.impressmap.preference;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

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

    public void putUser(String email,
                        String password)
    {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(EMAIL_KEY, email);
        edit.putString(PASSWORD_KEY, password);
        edit.apply();
    }

    public void deleteUser()
    {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(EMAIL_KEY, null);
        edit.putString(PASSWORD_KEY, null);
        edit.apply();
    }

    public String[] getUserInfo()
    {
        String email = preferences.getString(EMAIL_KEY, "");
        String password = preferences.getString(PASSWORD_KEY, "");

        boolean emailExists = !email.isEmpty() && preferences.contains(EMAIL_KEY);
        boolean passwordExists = !password.isEmpty() && preferences.contains(PASSWORD_KEY);

        return emailExists && passwordExists ? new String[]{email, password} : null;
    }
}
