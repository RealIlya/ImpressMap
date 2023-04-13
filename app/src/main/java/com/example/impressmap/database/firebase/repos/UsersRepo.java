package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.AllUsersLiveData;
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

    @Override
    public void insert(User user)
    {
        String userKey = usersRef.push().getKey();

        Map<String, Object> data = user.prepareToTransferToDatabase();

        usersRef.child(userKey).updateChildren(data);
    }

    @Override
    public void update(User user)
    {

    }

    @Override
    public void delete(User user)
    {

    }
}
