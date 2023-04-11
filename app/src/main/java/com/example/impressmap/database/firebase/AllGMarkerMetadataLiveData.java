package com.example.impressmap.database.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.impressmap.model.data.GMarkerMetadata;
import com.example.impressmap.ui.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllGMarkerMetadataLiveData extends LiveData<List<GMarkerMetadata>>
{
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final DatabaseReference database = FirebaseDatabase.getInstance()
                                                               .getReference()
                                                               .child(Constants.Key.GMARKERS);

    private final ValueEventListener listener = new ValueEventListener()
    {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot)
        {
            List<GMarkerMetadata> gMarkerMetadata = new ArrayList<>();
            for (DataSnapshot dataSnapshot : snapshot.getChildren())
            {
                GMarkerMetadata value = dataSnapshot.getValue(GMarkerMetadata.class);
                gMarkerMetadata.add(value);
            }

            setValue(gMarkerMetadata);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error)
        {

        }
    };

    @Override
    protected void onActive()
    {
        database.addValueEventListener(listener);
        super.onActive();
    }

    @Override
    protected void onInactive()
    {
        database.removeEventListener(listener);
        super.onInactive();
    }
}
