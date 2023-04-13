package com.example.impressmap.database.firebase;

import static com.example.impressmap.util.Constants.DATABASE_REF;
import static com.example.impressmap.util.Constants.Keys.USERS_NODE;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// you don't need to AllUsersLiveData
public class AllUsersLiveData extends LiveData<List<User>>
{
    private final DatabaseReference usersRef;

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            List<User> users = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren())
            {
                User value = dataSnapshot.getValue(User.class);
                users.add(value);
            }
            setValue(users);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {

        }
    };

    public AllUsersLiveData()
    {
        usersRef = DATABASE_REF.child(USERS_NODE);
    }

    @Override
    protected void onActive()
    {
        usersRef.addValueEventListener(listener);
        super.onActive();
    }

    @Override
    protected void onInactive()
    {
        usersRef.removeEventListener(listener);
        super.onInactive();
    }
}
