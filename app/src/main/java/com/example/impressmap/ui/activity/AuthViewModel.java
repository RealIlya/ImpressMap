package com.example.impressmap.ui.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.impressmap.database.firebase.cases.AuthorizationCase;
import com.example.impressmap.preference.SessionPreferences;
import com.example.impressmap.util.FailCallback;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;

public class AuthViewModel extends AndroidViewModel
{
    private final AuthorizationCase authorizationCase;
    private final SessionPreferences sessionPreferences;

    public AuthViewModel(@NonNull Application application)
    {
        super(application);

        authorizationCase = new AuthorizationCase();
        sessionPreferences = new SessionPreferences(application);
    }

    public void signUp(String name,
                       String surname,
                       String email,
                       String password,
                       boolean rememberMe,
                       SuccessCallback successCallback,
                       FailCallback failCallback,
                       FieldEmptyCallback fieldEmptyCallback)
    {
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty())
        {
            fieldEmptyCallback.onEmpty();
            return;
        }

        authorizationCase.signUp(name, surname, email, password, () ->
        {
            if (rememberMe)
            {
                sessionPreferences.setUser(email, password);
            }

            successCallback.onSuccess();
        }, failCallback);
    }

    public void signIn(String email,
                       String password,
                       boolean rememberMe,
                       SuccessCallback successCallback,
                       FailCallback failCallback,
                       FieldEmptyCallback fieldEmptyCallback)
    {
        if (email.isEmpty() || password.isEmpty())
        {
            fieldEmptyCallback.onEmpty();
            return;
        }

        authorizationCase.signIn(email, password, () ->
        {
            if (rememberMe)
            {
                sessionPreferences.setUser(email, password);
            }

            successCallback.onSuccess();
        }, failCallback);
    }

    public void tryToSignIn(SuccessCallback successCallback,
                            FailCallback failCallback)
    {
        String[] userInfo = sessionPreferences.getUserInfo();

        if (userInfo != null)
        {
            authorizationCase.signIn(userInfo[0], userInfo[1], successCallback, failCallback);
        }
        else
        {
            failCallback.onFail();
        }
    }
}
