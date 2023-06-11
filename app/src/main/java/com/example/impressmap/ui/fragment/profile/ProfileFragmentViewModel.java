package com.example.impressmap.ui.fragment.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.impressmap.database.firebase.cases.AuthorizationCase;
import com.example.impressmap.preference.PositionPreferences;
import com.example.impressmap.preference.SessionPreferences;
import com.example.impressmap.util.SuccessCallback;

public class ProfileFragmentViewModel extends AndroidViewModel
{
    private final AuthorizationCase authorizationCase;
    private final SessionPreferences sessionPreferences;
    private final PositionPreferences positionPreferences;

    public ProfileFragmentViewModel(Application application)
    {
        super(application);
        authorizationCase = new AuthorizationCase();
        sessionPreferences = new SessionPreferences(application);
        positionPreferences = new PositionPreferences(application);
    }

    public void signOut(SuccessCallback successCallback)
    {
        authorizationCase.signOut(() ->
        {
            sessionPreferences.removeUser();
            positionPreferences.removePosition();
            successCallback.onSuccess();
        });
    }
}
