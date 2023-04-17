package com.example.impressmap.ui.viewModels;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.UpdateUserCase;

public class ProfileFragmentViewModel extends ViewModel
{
    private UpdateUserCase updateUserCase;

    public ProfileFragmentViewModel(UpdateUserCase updateUserCase)
    {
        this.updateUserCase = updateUserCase;
    }

    public void changeFullName(String newFullName)
    {
        updateUserCase.changeFullName(newFullName);
    }
}
