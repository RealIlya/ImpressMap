package com.example.impressmap.database.firebase.repos;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;
import static com.example.impressmap.util.Constants.UID;

import com.example.impressmap.model.data.User;
import com.google.firebase.database.DatabaseReference;

public class UserRepo
{
    private final DatabaseReference userRef;

    public UserRepo()
    {
        userRef = DATABASE_REF.child(USERS_NODE).child(UID);
    }

    public User getCurrentUser()
    {
        return userRef.get().getResult().getValue(User.class);
    }
}
