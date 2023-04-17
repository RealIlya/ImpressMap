package com.example.impressmap.database.firebase.cases;

import com.example.impressmap.database.firebase.repos.UsersRepo;
import com.example.impressmap.model.data.User;

public class UpdateUserCase
{
    private UsersRepo usersRepo;

    public UpdateUserCase()
    {
        this.usersRepo = new UsersRepo();
    }

    public void changeFullName(String newFullName)
    {
        User user = usersRepo.getUser().getValue();
        user.setFullName(newFullName);
        usersRepo.update(user);
    }
}
