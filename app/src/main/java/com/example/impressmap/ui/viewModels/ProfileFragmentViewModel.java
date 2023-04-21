package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.UserCase;
import com.example.impressmap.model.data.User;

public class ProfileFragmentViewModel extends ViewModel
{
    private final UserCase userCase;

    public ProfileFragmentViewModel()
    {
        userCase = new UserCase();
    }

    public LiveData<User> getUser()
    {
        return userCase.getUser();
    }

    public void changeFullName(String newFullName)
    {
        userCase.changeFullName(newFullName);
    }
}
