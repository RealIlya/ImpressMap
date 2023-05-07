package com.example.impressmap.database.firebase.cases;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.repos.UsersRepo;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.SuccessCallback;

public class UserCase
{
    private final UsersRepo usersRepo;

    public UserCase()
    {
        this.usersRepo = new UsersRepo();
    }

    public LiveData<User> getUser()
    {
        return usersRepo.getUser();
    }

    public void changeFullName(String newFullName, SuccessCallback successCallback)
    {
        User user = usersRepo.getUser().getValue();
        user.setFullName(newFullName);
        usersRepo.update(user, successCallback);
    }
}
