package com.example.impressmap.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.UserCase;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.SuccessCallback;

public class ProfileFragmentViewModel extends ViewModel
{
    private final UserCase userCase;

    private LiveData<User> user;

    public ProfileFragmentViewModel()
    {
        userCase = new UserCase();
    }

    public LiveData<User> getUser()
    {
        return userCase.getUser();
    }

    public void changeFullName(String newFullName,
                               SuccessCallback successCallback)
    {
        userCase.changeFullName(newFullName, successCallback);
    }
}
