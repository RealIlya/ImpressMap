package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.CHILD_ID_NODE;
import static com.example.impressmap.util.Constants.Keys.EMAIL_NODE;
import static com.example.impressmap.util.Constants.Keys.FULL_NAME_NODE;
import static com.example.impressmap.util.Constants.Keys.PHONE_NUMBER_NODE;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;
import static com.example.impressmap.util.Constants.UID;

import androidx.lifecycle.LiveData;

import com.example.impressmap.database.DatabaseRepo;
import com.example.impressmap.database.firebase.data.AllUsersLiveData;
import com.example.impressmap.database.firebase.data.UserLiveData;
import com.example.impressmap.model.data.DatabaseTransfer;
import com.example.impressmap.model.data.User;
import com.example.impressmap.util.SuccessCallback;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersRepo implements DatabaseRepo<User>, DatabaseTransfer<User>
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
    public void insert(User user,
                       SuccessCallback successCallback)
    {
        Map<String, Object> data = toMap(user);
        usersRef.child(user.getId())
                .updateChildren(data)
                .addOnSuccessListener(unused -> successCallback.onSuccess());
    }

    @Override
    public void update(User user,
                       SuccessCallback successCallback)
    {
        Map<String, Object> data = toMap(user);
        usersRef.child(user.getId())
                .updateChildren(data)
                .addOnSuccessListener(unused -> successCallback.onSuccess());
    }

    @Override
    public Map<String, Object> toMap(User user)
    {
        Map<String, Object> data = new HashMap<>();

        data.put(CHILD_ID_NODE, user.getId());
        data.put(FULL_NAME_NODE, user.getFullName());
        data.put(EMAIL_NODE, user.getEmail());
        data.put(PHONE_NUMBER_NODE, user.getPhoneNumber());

        return data;
    }
}
