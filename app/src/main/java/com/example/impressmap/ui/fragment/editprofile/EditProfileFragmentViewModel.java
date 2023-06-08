package com.example.impressmap.ui.fragment.editprofile;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.UserCase;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;

public class EditProfileFragmentViewModel extends ViewModel
{
    private final UserCase userCase;

    public EditProfileFragmentViewModel()
    {
        userCase = new UserCase();
    }

    public void update(User user,
                       SuccessCallback successCallback)
    {
        userCase.update(user, successCallback);
    }
}
