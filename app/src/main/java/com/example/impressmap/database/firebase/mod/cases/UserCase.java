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

    public void update(User user,
                       SuccessCallback successCallback)
    {
        usersRepo.update(user, successCallback);
    }
}
