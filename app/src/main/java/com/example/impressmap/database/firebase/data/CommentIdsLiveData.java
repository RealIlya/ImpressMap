package com.example.impressmap.database.firebase.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.CommentId;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentIdsLiveData extends LiveData<List<String>>
{
    private final DatabaseReference commentsRef;

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            List<String> values = new ArrayList<>();

            for (DataSnapshot child : snapshot.getChildren())
            {
                values.add(child.getValue(CommentId.class).getId());
            }

            setValue(values);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {

        }
    };

    public CommentIdsLiveData(DatabaseReference commentsRef)
    {
        this.commentsRef = commentsRef;
    }

    @Override
    protected void onActive()
    {
        commentsRef.addValueEventListener(listener);
    }

    @Override
    protected void onInactive()
    {
        commentsRef.removeEventListener(listener);
    }
}
