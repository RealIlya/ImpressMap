package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;
import static com.example.impressmap.util.Constants.UID;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.AllUsersLiveData;
import com.example.impressmap.database.firebase.data.UserLiveData;
import com.example.impressmap.model.data.User;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.Map;

public class UsersRepo implements DatabaseRepo<User>
{
    private final DatabaseReference usersRef;

    public UsersRepo()
    {
        usersRef = DATABASE_REF.child(USERS_NODE);
    }

    @Override
    public LiveData<List<User>> getAll()
    {
        return new AllUsersLiveData();
    }

    public LiveData<User> getUser()
    {
        return new UserLiveData(usersRef.child(UID));
    }

    @Override
    public void insert(User user)
    {
        Map<String, Object> data = user.prepareToTransferToDatabase();
        usersRef.child(user.getId()).updateChildren(data);
    }

    @Override
    public void update(User user)
    {
        Map<String, Object> data = user.prepareToTransferToDatabase();
        usersRef.child(user.getId()).updateChildren(data);
    }

    @Override
    public void delete(User user)
    {

    }
}
