package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.UpdateUserCase;

public class ProfileFragmentViewModel extends ViewModel
{
    private final UpdateUserCase updateUserCase;

    public ProfileFragmentViewModel()
    {
        updateUserCase = new UpdateUserCase();
    }

    public void changeFullName(String newFullName)
    {
        updateUserCase.changeFullName(newFullName);
    }
}
