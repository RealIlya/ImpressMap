package com.example.impressmap.ui.fragment.editprofile;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.UserCase;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;

public class EditProfileFragmentViewModel extends ViewModel
{
    private final UserCase userCase;

    public EditProfileFragmentViewModel()
    {
        userCase = new UserCase();
    }

    public void changeFullName(String name,
                               String surname,
                               SuccessCallback successCallback,
                               FieldEmptyCallback fieldEmptyCallback)
    {
        if (name.isEmpty() || surname.isEmpty())
        {
            fieldEmptyCallback.onEmpty();
            return;
        }

        userCase.changeFullName(name + " " + surname, successCallback);
    }
}
