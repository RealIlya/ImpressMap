package com.example.impressmap.ui.fragment.editphone;

import androidx.lifecycle.ViewModel;

import com.example.impressmap.database.firebase.cases.UserCase;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.FieldEmptyCallback;
import com.example.impressmap.util.SuccessCallback;

public class EditPhoneFragmentViewModel extends ViewModel
{
    private final UserCase userCase;

    public EditPhoneFragmentViewModel()
    {
        userCase = new UserCase();
    }

    public void update(User user,
                       SuccessCallback successCallback,
                       FieldEmptyCallback fieldEmptyCallback)
    {
        if (user.getPhoneNumber().isEmpty())
        {
            fieldEmptyCallback.onEmpty();
            return;
        }

        userCase.update(user, successCallback);
    }
}