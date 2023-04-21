package com.example.impressmap.database.firebase.cases;

import static com.example.impressmap.util.Constants.UID;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.firebase.data.UserLiveData;
import com.example.impressmap.database.firebase.repos.UsersRepo;
import com.example.impressmap.model.data.User;

public class UserCase
{
    private UsersRepo usersRepo;

    public UserCase()
    {
        this.usersRepo = new UsersRepo();
    }

    public LiveData<User> getUser()
    {
        return usersRepo.getUser();
    }

    public void changeFullName(String newFullName)
    {
        User user = usersRepo.getUser().getValue();
        user.setFullName(newFullName);
        usersRepo.update(user);
    }
}
