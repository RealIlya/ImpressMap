package com.example.impressmap.database.firebase.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.data.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserLiveData extends LiveData<User>
{
    private final DatabaseReference userRef;

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            User user = snapshot.getValue(User.class);
            setValue(user);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {

        }
    };

    public UserLiveData(DatabaseReference userRef)
    {
        this.userRef = userRef;
    }

    @Override
    protected void onActive()
    {
        userRef.addValueEventListener(listener);
    }

    @Override
    protected void onInactive()
    {
        userRef.removeEventListener(listener);
    }
}
