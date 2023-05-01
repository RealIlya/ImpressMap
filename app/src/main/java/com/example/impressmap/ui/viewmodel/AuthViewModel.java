package com.example.impressmap.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.impressmap.database.firebase.cases.AuthorizationCase;
import com.example.impressmap.preference.SessionPreferences;
import com.example.impressmap.util.FailCallback;
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
                       SuccessCallback successCallback)
    {
        authorizationCase.signUp(name, surname, email, password, () ->
        {
            if (rememberMe)
            {
                sessionPreferences.putUser(email, password);
            }

            successCallback.onSuccess();
        });
    }

    public void signIn(String email,
                       String password,
                       boolean rememberMe,
                       SuccessCallback successCallback)
    {
        authorizationCase.signIn(email, password, () ->
        {
            if (rememberMe)
            {
                sessionPreferences.putUser(email, password);
            }

            successCallback.onSuccess();
        });
    }

    public void tryToSignIn(SuccessCallback successCallback,
                            FailCallback failCallback)
    {
        String[] userInfo = sessionPreferences.getUserInfo();

        if (userInfo != null)
        {
            authorizationCase.signIn(userInfo[0], userInfo[1], successCallback);
        }
        else
        {
            failCallback.onFail();
        }
    }
}
